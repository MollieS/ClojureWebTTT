package tttweb;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private static String MARK_ONE = "x";
    private static String MARK_TWO = "o";
    private final String gameType;
    private String[] board;
    private IFn getWinningPositions;

    public Game(String[] board, String gameType) {
        this.board = board;
        this.gameType = gameType;
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("tic-tac-toe.game.rules"));
        require.invoke(Clojure.read("tic-tac-toe.game.board"));
        this.getWinningPositions = Clojure.var("tic-tac-toe.game.board", "get-winning-positions");
    }

    public boolean isOver() {
        IFn isOver = Clojure.var("tic-tac-toe.game.rules", "is-over?");
        return (boolean) isOver.invoke(getWinningPositions.invoke(formatBoard()), MARK_ONE, MARK_TWO);
    }

    private List<String> formatBoard() {
        List<String> formattedBoard = new ArrayList<>();
        for (String cell : board) {
            if (cell.equals("-")) {
                formattedBoard.add(null);
            } else {
                formattedBoard.add(cell);
            }
        }
        return formattedBoard;
    }

    public String gameType() {
        return gameType;
    }

    public String[] getBoard() {
        return board;
    }

    public boolean isDraw() {
        IFn isDrawn = Clojure.var("tic-tac-toe.game.rules", "is-drawn?");
        return (boolean) isDrawn.invoke(getWinningPositions.invoke(formatBoard()), MARK_ONE, MARK_TWO);
    }

    public String winningSymbol() {
        IFn winningSymbol = Clojure.var("tic-tac-toe.game.rules", "get-winner");
        return (String) winningSymbol.invoke(getWinningPositions.invoke(board), MARK_ONE, MARK_TWO);
    }

    public String[] placeMark(int i) {
        board[i] = MARK_ONE;
        return board;
    }

}
