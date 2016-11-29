package tttweb.controllers;

import httpserver.Response;
import httpserver.httpresponse.ResponseHeader;
import httpserver.sessions.HTTPSession;
import httpserver.sessions.Session;
import httpserver.sessions.SessionFactory;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.HashMap;

import static httpserver.routing.Method.POST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GameControllerTest {

    private SessionFactorySpy sessionFactorySpy = new SessionFactorySpy();
    private GameController gameController = new GameController(sessionFactorySpy);

    @Test
    public void sendsA200ResponseForAPOSTRequest() {
        RequestDouble request = new RequestDouble("/game", POST);
        request.addCookie("1");

        Response response = gameController.performAction(request);

        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getReasonPhrase());
    }

    @Test
    public void createsASessionWithCorrectSessionId() {
        RequestDouble request = new RequestDouble("/game", POST);
        request.addCookie("1");

        gameController.performAction(request);

        assertNotNull(sessionFactorySpy.createdSession);
        assertEquals("1", sessionFactorySpy.createdSession.getId());
    }

    @Test
    public void ifRequestHasNoCookieItRedirectsToMenuPage() {
        RequestDouble request = new RequestDouble("/game", POST);

        Response response = gameController.performAction(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("Found", response.getReasonPhrase());
        assertTrue(response.hasHeader(ResponseHeader.LOCATION));
        assertEquals("/", new String(response.getValue(ResponseHeader.LOCATION), Charset.defaultCharset()));
    }

    @Test
    public void doesNotCreateSessionIfSessionExists() {
        RequestDouble request = new RequestDouble("/game", POST);
        request.addCookie("1");

        gameController.performAction(request);
        gameController.performAction(request);

        assertEquals(1, sessionFactorySpy.timesCalled);
    }

    private class SessionFactorySpy implements SessionFactory {

        public HTTPSession createdSession;
        public int timesCalled = 0;

        @Override
        public Session createSession(String id) {
            timesCalled++;
            createdSession = new HTTPSession(id);
            return createdSession;
        }
    }

    private class SessionStub implements Session {

        private final String id;

        public SessionStub(String id) {
            this.id = id;
        }

        @Override
        public HashMap<String, String> getData() {
            return null;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void addData(String s, String s1) {

        }

        @Override
        public boolean hasData(String s) {
            return false;
        }
    }
}
