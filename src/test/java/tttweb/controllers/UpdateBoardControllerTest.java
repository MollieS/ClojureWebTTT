package tttweb.controllers;

import httpserver.Response;
import httpserver.sessions.HTTPSession;
import httpserver.sessions.HTTPSessionFactory;
import httpserver.sessions.Session;
import org.junit.Before;
import org.junit.Test;
import tttweb.doubles.RequestDouble;
import tttweb.SessionManager;

import java.nio.charset.Charset;

import static httpserver.httpresponse.ResponseHeader.LOCATION;
import static httpserver.routing.Method.POST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpdateBoardControllerTest {

    private RequestDouble requestDouble = new RequestDouble("/update-board", POST);
    private Session session = new HTTPSession("1");
    private SessionManager sessionManager = new SessionManagerStub(session);
    private UpdateBoardController updateBoardController = new UpdateBoardController(sessionManager);

    @Before
    public void setUp() {
        requestDouble.addCookie("1");
        session.addData("gameType", "hvh");
    }

    @Test
    public void updatesBoardWhenDataIsPresent() {
        requestDouble.addData("0");
        session.addData("boardState", "---------");

        updateBoardController.performAction(requestDouble);

        String boardState = session.getData().get("boardState");

        assertEquals("x--------", boardState);
    }

    @Test
    public void updatesBoardWithCorrectMark() {
        requestDouble.addData("4");
        session.addData("boardState", "x--------");

        updateBoardController.performAction(requestDouble);

        String boardState = session.getData().get("boardState");

        assertEquals("x---o----", boardState);
    }

    @Test
    public void returnsARedirectResponse() {
        requestDouble.addData("4");
        session.addData("boardState", "---------");

        Response response = updateBoardController.performAction(requestDouble);

        assertEquals("Found", response.getReasonPhrase());
        assertEquals(302, response.getStatusCode());
        assertTrue(response.hasHeader(LOCATION));
        assertEquals("/game", new String(response.getValue(LOCATION), Charset.defaultCharset()));
    }

    @Test
    public void redirectIfRequestHasNoMoveData() {
        session.addData("boardState", "---------");

        Response response = updateBoardController.performAction(requestDouble);

        assertEquals("Found", response.getReasonPhrase());
        assertEquals(302, response.getStatusCode());
        assertTrue(response.hasHeader(LOCATION));
        assertEquals("/game", new String(response.getValue(LOCATION), Charset.defaultCharset()));
    }

    @Test
    public void redirectToMenuIfRequestHasNoCookieData() {
        RequestDouble requestDouble = new RequestDouble("/update-board", POST);
        requestDouble.addData("4");

        Response response = updateBoardController.performAction(requestDouble);

        assertEquals("Found", response.getReasonPhrase());
        assertEquals(302, response.getStatusCode());
        assertTrue(response.hasHeader(LOCATION));
        assertEquals("/", new String(response.getValue(LOCATION), Charset.defaultCharset()));
    }

    @Test
    public void redirectIfSessionDataIsInvalid() {
        RequestDouble requestDouble = new RequestDouble("/update-board", POST);
        requestDouble.addCookie("2");
        requestDouble.addData("2");

        Response response = updateBoardController.performAction(requestDouble);

        assertEquals("Found", response.getReasonPhrase());
        assertEquals(302, response.getStatusCode());
        assertTrue(response.hasHeader(LOCATION));
        assertEquals("/game", new String(response.getValue(LOCATION), Charset.defaultCharset()));
    }

    private class SessionManagerStub extends SessionManager {

        private final Session session;

        public SessionManagerStub(Session session) {
            super(new HTTPSessionFactory());
            this.session = session;
        }

        @Override
        public Session getSession(String sessionId) {
            return session;
        }

        @Override
        public boolean exists(String sessionId) {
            return sessionId.equals("1");
        }
    }
}
