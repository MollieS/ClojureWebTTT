package tttweb.view;

public class GameView {

    public static String createView(GamePresenter gamePresenter) {
        String view = header(gamePresenter);
        view += body(gamePresenter);
        view += footer();
        return view;
    }

    private static String header(GamePresenter gamePresenter) {
        String view = htmlHeader();
        view += addRefresh(gamePresenter);
        view += css();
        view += htmlHeaderEnd();
        return view;
    }

    private static String htmlHeaderEnd() {
        return "<title>Tic Tac Toe</title>" +
                "</head>";
    }

    private static String htmlHeader() {
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">";
    }

    private static String addRefresh(GamePresenter gamePresenter) {
        if (gamePresenter.gameType().equals("cvc") && !gamePresenter.gameIsOver()) {
            return "<meta http-equiv=\"refresh\" content=\"1\">";
        }
        return "";
    }

    private static String body(GamePresenter gamePresenter) {
        String body = addBoard(gamePresenter);
        if (gamePresenter.gameIsOver()) {
            body += addResult(gamePresenter);
            body += addReplay();
        }
        return body;
    }

    private static String addReplay() {
        return "<div class=\"replay\">" +
                "<form method=\"get\" action=\"/\">" +
                "<button class=\"replay-button\" name=\"replay\" value=\"true\">Replay?</button>" +
                "</form>" +
                "</div>";
    }

    private static String addResult(GamePresenter gamePresenter) {
        return "<div class=\"result\">" +
                gamePresenter.getResult() +
                "<div>";
    }

    private static String addBoard(GamePresenter gamePresenter) {
        String body = "<body>" +
                "<div class=\"board\">" +
                "<form method=\"post\" action=\"/new-move\">";
        body += createBoard(gamePresenter, body);
        body += "</form>" +
                "</div>";
        return body;
    }

    private static String footer() {
        return "</body>" +
                "</html>";
    }

    private static String createButton(int i, GamePresenter gamePresenter) {
        String button = "<div class=\"cell\">" +
                "<button class=\"button\" placeholder=\"-\" name=\"data\" value=\"" + i + "\"";
        if (gamePresenter.gameIsOver()) {
            button += " disabled";
        }
        button += ">" + gamePresenter.getMark(i) + "</button>\n" +
                "</div>";

        if ((i + 1) % 3 == 0) {
            button += "</br>";
        }
        return button;
    }

    private static String createBoard(GamePresenter gamePresenter, String view) {
        for (int i = 0; i < 9; i++) {
            view += createButton(i, gamePresenter);
        }
        return view;
    }

    private static String css() {
        String result = "<style media=\"screen\" type=\"text/css\">";
        result += buttonCSS();
        result += boardCSS();
        result += cellCSS();
        result += replayCSS();
        result += replayButtonCSS();
        result += resultCSS();
        result += "</style>";
        return result;
    }

    private static String resultCSS() {
        return ".result {" +
                    "font-size: 24px;" +
                    "margin: auto;" +
                    "width: 50%;" +
                    "padding: 10px;" +
                    "}";
    }

    private static String replayButtonCSS() {
        return ".replay-button {" +
                    "padding: 10px 10px;" +
                    "font-size: 18px;" +
                    "}";
    }

    private static String replayCSS() {
        return ".replay {" +
                    "margin: auto;" +
                    "width: 50%;" +
                    "font-size: 18px;" +
                    "padding: 10px;" +
                    "}";
    }

    private static String cellCSS() {
        return ".cell {" +
                    "border: black 10px; " +
                    "display: inline-block; " +
                    "padding: 10px; " +
                    "}";
    }

    private static String boardCSS() {
        return ".board {" +
                    "margin: auto; " +
                    "width: 50%; " +
                    "padding: 30px 30px;" +
                    "}";
    }

    private static String buttonCSS() {
        return ".button {" +
                    "padding: 32px 36px;" +
                    "border: none;" +
                    "font-size: 24px;" +
                    "}";
    }
}
