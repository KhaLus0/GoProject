package Server;

import GoGame.GoGame;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        try  {
            ServerSocket listener = new ServerSocket(58901);
            ExecutorService pool = Executors.newFixedThreadPool(2);
            while (true) {
                Game game = new Game();
                pool.execute(game.new Player(listener.accept(), 'B'));
                pool.execute(game.new Player(listener.accept(), 'W'));
            }
        } catch(Exception e) { }
    }
}

class Game {
    GoGame game;
    Player currentPlayer;
    int passCounter;

    public Game() {
        game = new GoGame();
        passCounter = 0;
    }


    public void move(int x, int y, Player player) {
        if (player != currentPlayer) {
            return;
        } else if (player.opponent == null) {
            return;
        }
        boolean validMove = game.placeStone(x, y);
        if (validMove) {
            passCounter = 0;
            sendUpdatedBoard();
            currentPlayer = currentPlayer.opponent;
        }
    }

    public void sendUpdatedBoard() {
        currentPlayer.output.println("BOARD" + game.board.toString());
        currentPlayer.opponent.output.println("BOARD" + game.board.toString());
    }

    public void pass(Player player) {
        if (player != currentPlayer) {
            return;
        } else if (player.opponent == null) {
            return;
        }
        passCounter++;
        currentPlayer = currentPlayer.opponent;
    }

    public void resign(Player player) {
        if (player != currentPlayer) {
            return;
        } else if (player.opponent == null) {
            return;
        }
        currentPlayer.output.println("LOSE");
        currentPlayer.opponent.output.println("WIN");
    }


    class Player implements Runnable{

        char stone;
        Player opponent;
        Socket socket;
        Scanner input;
        PrintWriter output;

        public Player(Socket socket, char stone) {
            this.socket = socket;
            this.stone = stone;
        }

        public void run() {
            try {
                setup();
                processCommands();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }

        private void setup() throws IOException {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            if (stone == 'B') {
                currentPlayer = this;
            } else {
                opponent = currentPlayer;
                opponent.opponent = this;
            }
        }

        private void processCommands() {
            while (true) {
                String command = input.nextLine();
                if (command.startsWith("MOVE")) {
                    String[] temp = command.split(" ");
                    move(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), this);
                } else if (command.startsWith("PASS" )) {
                    pass(this);
                } else if (command.startsWith("RESIGN")) {
                    resign(this);
                }
            }
        }
    }
}
