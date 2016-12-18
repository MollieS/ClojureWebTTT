package tttweb;

import org.junit.Test;
import tttweb.view.GamePresenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GamePresenterTest {

    @Test
    public void knowsWhenTHeGameIsOver() {
        GameDouble gameDouble = new GameDouble();

        GamePresenter gamePresenter = new GamePresenter(gameDouble);
        gamePresenter.gameIsOver();

        assertTrue(gameDouble.isOverWasCalled);
    }

    @Test
    public void returnsTheCorrectResultForADraw() {
        String[] board = new String[]{"x", "x", "o", "o", "o", "x", "x", "x", "o"};
        Game game = new Game(board, "hvh");

        GamePresenter gamePresenter = new GamePresenter(game);

        assertEquals("It's a draw!", gamePresenter.getResult());
    }

    @Test
    public void returnsTheCorrectResultForAWin() {
        String[] board = new String[]{"x", "x", "x", "-", "-", "-", "o", "-", "o"};
        Game game = new Game(board, "hvh");

        GamePresenter gamePresenter = new GamePresenter(game);

        assertEquals("X wins!", gamePresenter.getResult());
    }

    private class GameDouble extends Game {

        public boolean isOverWasCalled;

        public GameDouble() {
            super(new String[]{"-", "-", "-"}, "hvh");
        }

        @Override
        public boolean isOver() {
            isOverWasCalled = true;
            return true;
        }
    }
}
