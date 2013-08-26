package org.blbcase.web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.blbcase.core.exceptions.BLBException;
import org.blbcase.core.json.additionals.ResponseConstants;

/**
 * Contains a few useful null-safe methods for working with http session
 *
 * @author danon
 */
public class SessionUtils {

    public static final String USER_ID_SESSION_ATTR = "user_id";
    private static final Logger log = Logger.getLogger(SessionUtils.class);

    public static <T> T getSessionAttribute(Class<T> clazz, final HttpSession session, String name) {
        try {
            if (isSessionValid(session)) {
                synchronized (session) {
                    return (T) session.getAttribute(name);
                }
            }
        } catch (Exception ex) {
            log.debug("Exception whyle accesssing session attribute.", ex);
        }
        return null;
    }

    public static void setSessionAttribute(final HttpSession session, String name, Object value) {
        try {
            if (isSessionValid(session)) {
                synchronized (session) {
                    session.setAttribute(name, value);
                    if (log.isTraceEnabled()) {
                        log.trace("setSessionAttribute(): " + name + " is set to " + value);
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Exception whyle trying to set session attribute.", ex);
        }
    }

    public static boolean isSessionValid(final HttpSession session) {
        if (session == null) {
            return false;
        }
        try {
            synchronized (session) {
                long sd = session.getCreationTime();
            }
        } catch (IllegalStateException ex) {
            return false;
        }
        return true;
    }

    public static HttpSession getSession(HttpServletRequest request, boolean create) {
        if (request == null) {
            return null;
        }
        return request.getSession(create);
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        return getSessionAttribute(Long.class, getSession(request, false), USER_ID_SESSION_ATTR) != null;
    }

    public static Long getUserId(HttpSession session) {
        return getSessionAttribute(Long.class, session, USER_ID_SESSION_ATTR);
    }

    public static void setUserId(HttpSession session, Long userId) {
        setSessionAttribute(session, USER_ID_SESSION_ATTR, userId);
    }

    public static Long getCurrentUserIdThrowingException(HttpServletRequest req) throws BLBException {
        HttpSession session = req.getSession(false);
        Long userId = (Long) session.getAttribute(USER_ID_SESSION_ATTR);// my mistake
        if (userId == null) {
            throw new BLBException("You are not logged in", ResponseConstants.USER_IS_NOT_LOGGED_IN);
        }
        return userId;
    }
}