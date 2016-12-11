package tttweb.controllers;

import httpserver.Response;
import org.junit.Test;

import static httpserver.routing.Method.GET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameControllerTest {

    @Test
    public void sendsA200ResponseWithBody() {
        GameController gameController = new GameController();
        RequestDouble requestDouble = new RequestDouble("/game", GET);

        Response reponse = gameController.performAction(requestDouble);

        assertEquals(200, reponse.getStatusCode());
        assertEquals("OK", reponse.getReasonPhrase());
        assertTrue(reponse.hasBody());
    }
}
