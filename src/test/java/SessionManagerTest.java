import httpserver.sessions.HTTPSession;
import httpserver.sessions.Session;
import httpserver.sessions.SessionFactory;
import org.junit.Test;
import tttweb.SessionManager;

import static org.junit.Assert.*;

public class SessionManagerTest {

    private SessionFactorySpy sessionFactorySpy = new SessionFactorySpy();
    private SessionManager sessionManager = new SessionManager(sessionFactorySpy);

    @Test
    public void knowsIfSessionDoesNotExists() {
        assertFalse(sessionManager.exists("1"));
    }

    @Test
    public void knowsIfSessionExists() {
        sessionManager.getOrCreateSession("1");

        assertTrue(sessionManager.exists("1"));
    }

    @Test
    public void createsANewSessionIfOneDoesNotExist() {
        Session session = sessionManager.getOrCreateSession("1");

        assertEquals(1, sessionFactorySpy.timesCalled);
        assertNotNull(session);
        assertEquals("1", sessionFactorySpy.createdSession.getId());
    }

    @Test
    public void findsCorrectSessionIfItExists() {
        sessionManager.getOrCreateSession("1");
        sessionManager.getOrCreateSession("1");

        assertEquals(1, sessionFactorySpy.timesCalled);
    }

    private class SessionFactorySpy implements SessionFactory {

        public HTTPSession createdSession;
        public int timesCalled = 0;

        @Override
        public Session createSession(String id) {
            timesCalled++;
            createdSession = new HTTPSession(id);
            return createdSession;
        }
    }
}
