import org.junit.Test;
import tttweb.Game;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void knowsIfTheGameIsOver() {
        String[] board = {"x", "x", "x", "-", "-", "-", "o", "o", "o"};

        Game game = new Game(board, "hvh");

        assertTrue(game.isOver());
    }

    @Test
    public void knowsIfTheGameIsDrawn() {
        String[] board = {"x", "x", "o", "o", "o", "x", "x", "x", "o"};

        Game game = new Game(board, "hvh");

        assertTrue(game.isOver());
    }

    @Test
    public void knowsIfTheGameIsNotDrawn() {
        String[] board = {"-", "-", "-", "-", "-", "-", "-", "-", "-"};

        Game game = new Game(board, "hvh");

        assertFalse(game.isOver());
    }

    @Test
    public void knowsTheWinningSymbol() {
        String[] board = {"x", "x", "x", "-", "-", "-", "o", "o", "o"};

        Game game = new Game(board, "hvh");

        assertEquals("x", game.winningSymbol());
    }

    @Test
    public void canPlaceAMark() {
        String[] board = {"-", "-", "-", "-", "-", "-", "-", "-", "-"};

        Game game = new Game(board, "hvh");
        String[] markedBoard = game.placeMark(0);

        assertEquals("x", markedBoard[0]);
    }
}
