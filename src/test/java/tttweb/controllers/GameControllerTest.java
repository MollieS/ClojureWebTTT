package tttweb.controllers;

import httpserver.Response;
import httpserver.httpresponse.ResponseHeader;
import httpserver.sessions.Session;
import httpserver.sessions.SessionManager;
import org.junit.Test;
import tttweb.doubles.RequestDouble;
import tttweb.doubles.SessionManagerStub;

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

    @Test
    public void playsAComputerMoveIfComputerIsFirst() {
        RequestDouble requestDouble = new RequestDouble("/game", GET);
        requestDouble.addCookie("1");
        Session session = sessionManager.getSession("1");
        session.addData("gameType", "cvh");
        session.addData("boardState", "---------");

        Response response = gameController.performAction(requestDouble);

        assertEquals(200, response.getStatusCode());
        assertEquals("x--------", session.getData().get("boardState"));
    }
}
