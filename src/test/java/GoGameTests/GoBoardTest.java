package GoGameTests;


import GoGame.*;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import GoGame.FieldState;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;


public class GoBoardTest {

    @Test
    public void libertiesTest() {
        GoGame game = new GoGame();
        game.board.getBoard()[4][5] = FieldState.WHITE;
        game.board.getBoard()[5][4] = FieldState.WHITE;
        game.board.getBoard()[6][5] = FieldState.WHITE;
        game.board.getBoard()[5][5] = FieldState.BLACK;
        game.board.getBoard()[5][6] = FieldState.BLACK;
        assertEquals(0, game.board.getNumOfLiberties(5, 5));
        game.board.getBoard()[5][6] = FieldState.FREE;
        assertEquals(1, game.board.getNumOfLiberties(5, 5));
        game.board.getBoard()[6][5] = FieldState.FREE;
        assertEquals(2, game.board.getNumOfLiberties(5, 5));
        game.board.getBoard()[5][4] = FieldState.FREE;
        assertEquals(3, game.board.getNumOfLiberties(5, 5));
        game.board.getBoard()[4][5] = FieldState.FREE;
        assertEquals(4, game.board.getNumOfLiberties(5, 5));
        game.board.getBoard()[9][9] = FieldState.WHITE;
        assertEquals(2, game.board.getNumOfLiberties(9, 9));
    }

    @Test
    public void getChainOfStonesTest() {
        GoGame game = new GoGame();
        game.board.getBoard()[2][1] = FieldState.WHITE;
        game.board.getBoard()[2][2] = FieldState.WHITE;
        game.board.getBoard()[2][3] = FieldState.WHITE;
        game.board.getBoard()[2][4] = FieldState.WHITE;
        game.board.getBoard()[2][5] = FieldState.WHITE;
        game.board.getBoard()[1][3] = FieldState.WHITE;
        game.board.getBoard()[3][2] = FieldState.WHITE;
        game.board.getBoard()[3][3] = FieldState.WHITE;
        game.board.getBoard()[3][5] = FieldState.WHITE;
        HashSet<Point> mySet = game.board.getChainOfStones(2, 3, new HashSet<Point>());
        assertTrue(mySet.contains(new Point(2,1)));
        assertTrue(mySet.contains(new Point(2,2)));
        assertTrue(mySet.contains(new Point(2,3)));
        assertTrue(mySet.contains(new Point(2,4)));
        assertTrue(mySet.contains(new Point(2,5)));
        assertTrue(mySet.contains(new Point(1,3)));
        assertTrue(mySet.contains(new Point(3,2)));
        assertTrue(mySet.contains(new Point(3,3)));
        assertTrue(mySet.contains(new Point(3,5)));
        assertFalse(mySet.contains(new Point(1,1)));
        assertEquals(9, mySet.size());
    }

    @Test
    public void isPartOfAliveChainTest() {
        GoGame game = new GoGame();
        game.board.getBoard()[1][1] = FieldState.WHITE;
        game.board.getBoard()[2][1] = FieldState.WHITE;
        game.board.getBoard()[3][1] = FieldState.WHITE;
        game.board.getBoard()[3][2] = FieldState.WHITE;
        game.board.getBoard()[3][3] = FieldState.WHITE;
        game.board.getBoard()[2][4] = FieldState.WHITE;
        game.board.getBoard()[1][5] = FieldState.WHITE;
        game.board.getBoard()[2][2] = FieldState.BLACK;
        game.board.getBoard()[1][3] = FieldState.BLACK;
        game.board.getBoard()[2][3] = FieldState.BLACK;
        game.board.printBoard();
        assertTrue(game.board.isPartOfAliveChain(1,4, Player.BLACK));
        game.board.getBoard()[1][2] = FieldState.BLACK;
        game.board.printBoard();
        assertFalse(game.board.isPartOfAliveChain(1,4, Player.BLACK));
    }

    @Test
    public void isChainAliveTest() {
        GoGame game = new GoGame();
        HashSet<Point> stones = new HashSet<Point>();
        game.board.getBoard()[1][1] = FieldState.WHITE;
        game.board.getBoard()[1][2] = FieldState.WHITE;
        game.board.getBoard()[1][3] = FieldState.WHITE;
        game.board.getBoard()[1][4] = FieldState.WHITE;
        game.board.getBoard()[2][5] = FieldState.WHITE;
        game.board.getBoard()[3][1] = FieldState.WHITE;
        game.board.getBoard()[3][2] = FieldState.WHITE;
        game.board.getBoard()[3][3] = FieldState.WHITE;
        game.board.getBoard()[3][4] = FieldState.WHITE;
        game.board.getBoard()[2][2] = FieldState.BLACK;
        stones.add(new Point(2,2));
        game.board.getBoard()[2][3] = FieldState.BLACK;
        stones.add(new Point(2,3));
        game.board.getBoard()[2][4] = FieldState.BLACK;
        stones.add(new Point(2,4));
        assertTrue(game.board.isChainAlive(stones));
        game.board.getBoard()[2][1] = FieldState.WHITE;
        assertFalse(game.board.isChainAlive(stones));
    }

    @Test
    public void removeChainOfStonesTest() {
        GoGame game = new GoGame();
        game.board.getBoard()[1][3] = FieldState.BLACK;
        game.board.getBoard()[1][4] = FieldState.BLACK;
        game.board.getBoard()[2][2] = FieldState.BLACK;
        game.board.getBoard()[3][1] = FieldState.BLACK;
        game.board.getBoard()[4][2] = FieldState.BLACK;
        game.board.getBoard()[4][3] = FieldState.BLACK;
        game.board.getBoard()[3][4] = FieldState.BLACK;
        game.board.getBoard()[1][5] = FieldState.WHITE;
        game.board.getBoard()[2][3] = FieldState.WHITE;
        game.board.getBoard()[2][4] = FieldState.WHITE;
        game.board.getBoard()[3][2] = FieldState.WHITE;
        game.board.getBoard()[3][3] = FieldState.WHITE;
        game.board.getBoard()[2][6] = FieldState.WHITE;
        game.board.getBoard()[3][5] = FieldState.WHITE;
        game.board.printBoard();
        game.placeStone(2,5);
        game.board.printBoard();
        assertEquals(game.board.getBoard()[2][5], FieldState.BLACK);
        assertEquals(game.board.getBoard()[2][4], FieldState.FREE);
        assertEquals(game.board.getBoard()[2][3], FieldState.FREE);
        assertEquals(game.board.getBoard()[3][2], FieldState.FREE);
        assertEquals(game.board.getBoard()[3][3], FieldState.FREE);
    }

    @Test
    public void suicideMoveTest() {
        GoGame game = new GoGame();
        game.board.getBoard()[1][2] = FieldState.WHITE;
        game.board.getBoard()[2][1] = FieldState.WHITE;
        game.board.getBoard()[2][3] = FieldState.WHITE;
        game.board.getBoard()[3][2] = FieldState.WHITE;
        game.board.printBoard();
        assertFalse(game.placeStone(2,2));
        game.board.printBoard();
        assertEquals(game.getCurrentPlayer(), Player.BLACK);
    }

    @Test
    public void changePlayerTest() {
        GoGame game = new GoGame();
        assertEquals(game.getCurrentPlayer(), Player.BLACK);
        game.placeStone(5,5);
        assertEquals(game.getCurrentPlayer(), Player.WHITE);
        assertNotEquals(game.board.getBoard()[5][5], FieldState.FREE);
        game.placeStone(5, 5);
        assertEquals(game.getCurrentPlayer(), Player.WHITE);
    }

}
