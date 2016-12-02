import httpserver.sessions.HTTPSession;
import httpserver.sessions.Session;
import httpserver.sessions.SessionFactory;
import org.junit.Test;
import tttweb.SessionManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SessionManagerTest {

    @Test
    public void createsANewSessionIfOneDoesNotExist() {
        SessionFactorySpy sessionFactorySpy = new SessionFactorySpy();
        SessionManager sessionManager = new SessionManager(sessionFactorySpy);
        Session session = sessionManager.getCurrentSession("1");

        assertEquals(1, sessionFactorySpy.timesCalled);
        assertNotNull(session);
        assertEquals("1", sessionFactorySpy.createdSession.getId());
    }

    @Test
    public void findsCorrectSessionIfItExists() {
        SessionFactorySpy sessionFactorySpy = new SessionFactorySpy();
        SessionManager sessionManager = new SessionManager(sessionFactorySpy);
        sessionManager.getCurrentSession("1");
        sessionManager.getCurrentSession("1");

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
