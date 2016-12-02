package tttweb.controllers;

import httpserver.Request;
import httpserver.Response;
import httpserver.httpresponse.HTTPResponse;
import httpserver.httpresponse.ResponseHeader;
import httpserver.routing.Route;
import httpserver.sessions.Session;
import httpserver.sessions.SessionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static httpserver.httprequests.RequestHeader.COOKIE;
import static httpserver.httprequests.RequestHeader.DATA;
import static httpserver.httpresponse.StatusCode.REDIRECT;
import static httpserver.routing.Method.POST;

public class NewGameController extends Route {

    private final SessionFactory sessionFactory;
    private List<Session> sessions = new ArrayList<>();

    public NewGameController(SessionFactory sessionFactory) {
        super("/new-game", POST);
        this.sessionFactory = sessionFactory;
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
        if (isNewSession(sessionID)) {
            sessions.add(sessionFactory.createSession(sessionID));
        }
        updateSession(sessionID, request);
        return redirect("/game");
    }

    private boolean requestIsInvalid(Request request) {
        return !request.hasHeader(COOKIE) || !request.hasHeader(DATA);
    }

    private void updateSession(String sessionID, Request request) {
        Session currentSession = getCurrentSession(sessionID);
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

    private Session getCurrentSession(String sessionID) {
        for (Session session : sessions) {
            if (session.getId().equals(sessionID)) {
                return session;
            }
        }
        return null;
    }

    private boolean isNewSession(String sessionID) {
        return getCurrentSession(sessionID) == null;
    }

    private Response redirect(String url) {
        HashMap<ResponseHeader, byte[]> headers = new HashMap<>();
        headers.put(ResponseHeader.LOCATION, url.getBytes());
        return HTTPResponse.create(REDIRECT).withHeaders(headers);
    }

}
