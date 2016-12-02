package tttweb.controllers;

import httpserver.Response;
import httpserver.httpresponse.ResponseHeader;
import httpserver.sessions.HTTPSession;
import httpserver.sessions.Session;
import org.junit.Test;
import tttweb.SessionManager;

import java.nio.charset.Charset;

import static httpserver.routing.Method.GET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameControllerTest {

    private SessionManager sessionManager = new SessionManagerStub();
    private GameController gameController = new GameController(sessionManager);

    @Test
    public void redirectsToMenuPageIfRequestHasNoCookie() {
        RequestDouble requestDouble = new RequestDouble("/game", GET);

        Response response = gameController.performAction(requestDouble);

        assertEquals(302, response.getStatusCode());
        assertEquals("Found", response.getReasonPhrase());
        assertTrue(response.hasHeader(ResponseHeader.LOCATION));
        assertEquals("/", new String(response.getValue(ResponseHeader.LOCATION), Charset.defaultCharset()));
    }

    @Test
    public void redirectsToMenuPageIfRequestHasNoSessionAssociatedWithIt() {
        RequestDouble requestDouble = new RequestDouble("/game", GET);
        requestDouble.addCookie("3");

        Response response = gameController.performAction(requestDouble);

        assertEquals(302, response.getStatusCode());
        assertEquals("Found", response.getReasonPhrase());
        assertTrue(response.hasHeader(ResponseHeader.LOCATION));
        assertEquals("/", new String(response.getValue(ResponseHeader.LOCATION), Charset.defaultCharset()));
    }

    @Test
    public void redirectsToMenuPageIfRequestHasNoGameData() {
        RequestDouble requestDouble = new RequestDouble("/game", GET);
        requestDouble.addCookie("2");

        Response response = gameController.performAction(requestDouble);

        assertEquals(302, response.getStatusCode());
        assertEquals("Found", response.getReasonPhrase());
        assertTrue(response.hasHeader(ResponseHeader.LOCATION));
        assertEquals("/", new String(response.getValue(ResponseHeader.LOCATION), Charset.defaultCharset()));
    }

    @Test
    public void sendsA200ResponseWithBodyForAValidRequest() {
        sessionManager.getOrCreateSession("1");
        RequestDouble requestDouble = new RequestDouble("/game", GET);
        requestDouble.addCookie("1");

        Response reponse = gameController.performAction(requestDouble);

        assertEquals(200, reponse.getStatusCode());
        assertEquals("OK", reponse.getReasonPhrase());
        assertTrue(reponse.hasBody());
    }

    private class SessionManagerStub extends SessionManager {

        private HTTPSession validSession;
        private HTTPSession invalidSession;

        public SessionManagerStub() {
            super(new SessionFactorySpy());
            this.validSession = new HTTPSession("1");
            validSession.addData("gameType", "hvh");
            validSession.addData("boardState", "---------");
            this.invalidSession = new HTTPSession("2");
        }

        @Override
        public Session getOrCreateSession(String sessionId) {
            if (sessionId.equals("1")) {
                return validSession;
            }
            return invalidSession;
        }

        @Override
        public boolean exists(String sessionId) {
            return sessionId.equals("1") || sessionId.equals("2");
        }

        @Override
        public Session getSession(String sessionID) {
            return getOrCreateSession(sessionID);
        }
    }
}
