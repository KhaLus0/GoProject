package GoGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;


public class GoBoard {
    private FieldState[][] board;
    private int size;
    private Point KoPoint = new Point(0, 0);
    private boolean isKo = false;

    public GoBoard(int size) {
        this.size = size;
        board = new FieldState[size+2][size+2];
        init(board);
    }

    private void init(FieldState[][] board) {
        //int size = board[0].length;
        //set borders to FieldState.BORDER
        for (int j = 0; j < size+2; j++) {
            board[0][j] = FieldState.BORDER;
            board[size+1][j] = FieldState.BORDER;
        }
        for (int i = 0; i < size+2; i++) {
            board[i][0] = FieldState.BORDER;
            board[i][size+1] = FieldState.BORDER;
        }
        //set the inside of board to  FieldState.FREE
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                board[i][j] = FieldState.FREE;
            }
        }

    }


    public FieldState[][] getBoard() {
        return board;
    }

    public boolean placeStone(int x, int y, Player player) {
        if (isAvailable(x, y, player)) {
            if (player.equals(Player.BLACK)) {
                board[x][y] = FieldState.BLACK;
            } else {
                board[x][y] = FieldState.WHITE;
            }
            return true;
        }
        return false;
    }

    public boolean isAvailable(int x, int y, Player player) {
        HashSet<Point> stones = getSetOfStonesToRemove(x, y, player);

        if ((new Point(x, y)).equals(KoPoint) && stones.size() == 1)
            return false;

        if (board[x][y].equals(FieldState.FREE) && (getNumOfLiberties(x, y) > 0 || !stones.isEmpty() || isPartOfAliveChain(x, y, player))) {
            removeChainOfStones(stones);

            //after succesful move restart Ko rule
            if (isKo) {
                isKo = false;
                KoPoint = new Point(0, 0);
            }

            //set KoPoint
            if (stones.size() == 1) {
                for (Point point: stones) {
                    KoPoint = new Point((int)point.getX(), (int)point.getY());
                    isKo = true;
                }
            }

           // System.out.println("Stone placed");
            return true;
        }
        //System.out.println("Couldn't place stone");
        return false;
    }

    public boolean isPartOfAliveChain(int x, int y, Player player) {
        FieldState state = board[x][y];
        if (player.equals(Player.BLACK)) {
            board[x][y] = FieldState.BLACK;
        } else {
            board[x][y] = FieldState.WHITE;
        }
        HashSet<Point> stones = getChainOfStones(x, y, new HashSet<Point>());
        boolean result = isChainAlive(stones);
        board[x][y] = state;
        return result;
    }


    public HashSet<Point> getSetOfStonesToRemove(int x, int y, Player player) {
        FieldState colour;
        FieldState state = board[x][y];
        if (player.equals(Player.WHITE)){
            colour = FieldState.BLACK;
            board[x][y] = FieldState.WHITE;
        } else {
            colour = FieldState.WHITE;
            board[x][y] = FieldState.BLACK;
        }

        HashSet<Point> stones = new HashSet<Point>();
        ArrayList<Point> possibleChains = new ArrayList<Point>();
        if (board[x-1][y].equals(colour))
            possibleChains.add(new Point(x-1, y));
        if (board[x][y-1].equals(colour))
            possibleChains.add(new Point(x, y-1));
        if (board[x+1][y].equals(colour))
            possibleChains.add(new Point(x+1, y));
        if (board[x][y+1].equals(colour))
            possibleChains.add(new Point(x, y+1));

        for (Point point: possibleChains) {
            HashSet<Point> tempSet = getChainOfStones((int)point.getX(), (int)point.getY(), new HashSet<Point>());
            if (!isChainAlive(tempSet))
                stones.addAll(tempSet);
        }

        board[x][y] = state;

        return stones;
    }

    //pass new Set when calling this method
    public HashSet<Point> getChainOfStones(int x, int y, HashSet<Point> stones) {
        //System.out.println(x + " " + y);
        FieldState state = board[x][y];
        stones.add(new Point(x, y));
        if (board[x-1][y].equals(state) && !stones.contains(new Point(x-1, y))) {
            stones = getChainOfStones(x-1, y, stones);
        }
        if (board[x][y-1].equals(state) && !stones.contains(new Point(x, y-1))) {
            stones = getChainOfStones(x, y-1, stones);
        }
        if (board[x+1][y].equals(state) && !stones.contains(new Point(x+1, y))) {
            stones = getChainOfStones(x+1, y, stones);
        }
        if (board[x][y+1].equals(state) && !stones.contains(new Point(x, y+1))) {
            stones = getChainOfStones(x, y+1, stones);
        }
        return stones;
    }

    public void removeChainOfStones(HashSet<Point> stones) {
        for (Point point: stones) {
            board[(int)point.getX()][(int)point.getY()] = FieldState.FREE;
        }
    }

    public boolean isChainAlive(HashSet<Point> stones) {
        for (Point point: stones) {
            if (getNumOfLiberties((int)point.getX(), (int)point.getY()) > 0)
                return true;
        }
        return false;
    }

    public int getNumOfLiberties(int x, int y) {
        int liberties = 0;
        if (board[x-1][y] == FieldState.FREE)
            liberties++;
        if (board[x+1][y] == FieldState.FREE)
            liberties++;
        if (board[x][y-1] == FieldState.FREE)
            liberties++;
        if (board[x][y+1] == FieldState.FREE)
            liberties++;
        return liberties;
    }

    public void printBoard() {
        for (int i = 0; i < size + 2; i++) {
            for (int j = 0; j < size + 2; j++) {
                if (board[i][j].equals(FieldState.BORDER))
                    System.out.print("# ");
                else if (board[i][j].equals(FieldState.FREE))
                    System.out.print("  ");
                else if (board[i][j].equals(FieldState.WHITE))
                    System.out.print("W ");
                else
                    System.out.print("B ");

            }
            System.out.println();
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                if (board[i][j].equals(FieldState.FREE))
                    result.append("f");
                else if (board[i][j].equals(FieldState.WHITE))
                    result.append("w");
                else
                    result.append("b");
            }
        }
        return result.toString();
    }

    public Point getTerritoryAndCaptives(char stone, String territory) {
        int territoryPoints = 0;
        int captives = 0;
        FieldState opp = stone == 'B' ? FieldState.WHITE : FieldState.BLACK;
        for (int i = 0; i < territory.length(); i++) {
            if (territory.charAt(i) == 'g') {
                int x = (i / size) + 1;
                int y = (i % size) + 1;
                if (board[x][y].equals(FieldState.FREE))
                    territoryPoints++;
                else if (board[x][y].equals(opp)) {
                    territoryPoints++;
                    captives++;
                }
            }
        }
        return new Point(territoryPoints, captives);
    }
}
