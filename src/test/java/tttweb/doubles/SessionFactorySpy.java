package tttweb.doubles;

import httpserver.sessions.HTTPSession;
import httpserver.sessions.Session;
import httpserver.sessions.SessionFactory;

public class SessionFactorySpy implements SessionFactory {

    public HTTPSession createdSession;
    public int timesCalled = 0;

    @Override
    public Session createSession(String id) {
        timesCalled++;
        createdSession = new HTTPSession(id);
        return createdSession;
    }
}
