package tttweb.controllers;

import httpserver.Response;
import httpserver.httpresponse.ResponseHeader;
import httpserver.sessions.HTTPSession;
import httpserver.sessions.Session;
import httpserver.sessions.SessionFactory;
import org.junit.Test;

import java.nio.charset.Charset;

import static httpserver.routing.Method.POST;
import static org.junit.Assert.*;

public class NewGameControllerTest {

    private SessionFactorySpy sessionFactorySpy = new SessionFactorySpy();
    private NewGameController newGameController = new NewGameController(sessionFactorySpy);

    @Test
    public void sendsARedirectToGamePageForValidRequest() {
        RequestDouble requestDouble = new RequestDouble("/new-game", POST);
        requestDouble.addCookie("1");
        requestDouble.addData("hvh");

        Response response = newGameController.performAction(requestDouble);

        assertEquals(302, response.getStatusCode());
        assertEquals(response.getReasonPhrase(), "Found");
        assertTrue(response.hasHeader(ResponseHeader.LOCATION));
        assertEquals("/game", new String(response.getValue(ResponseHeader.LOCATION), Charset.defaultCharset()));
    }

    @Test
    public void createsASessionWithCorrectSessionId() {
        RequestDouble request = new RequestDouble("/new-game", POST);
        request.addCookie("1");
        request.addData("hvh");

        newGameController.performAction(request);

        assertNotNull(sessionFactorySpy.createdSession);
        assertEquals("1", sessionFactorySpy.createdSession.getId());
    }

    @Test
    public void ifRequestHasNoCookieItRedirectsToMenuPage() {
        RequestDouble request = new RequestDouble("/new-game", POST);
        request.addData("hvh");

        Response response = newGameController.performAction(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("Found", response.getReasonPhrase());
        assertTrue(response.hasHeader(ResponseHeader.LOCATION));
        assertEquals("/", new String(response.getValue(ResponseHeader.LOCATION), Charset.defaultCharset()));
    }

    @Test
    public void ifRequestHasNoDataItRedirectsToHomePage() {
        RequestDouble request = new RequestDouble("/new-game", POST);
        request.addCookie("1");

        Response response = newGameController.performAction(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("Found", response.getReasonPhrase());
        assertTrue(response.hasHeader(ResponseHeader.LOCATION));
        assertEquals("/", new String(response.getValue(ResponseHeader.LOCATION), Charset.defaultCharset()));
    }

    @Test
    public void doesNotCreateSessionIfSessionExists() {
        RequestDouble request = new RequestDouble("/game", POST);
        request.addCookie("1");
        request.addData("hvh");

        newGameController.performAction(request);
        newGameController.performAction(request);

        assertEquals(1, sessionFactorySpy.timesCalled);
    }

    @Test
    public void addsGameTypeToSession() {
        RequestDouble requestDouble = new RequestDouble("/game", POST);
        requestDouble.addData("hvh");
        requestDouble.addCookie("1");

        newGameController.performAction(requestDouble);

        assertTrue(sessionFactorySpy.createdSession.hasData("gameType"));
        assertEquals("hvh", sessionFactorySpy.createdSession.getData().get("gameType"));
    }

    @Test
    public void addsBoardStateToSession() {
        RequestDouble requestDouble = new RequestDouble("/game", POST);
        requestDouble.addData("hvh");
        requestDouble.addCookie("1");

        newGameController.performAction(requestDouble);

        assertTrue(sessionFactorySpy.createdSession.hasData("boardState"));
        assertEquals("---------", sessionFactorySpy.createdSession.getData().get("boardState"));

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
}
