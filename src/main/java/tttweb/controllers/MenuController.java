package tttweb.controllers;
import httpserver.Request;
import httpserver.Response;
import httpserver.httpresponse.HTTPResponse;
import httpserver.httpresponse.ResponseHeader;
import httpserver.resourcemanagement.HTMLResource;
import httpserver.routing.Route;
import tttweb.SessionExpirationDateGenerator;
import tttweb.SessionTokenGenerator;

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
            HTMLResource htmlResource = new HTMLResource("hello".getBytes());
            HashMap<ResponseHeader, byte[]> headers = new HashMap<>();
            String sessionToken = "sessionToken=" + sessionTokenGenerator.generateToken() + ";";
            String expiryDate = "Expires=" + sessionExpiryDateGenerator.generateExpiryDate(ZonedDateTime.now());
            headers.put(ResponseHeader.COOKIE, ((sessionToken + " " + expiryDate)).getBytes());
            return HTTPResponse.create(OK).withBody(htmlResource).withHeaders(headers);
        }
        return methodNotAllowed();
    }

}
