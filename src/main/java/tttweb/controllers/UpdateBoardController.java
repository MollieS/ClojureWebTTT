package tttweb.controllers;

import httpserver.Request;
import httpserver.Response;
import httpserver.httprequests.RequestHeader;
import httpserver.httpresponse.HTTPResponse;
import httpserver.httpresponse.ResponseHeader;
import httpserver.httpresponse.StatusCode;
import httpserver.routing.Route;
import httpserver.sessions.Session;
import httpserver.sessions.SessionManager;
import tttweb.Game;

import java.util.HashMap;

import static httpserver.httprequests.RequestHeader.COOKIE;
import static httpserver.httprequests.RequestHeader.DATA;
import static httpserver.routing.Method.POST;

public class UpdateBoardController extends Route {

    private final SessionManager sessionManager;

    public UpdateBoardController(SessionManager sessionManager) {
        super("/update-board", POST);
        this.sessionManager = sessionManager;
    }

    @Override
    public Response performAction(Request request) {
        String sessionId;
        if (request.hasHeader(COOKIE)) {
            sessionId = request.getValue(RequestHeader.COOKIE);
        } else {
            return redirect("/");
        }
        return responseForValidRequest(request, sessionId);
    }

    private Response responseForValidRequest(Request request, String sessionId) {
        if (isValid(request, sessionId)) {
            Integer move = Integer.valueOf(request.getValue(DATA));
            Session currentSession = getCurrentSession(sessionId);
            Game game = getGame(currentSession);
            String updatedBoard = convertBoardToString(game.placeMark(move));
            currentSession.addData("boardState", updatedBoard);
        }
        return redirect("/game");
    }

    private boolean isValid(Request request, String sessionId) {
        return request.hasHeader(DATA) && sessionManager.exists(sessionId);
    }

    private Game getGame(Session currentSession) {
        String gameType = currentSession.getData().get("gameType");
        String[] boardState = getBoardAsArray(currentSession);
        return new Game(boardState, gameType);
    }

    private Session getCurrentSession(String sessionId) {
        return sessionManager.getSession(sessionId);
    }

    private Response redirect(String location) {
        HashMap<ResponseHeader, byte[]> headers = new HashMap<>();
        headers.put(ResponseHeader.LOCATION, location.getBytes());
        return HTTPResponse.create(StatusCode.REDIRECT).withHeaders(headers);
    }

    public String[] getBoardAsArray(Session currentSession) {
        return currentSession.getData().get("boardState").split("");
    }

    public String convertBoardToString(String[] board) {
        String stringBoard = "";
        for (String cell : board) {
            stringBoard += cell;
        }
        return stringBoard;
    }
}
