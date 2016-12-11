package tttweb.doubles;

import httpserver.sessions.HTTPSession;
import httpserver.sessions.Session;
import httpserver.sessions.SessionManager;

public class SessionManagerStub extends SessionManager {

    private HTTPSession session;

    public SessionManagerStub() {
        super(new SessionFactorySpy());
        this.session = new HTTPSession("1");
    }

    @Override
    public Session getOrCreateSession(String sessionId) {
        session.addData("boardState", "---------");
        session.addData("gameType", "hvh");
        return session;
    }

    @Override
    public boolean exists(String sessionId) {
        return sessionId.equals("1");
    }

    @Override
    public Session getSession(String sessionID) {
        return session;
    }
}
