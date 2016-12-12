package tttweb;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {

    private static String MARK_ONE = "X";
    private static String MARK_TWO = "O";
    private final String gameType;
    private String[] board;
    private IFn getWinningPositions;

    public Game(String[] board, String gameType) {
        this.board = board;
        this.gameType = gameType;
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("tic-tac-toe.game.rules"));
        require.invoke(Clojure.read("tic-tac-toe.game.board"));
        require.invoke(Clojure.read("tic-tac-toe.game.marks"));
        require.invoke(Clojure.read("tic-tac-toe.players.unbeatable-player"));
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
                formattedBoard.add(cell.toUpperCase());
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
        return (String) winningSymbol.invoke(getWinningPositions.invoke(formatBoard()), MARK_ONE, MARK_TWO);
    }

    public String[] placeMark(int i) {
        if (board[i].equals("-")) {
            board[i] = getCurrentMark(board);
        }
        playComputerMove();
        return board;
    }

    private void playComputerMove() {
        if (gameType.contains("c") && !isOver()) {
            IFn getComputerMove = Clojure.var("tic-tac-toe.players.unbeatable-player", "get-computer-move");
            String[] marks = new String[]{"X", "O"};
            Long computerMove = (long) getComputerMove.invoke(formatBoard(), marks);
            board[computerMove.intValue()] = getCurrentMark(board);
        }
    }

    public String getCurrentMark(String[] board) {
        IFn getCurrentMark = Clojure.var("tic-tac-toe.game.marks", "get-current-mark");
        String currentMark = (String) getCurrentMark.invoke(isPlayerOne(board));
        return currentMark.toLowerCase();
    }

    private boolean isPlayerOne(String[] board) {
        return countMarks(board) % 2 == 0;
    }

    private int countMarks(String[] board) {
        return (int) Arrays.stream(board).filter(cell -> !cell.equals("-")).count();
    }
}
