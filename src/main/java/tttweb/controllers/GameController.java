package tttweb.controllers;

import httpserver.Request;
import httpserver.Response;
import httpserver.httpresponse.HTTPResponse;
import httpserver.resourcemanagement.HTMLResource;
import httpserver.routing.Route;

import static httpserver.httpresponse.StatusCode.OK;
import static httpserver.routing.Method.POST;

public class GameController extends Route {

    public GameController() {
        super("/game", POST);
    }

    @Override
    public Response performAction(Request request) {
        HTMLResource htmlResource = new HTMLResource("hello".getBytes());
        return HTTPResponse.create(OK).withBody(htmlResource);
    }
}
