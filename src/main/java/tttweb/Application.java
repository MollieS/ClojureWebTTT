package tttweb;

import httpserver.SocketServer;
import httpserver.routing.Route;
import httpserver.routing.Router;
import httpserver.server.HTTPLogger;
import httpserver.server.HTTPServer;
import httpserver.server.HTTPSocketServer;
import httpserver.sessions.HTTPSessionFactory;
import httpserver.sessions.SessionExpirationDateGenerator;
import httpserver.sessions.SessionManager;
import httpserver.sessions.SessionTokenGenerator;
import tttweb.controllers.GameController;
import tttweb.controllers.MenuController;
import tttweb.controllers.NewGameController;
import tttweb.controllers.UpdateBoardController;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {

    public static void start() {
        Router router = createRouter();
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

    private static Router createRouter() {
        List<Route> routes = new ArrayList();
        SessionManager sessionManager = new SessionManager(new HTTPSessionFactory());
        routes.add(new MenuController(new SessionTokenGenerator(), new SessionExpirationDateGenerator()));
        routes.add(new NewGameController(sessionManager));
        routes.add(new GameController(sessionManager));
        routes.add(new UpdateBoardController(sessionManager));
        return new Router(routes);
    }
}
