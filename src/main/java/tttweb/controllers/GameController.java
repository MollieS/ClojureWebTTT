package tttweb.controllers;

import httpserver.Request;
import httpserver.Response;
import httpserver.httprequests.RequestHeader;
import httpserver.httpresponse.HTTPResponse;
import httpserver.httpresponse.ResponseHeader;
import httpserver.resourcemanagement.HTMLResource;
import httpserver.routing.Route;
import httpserver.sessions.Session;
import tttweb.SessionManager;

import java.util.HashMap;

import static httpserver.httpresponse.StatusCode.OK;
import static httpserver.httpresponse.StatusCode.REDIRECT;
import static httpserver.routing.Method.POST;

public class GameController extends Route {

    private final SessionManager sessionManager;

    public GameController(SessionManager sessionManager) {
        super("/game", POST);
        this.sessionManager = sessionManager;
    }

    @Override
    public Response performAction(Request request) {
        if (invalidRequest(request)) {
            return redirect();
        }
        String sessionId = request.getValue(RequestHeader.COOKIE);
        if (!sessionManager.exists(sessionId)) {
            return redirect();
        }
        Session currentSession = sessionManager.getSession(sessionId);
        if (isInvalid(currentSession)) {
            return redirect();
        }
        return validRequest(sessionId);
    }

    private Response validRequest(String sessionId) {
        String gameType = sessionManager.getOrCreateSession(sessionId).getData().get("gameType");
        HTMLResource htmlResource = new HTMLResource(gameType.getBytes());
        return HTTPResponse.create(OK).withBody(htmlResource);
    }

    private boolean invalidRequest(Request request) {
        return !request.hasHeader(RequestHeader.COOKIE);
    }

    private boolean isInvalid(Session currentSession) {
        return !(currentSession.hasData("gameType") && currentSession.hasData("boardState"));
    }

    private Response redirect() {
        HashMap<ResponseHeader, byte[]> headers = new HashMap<>();
        headers.put(ResponseHeader.LOCATION, "/".getBytes());
        return HTTPResponse.create(REDIRECT).withHeaders(headers);
    }
}
