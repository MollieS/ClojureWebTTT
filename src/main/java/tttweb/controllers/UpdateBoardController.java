package tttweb.controllers;

import httpserver.Request;
import httpserver.Response;
import httpserver.routing.Route;
import tttweb.SessionManager;

import static httpserver.routing.Method.POST;

public class UpdateBoardController extends Route {

    public UpdateBoardController(SessionManager sessionManagerStub) {
        super("/update-board", POST);
    }

    @Override
    public Response performAction(Request request) {
        return null;
    }
}
