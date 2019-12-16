package GoGame;


public class GoGame {
    public GoBoard board;
    private Player currentPlayer;

    public GoGame(int size) {
        board = new GoBoard(size);
        currentPlayer = Player.BLACK;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public void changePlayer() {
        if (currentPlayer.equals(Player.WHITE)) {
            setCurrentPlayer(Player.BLACK);
        } else {
            setCurrentPlayer(Player.WHITE);
        }
    }

    public boolean placeStone(int x, int y) {
        boolean result = board.placeStone(x, y, currentPlayer);
        if (result)
            changePlayer();
        return result;
    }
}
