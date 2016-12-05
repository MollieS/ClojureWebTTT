package tttweb.controllers;

import httpserver.sessions.HTTPSession;
import httpserver.sessions.Session;
import tttweb.SessionManager;

class SessionManagerStub extends SessionManager {

    private HTTPSession validSession;
    private HTTPSession invalidSession;

    public SessionManagerStub() {
        super(new SessionFactorySpy());
        this.validSession = new HTTPSession("1");
        validSession.addData("gameType", "hvh");
        validSession.addData("boardState", "---------");
        this.invalidSession = new HTTPSession("2");
    }

    @Override
    public Session getOrCreateSession(String sessionId) {
        if (sessionId.equals("1")) {
            return validSession;
        }
        return invalidSession;
    }

    @Override
    public boolean exists(String sessionId) {
        return sessionId.equals("1") || sessionId.equals("2");
    }

    @Override
    public Session getSession(String sessionID) {
        return getOrCreateSession(sessionID);
    }
}
