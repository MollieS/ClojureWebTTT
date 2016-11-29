package tttweb.controllers;

import httpserver.Request;
import org.junit.Test;

import static httpserver.routing.Method.POST;

public class NewGameControllerTest {

    @Test
    public void sendsARedirectResponse() {
        NewGameController newGameController = new NewGameController();
        Request request = new RequestDouble("/game", POST);

    }
}
