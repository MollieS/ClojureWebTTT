package tttweb.controllers;

import httpserver.Response;
import httpserver.httpresponse.ResponseHeader;
import httpserver.sessions.SessionManager;
import org.junit.Test;
import tttweb.doubles.RequestDouble;
import tttweb.doubles.SessionFactorySpy;

import java.nio.charset.Charset;

import static httpserver.routing.Method.POST;
import static org.junit.Assert.*;

public class NewGameControllerTest {

    private SessionFactorySpy sessionFactorySpy = new SessionFactorySpy();
    private NewGameController newGameController = new NewGameController(new SessionManager(sessionFactorySpy));

    @Test
    public void sendsARedirectToGamePageForValidRequest() {
        RequestDouble requestDouble = getRequestDouble("1", "hvh");

        Response response = newGameController.performAction(requestDouble);

        assertEquals(302, response.getStatusCode());
        assertEquals(response.getReasonPhrase(), "Found");
        assertTrue(response.hasHeader(ResponseHeader.LOCATION));
        assertEquals("/game", new String(response.getValue(ResponseHeader.LOCATION), Charset.defaultCharset()));
    }


    @Test
    public void createsASessionWithCorrectSessionId() {
        RequestDouble request = getRequestDouble("1", "hvh");

        newGameController.performAction(request);

        assertNotNull(sessionFactorySpy.createdSession);
        assertEquals("1", sessionFactorySpy.createdSession.getId());
    }

    @Test
    public void ifRequestHasNoCookieItRedirectsToMenuPage() {
        RequestDouble request = getRequestDouble(null, "hvh");

        Response response = newGameController.performAction(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("Found", response.getReasonPhrase());
        assertTrue(response.hasHeader(ResponseHeader.LOCATION));
        assertEquals("/", new String(response.getValue(ResponseHeader.LOCATION), Charset.defaultCharset()));
    }

    @Test
    public void ifRequestHasNoDataItRedirectsToHomePage() {
        RequestDouble request = getRequestDouble("1", null);

        Response response = newGameController.performAction(request);

        assertEquals(302, response.getStatusCode());
        assertEquals("Found", response.getReasonPhrase());
        assertTrue(response.hasHeader(ResponseHeader.LOCATION));
        assertEquals("/", new String(response.getValue(ResponseHeader.LOCATION), Charset.defaultCharset()));
    }

    @Test
    public void doesNotCreateSessionIfSessionExists() {
        RequestDouble request = getRequestDouble("1", "hvh");

        newGameController.performAction(request);
        newGameController.performAction(request);

        assertEquals(1, sessionFactorySpy.timesCalled);
    }

    @Test
    public void addsGameTypeToSession() {
        RequestDouble requestDouble = getRequestDouble("1", "hvh");

        newGameController.performAction(requestDouble);

        assertTrue(sessionFactorySpy.createdSession.hasData("gameType"));
        assertEquals("hvh", sessionFactorySpy.createdSession.getData().get("gameType"));
    }

    @Test
    public void addsBoardStateToSession() {
        RequestDouble requestDouble = getRequestDouble("1", "hvh");

        newGameController.performAction(requestDouble);

        assertTrue(sessionFactorySpy.createdSession.hasData("boardState"));
        assertEquals("---------", sessionFactorySpy.createdSession.getData().get("boardState"));

    }

    private RequestDouble getRequestDouble(String sessionToken, String gamType) {
        RequestDouble requestDouble = new RequestDouble("/new-game", POST);
        requestDouble.addCookie(sessionToken);
        requestDouble.addData(gamType);
        return requestDouble;
    }

}
