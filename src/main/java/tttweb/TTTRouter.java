package tttweb;

import httpserver.Request;
import httpserver.Response;
import httpserver.httpresponse.HTTPResponse;
import httpserver.routing.Route;
import httpserver.routing.Router;

import java.net.URI;
import java.util.List;

import static httpserver.httpresponse.StatusCode.NOTFOUND;

public class TTTRouter extends Router {

    private final List<Route> registeredRoutes;

    public TTTRouter(List<Route> registeredRoutes) {
        super(null, registeredRoutes);
        this.registeredRoutes = registeredRoutes;
    }

    @Override
    public Response route(Request request) {
        for (Route route : registeredRoutes) {
            if (isRegistered(request.getRequestURI(), route)) {
                return route.performAction(request);
            }
        }
        return HTTPResponse.create(NOTFOUND);
    }

    private boolean isRegistered(URI uri, Route route) {
        return route.getUri().equals(uri);
    }
}
