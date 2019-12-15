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
            ExecutorService pool = Executors.newFixedThreadPool(200);
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

    public Game() {
        game = new GoGame();
    }


    public void move(int x, int y, Player player) {
        if (player != currentPlayer) {
            return;
        } else if (player.opponent == null) {
            return;
        }
        boolean validMove = game.placeStone(x, y);
        if (validMove) {
            sendUpdatedBoard();
            currentPlayer = currentPlayer.opponent;
        }
    }

    public void sendUpdatedBoard() {
        currentPlayer.output.println(game.board.toString());
        currentPlayer.opponent.output.println(game.board.toString());
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
                if (opponent != null && opponent.output != null) {
                    opponent.output.println("OTHER_PLAYER_LEFT");
                }
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
            while (input.hasNextLine()) {
                String command = input.nextLine();
                if (command.startsWith("MOVE")) {
                    String[] temp = command.split(" ");
                    move(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), this);
                }
            }
        }
    }
}
