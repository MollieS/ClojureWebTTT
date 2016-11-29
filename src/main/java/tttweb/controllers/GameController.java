package tttweb.controllers;

import httpserver.Request;
import httpserver.Response;
import httpserver.httprequests.RequestHeader;
import httpserver.httpresponse.HTTPResponse;
import httpserver.httpresponse.ResponseHeader;
import httpserver.routing.Route;
import httpserver.sessions.Session;
import httpserver.sessions.SessionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static httpserver.httpresponse.StatusCode.OK;
import static httpserver.httpresponse.StatusCode.REDIRECT;
import static httpserver.routing.Method.POST;

public class GameController extends Route {

    private final SessionFactory sessionFactory;
    private List<Session> sessions = new ArrayList<>();

    public GameController(SessionFactory sessionFactory) {
        super("/game", POST);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Response performAction(Request request) {
        if (!request.hasHeader(RequestHeader.COOKIE)) {
            return redirect();
        }
        String sessionID = request.getValue(RequestHeader.COOKIE);
        if (isNewSession(sessionID)) {
            sessions.add(sessionFactory.createSession(sessionID));
        }
        return HTTPResponse.create(OK);
    }

    private Response redirect() {
        HashMap<ResponseHeader, byte[]> headers = new HashMap<>();
        headers.put(ResponseHeader.LOCATION, "/".getBytes());
        return HTTPResponse.create(REDIRECT).withHeaders(headers);
    }

    public boolean isNewSession(String sessionID) {
        for (Session session : sessions) {
            if (session.getId().equals(sessionID)) {
                return false;
            }
        }
        return true;
    }
}
