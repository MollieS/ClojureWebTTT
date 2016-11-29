package tttweb.controllers;

import httpserver.Request;
import httpserver.Response;
import org.junit.Test;
import tttweb.SessionExpirationDateGenerator;
import tttweb.SessionTokenGenerator;

import java.nio.charset.Charset;

import static httpserver.httpresponse.ResponseHeader.COOKIE;
import static httpserver.routing.Method.GET;
import static httpserver.routing.Method.POST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MenuControllerTest {

    private MenuController menuController = new MenuController(new SessionTokenGeneratorStub(), new SessionExpirationDateGenerator());

    @Test
    public void sendsA200Response() {
        Request httpRequest = new RequestDouble("/", GET);

        Response response = menuController.performAction(httpRequest);

        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getReasonPhrase());
    }

    @Test
    public void sendsA405IfMethodNotAllowed() {
        Request httpRequest = new RequestDouble("/", POST);

        Response response = menuController.performAction(httpRequest);

        assertEquals(405, response.getStatusCode());
        assertEquals("Method Not Allowed", response.getReasonPhrase());
    }

    @Test
    public void returnsASetCookieHeaderResponse() {
        Request httpRequest = new RequestDouble("/", GET);

        Response response = menuController.performAction(httpRequest);

        assertTrue(response.hasHeader(COOKIE));
    }

    @Test
    public void addsASessionTokenAndExpiryDateForCookie() {
        Request httpRequest = new RequestDouble("/", GET);
        Response response = menuController.performAction(httpRequest);

        String cookie = new String(response.getValue(COOKIE), Charset.defaultCharset());

        assertTrue(cookie.contains("sessionToken=1"));
        assertTrue(cookie.contains("Expires="));
    }

    @Test
    public void addsBodyToResponse() {
        Request httpRequest = new RequestDouble("/", GET);

        Response response = menuController.performAction(httpRequest);

        assertTrue(response.hasBody());
    }

    private class SessionTokenGeneratorStub extends SessionTokenGenerator {

        @Override
        public int generateToken() {
            return 1;
        }
    }

}
