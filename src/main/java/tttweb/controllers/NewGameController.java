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
        if (!request.hasHeader(COOKIE) || !request.hasHeader(DATA)) {
            return redirect("/");
        }
        String sessionID = request.getValue(COOKIE);
        if (isNewSession(sessionID)) {
            sessions.add(sessionFactory.createSession(sessionID));
        }
        return redirect("/game");
    }

    private Response redirect(String url) {
        HashMap<ResponseHeader, byte[]> headers = new HashMap<>();
        headers.put(ResponseHeader.LOCATION, url.getBytes());
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
