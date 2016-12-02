package tttweb.controllers;

import httpserver.sessions.HTTPSession;
import httpserver.sessions.Session;
import httpserver.sessions.SessionFactory;

class SessionFactorySpy implements SessionFactory {

    public HTTPSession createdSession;
    public int timesCalled = 0;

    @Override
    public Session createSession(String id) {
        timesCalled++;
        createdSession = new HTTPSession(id);
        return createdSession;
    }
}
