package Server;

import GoGame.GoGame;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
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
    int size;
    Player currentPlayer;
    int passCounter;

    public Game() {
       // game = new GoGame(9);
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
            //System.out.println(game.board.toString());
            passCounter = 0;
            sendUpdatedBoard();
            currentPlayer = currentPlayer.opponent;
        }
    }

    public void sendUpdatedBoard() {
        currentPlayer.output.println("BOARD " + game.board.toString());
        currentPlayer.opponent.output.println("BOARD " + game.board.toString());
    }

    public void pass(Player player) {
        if (player != currentPlayer) {
            return;
        } else if (player.opponent == null) {
            return;
        }
        passCounter++;
        if (passCounter == 2){
            currentPlayer.output.println("PICK");
            currentPlayer.opponent.output.println("PICK");
            passCounter = 0;
        } else {
            currentPlayer.output.println("TURN");
            currentPlayer.opponent.output.println("TURN");
        }
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

    public void sendOpponentTerritory(Player player) {
        String currentTerritory, opponentTerritory;
        currentTerritory = player.territory;
        opponentTerritory = player.opponent.territory;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < currentTerritory.length(); i++) {
            if (currentTerritory.charAt(i) == 'n' && opponentTerritory.charAt(i) == 'g')
                builder.append("r");
            else if (currentTerritory.charAt(i) == 'g' && opponentTerritory.charAt(i) == 'n')
                builder.append("g");
            else
                builder.append("n");
        }
        player.output.println("TERR " + builder.toString());
        System.out.println(builder.toString());
        builder = new StringBuilder();
        for (int i = 0; i < opponentTerritory.length(); i++) {
            if (currentTerritory.charAt(i) == 'g' && opponentTerritory.charAt(i) == 'n')
                builder.append("r");
            else if (currentTerritory.charAt(i) == 'n' && opponentTerritory.charAt(i) == 'g')
                builder.append("g");
            else
                builder.append("n");
        }
        player.opponent.output.println("TERR " + builder.toString());
        System.out.println(builder.toString());
    }

    public void findWinner() {
        Point curr = game.board.getTerritoryAndCaptives(currentPlayer.stone, currentPlayer.territory);
        Point opp = game.board.getTerritoryAndCaptives(currentPlayer.opponent.stone, currentPlayer.opponent.territory);
        int currPoints = (int)curr.getX() - (int)opp.getY();
        int oppPoints = (int)opp.getX() - (int)curr.getY();

        if (currPoints > oppPoints) {
            currentPlayer.output.println("WIN");
            currentPlayer.opponent.output.println("LOSE");
        } else if (currPoints < oppPoints) {
            currentPlayer.output.println("LOSE");
            currentPlayer.opponent.output.println("WIN");
        } else {
            currentPlayer.output.println("TIE");
            currentPlayer.opponent.output.println("TIE");
        }
    }

    class Player implements Runnable{

        char stone;
        boolean choice = false;
        boolean agreed = false;
        String territory;
        Player opponent;
        Socket socket;
        Scanner input;
        PrintWriter output;

        public Player(Socket socket, char stone) {
            this.socket = socket;
            this.stone = stone;
            territory = "";
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
                this.output.println("Player 1");
                currentPlayer = this;
            } else {
                this.output.println("Player 2 " + size);
                opponent = currentPlayer;
                opponent.opponent = this;
            }
        }

        private void processCommands() {
            while (true) {
                if (input.hasNextLine()) {
                    String command = input.nextLine();
                    if (command.startsWith("MOVE")) {
                       // System.out.println(command);
                        String[] temp = command.split(" ");
                        //System.out.println(temp[1] + " " + temp[2]);
                        move(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), this);
                    } else if (command.startsWith("PASS")) {
                        pass(this);
                    } else if (command.startsWith("RESIGN")) {
                        resign(this);
                    } else if (command.startsWith("SEND")) {
                        String[] temp = command.split(" ");
                        this.territory = temp[1];
                        if (!this.opponent.territory.equals("")) {
                            sendOpponentTerritory(this);
                        }
                    } else if (command.startsWith("AGREE") || command.startsWith("REFUSE")) {
                        this.choice = true;
                        if (command.equals("AGREE"))
                            this.agreed = true;
                        if (this.opponent.choice) {
                            if (this.agreed && this.opponent.agreed)
                                findWinner();
                            else {
                                this.choice = false;
                                this.opponent.choice = false;
                                this.agreed = false;
                                this.opponent.agreed = false;
                                this.output.println("REPLAY");
                                this.opponent.output.println("REPLAY");
                            }
                        }
                    } else if (command.startsWith("SIZE")) {
                        String[] temp = command.split(" ");
                        size = Integer.parseInt(temp[1]);
                        game = new GoGame(size);
                    }
                }
            }
        }
    }
}
