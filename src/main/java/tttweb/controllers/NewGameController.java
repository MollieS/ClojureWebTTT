package tttweb.controllers;

import httpserver.Request;
import httpserver.Response;
import httpserver.httpresponse.HTTPResponse;
import httpserver.httpresponse.ResponseHeader;
import httpserver.routing.Route;
import httpserver.sessions.Session;
import tttweb.SessionManager;

import java.util.HashMap;

import static httpserver.httprequests.RequestHeader.COOKIE;
import static httpserver.httprequests.RequestHeader.DATA;
import static httpserver.httpresponse.StatusCode.REDIRECT;
import static httpserver.routing.Method.POST;

public class NewGameController extends Route {

    private final SessionManager sessionManager;

    public NewGameController(SessionManager sessionManager) {
        super("/new-game", POST);
        this.sessionManager = sessionManager;
    }

    @Override
    public Response performAction(Request request) {
        if (requestIsInvalid(request)) {
            return redirect("/");
        }
        return createNewGame(request);
    }

    private Response createNewGame(Request request) {
        String sessionID = request.getValue(COOKIE);
        updateSession(sessionID, request);
        return redirect("/game");
    }

    private boolean requestIsInvalid(Request request) {
//        System.out.println(request.hasHeader(COOKIE));
//        System.out.println(request.hasHeader(DATA));
        return !request.hasHeader(COOKIE) || !request.hasHeader(DATA);
    }

    private void updateSession(String sessionID, Request request) {
        Session currentSession = sessionManager.getOrCreateSession(sessionID);
        addGameTypeToSession(currentSession, request);
        addBoardStateToSession(currentSession);
    }

    private void addBoardStateToSession(Session currentSession) {
        currentSession.addData("boardState", "---------");
    }

    private void addGameTypeToSession(Session currentSession, Request request) {
        String gameType = request.getValue(DATA);
        currentSession.addData("gameType", gameType);
    }

    private Response redirect(String url) {
        HashMap<ResponseHeader, byte[]> headers = new HashMap<>();
        headers.put(ResponseHeader.LOCATION, url.getBytes());
        return HTTPResponse.create(REDIRECT).withHeaders(headers);
    }
}
