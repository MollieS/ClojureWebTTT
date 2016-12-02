package tttweb;

import httpserver.sessions.Session;
import httpserver.sessions.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    private final SessionFactory sessionFactory;
    private List<Session> sessions = new ArrayList<>();

    public SessionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession(String sessionID) {
        if (exists(sessionID)) {
            return getSession(sessionID);
        }
        return createNewSession(sessionID);
    }

    private Session createNewSession(String sessionID) {
        Session session = sessionFactory.createSession(sessionID);
        sessions.add(session);
        return session;
    }

    private Session getSession(String sessionID) {
        for (Session session : sessions) {
            if (session.getId().equals(sessionID)) {
                return session;
            }
        }
        return null;
    }

    private boolean exists(String sessionID) {
        return getSession(sessionID) != null;
    }
}
