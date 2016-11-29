package tttweb;

import httpserver.SocketServer;
import httpserver.routing.Route;
import httpserver.server.HTTPLogger;
import httpserver.server.HTTPServer;
import httpserver.server.HTTPSocketServer;
import tttweb.controllers.MenuController;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {

    public static void start() {
        TTTRouter router = createRouter();
        HTTPLogger logger = new HTTPLogger("./logs");
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        SocketServer socketServer = getSocketServer();
        HTTPServer httpServer = new HTTPServer(socketServer, router, logger);
        try {
            httpServer.start(executorService);
        } finally {
            executorService.shutdown();
        }
    }

    private static SocketServer getSocketServer() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HTTPSocketServer(serverSocket);
    }

    private static TTTRouter createRouter() {
        List<Route> routes = new ArrayList();
        routes.add(new MenuController(new SessionTokenGenerator(), new SessionExpirationDateGenerator()));
        return new TTTRouter(routes);
    }
}
