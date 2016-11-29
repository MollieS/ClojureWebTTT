package tttweb.controllers;

import httpserver.Request;
import httpserver.Response;
import httpserver.httprequests.RequestHeader;
import httpserver.httpresponse.HTTPResponse;
import httpserver.routing.Route;

import static httpserver.httpresponse.StatusCode.REDIRECT;
import static httpserver.routing.Method.POST;

public class NewGameController extends Route {

    public NewGameController() {
        super("/game", POST);
    }

    @Override
    public Response performAction(Request request) {
        String gameType = (request.getValue(RequestHeader.DATA));
        System.out.println(gameType);

        return HTTPResponse.create(REDIRECT);
    }
}
