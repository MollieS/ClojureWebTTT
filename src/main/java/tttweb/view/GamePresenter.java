package tttweb.view;

import tttweb.Game;

public class GamePresenter {

    private final Game game;

    public GamePresenter(Game game) {
        this.game = game;
    }

    public boolean gameIsOver() {
        return game.isOver();
    }

    public String gameType() {
        return game.gameType();
    }

    public String getMark(int i) {
        String[] boardArray = game.getBoard();
        return boardArray[i].toLowerCase();
    }

    public String getResult() {
        if (game.isOver() && !game.isDraw()) {
            return (game.winningSymbol() + " wins!");
        } else {
            return "It's a draw!";
        }
    }
}
