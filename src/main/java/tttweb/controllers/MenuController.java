package tttweb.controllers;

import httpserver.Request;
import httpserver.Response;
import httpserver.httpresponse.HTTPResponse;
import httpserver.httpresponse.ResponseHeader;
import httpserver.resourcemanagement.HTMLResource;
import httpserver.routing.Route;
import httpserver.sessions.SessionExpirationDateGenerator;
import httpserver.sessions.SessionTokenGenerator;

import java.time.ZonedDateTime;
import java.util.HashMap;

import static httpserver.httpresponse.StatusCode.OK;
import static httpserver.routing.Method.GET;
import static httpserver.routing.Method.HEAD;

public class MenuController extends Route {

    private final SessionTokenGenerator sessionTokenGenerator;
    private final SessionExpirationDateGenerator sessionExpiryDateGenerator;

    public MenuController(SessionTokenGenerator sessionTokenGenerator, SessionExpirationDateGenerator sessionExpirationDateGenerator) {
        super("/", GET, HEAD);
        this.sessionTokenGenerator = sessionTokenGenerator;
        this.sessionExpiryDateGenerator = sessionExpirationDateGenerator;
    }

    public Response performAction(Request request) {
        if (methodIsAllowed(request.getMethod())) {
            HTMLResource htmlResource = new HTMLResource("/menu");
            return HTTPResponse.create(OK).withHeaders(addCookieToHeader()).withBody(htmlResource);
        }
        return methodNotAllowed();
    }

    private HashMap<ResponseHeader, byte[]> addCookieToHeader() {
        HashMap<ResponseHeader, byte[]> headers = new HashMap<>();
        String sessionToken = "sessionToken=" + sessionTokenGenerator.generateToken() + ";";
        String expiryDate = "Expires=" + sessionExpiryDateGenerator.generateExpiryDate(ZonedDateTime.now());
        headers.put(ResponseHeader.COOKIE, ((sessionToken + " " + expiryDate)).getBytes());
        return headers;
    }

}
