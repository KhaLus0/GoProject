package GoGame;

public class GoBoard {
    private FieldState[][] board;

    public GoBoard(int size) {
        board = new FieldState[size+2][size+2];
        init(board);
    }

    private void init(FieldState[][] board) {
        int size = board[0].length;
        //set borders to FieldState.BORDER
        for (int j = 0; j < size; j++) {
            board[0][j] = FieldState.BORDER;
            board[size-1][j] = FieldState.BORDER;
        }
        for (int i = 0; i < size; i++) {
            board[i][0] = FieldState.BORDER;
            board[i][size-1] = FieldState.BORDER;
        }
        //set the inside of board to FieldState.FREE
        for (int i = 1; i < size - 1; i++) {
            for (int j = 1; j < size - 1; j++) {
                board[i][j] = FieldState.FREE;
            }
        }

    }

    public FieldState[][] getBoard() {
        return board;
    }
}
