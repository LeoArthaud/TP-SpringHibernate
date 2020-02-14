package org.epsi.b3.simplewebapp.web.utils;

import org.epsi.b3.simplewebapp.web.user.entity.UserInfo;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class of helpers to perform common operations on HTTP sessions, request and responses.
 */
public class HttpUtils {

    public static final String ATT_NAME_CONNECTION = "ATTRIBUTE_FOR_CONNECTION";

    /**
     * The name of the cookie that will store the user name of the current session.
     */
    public static final String USERNAME_COOKIE = "USER_COOKIE";

    /**
     * The name of the session attribute that stores the user account.
     */
    private static final String USER_ACCOUNT_SESSION_ATTRIBUTE = "loginedUser";

    private static final Logger LOGGER = Logger.getLogger(HttpUtils.class.getName());

    /**
     * Stores the connection in the attribute {@link #ATT_NAME_CONNECTION} of a request.
     * Stored information only exist for the request until they are sent to the user browser.
     * <strong>This method has a side effect on the {@code request} param</strong>
     * @param request the request to update
     * @param conn the connection to store
     */
    public static void storeConnection(ServletRequest request, Connection conn) {
        request.setAttribute(ATT_NAME_CONNECTION, conn);
    }

    /**
     * Gets the connection stored in the attribute {@link #ATT_NAME_CONNECTION} of a request.
     * @param request the request to query
     * @return the connection stored
     */
    public static Connection getStoredConnection(ServletRequest request) {
        return (Connection) request.getAttribute(ATT_NAME_CONNECTION);
    }

    /**
     * Stores the user account in the HTTP session attribute {@link #USER_ACCOUNT_SESSION_ATTRIBUTE}.
     * <strong>This method has a side effect on the {@code session} param</strong>
     * @param session the session to update
     * @param loginedUser the user account to store
     */
    public static void storeLoginedUser(HttpSession session, UserInfo loginedUser) {
        // Available in a JSP page via ${loginedUser}
        session.setAttribute(USER_ACCOUNT_SESSION_ATTRIBUTE, loginedUser);
    }

    /**
     * Gets user information stored in the attribute {@link #USER_ACCOUNT_SESSION_ATTRIBUTE} of a HTTP session.
     * @param session the HTTP session to query
     * @return the user account stored
     */
    public static UserInfo getLoginedUser(HttpSession session) {
        return (UserInfo) session.getAttribute(USER_ACCOUNT_SESSION_ATTRIBUTE);
    }

    /**
     * Stores the username in the {@link #USERNAME_COOKIE} cookie.
     * <strong>This method has a side effect on the {@code response} param</strong>
     * @param response the HTTP response with the added cookie
     * @param userName the username to store
     */
    public static void storeUserCookie(HttpServletResponse response, String userName) {
        LOGGER.log(Level.FINE, "Stored user {0} in cookie", userName);
        Cookie cookieUserName = new Cookie(USERNAME_COOKIE, userName);
        // keep the cookie 1 day (converted in seconds)
        cookieUserName.setMaxAge(24 * 60 * 60);
        response.addCookie(cookieUserName);
    }

    /**
     * Gets the user name stored in the {@link #USERNAME_COOKIE} cookie of the given request.
     * @param request the request to query
     * @return the username found if any
     */
    public static Optional<String> getUserNameInCookie(HttpServletRequest request) {
        return Optional
                .ofNullable(request.getCookies()) // get cookies if any
                .flatMap(cookies -> Arrays.stream(cookies) // iterate on cookies
                        // keep cookies with the name searched
                        .filter(cookie -> USERNAME_COOKIE.equals(cookie.getName()))
                        // only 1 cookie expected - if any
                        .findFirst()
                ).map(Cookie::getValue); // get the username in the value of the cookie
    }

    /**
     * Deletes the username cookie {@link #USERNAME_COOKIE}.
     * This is done by injecting a fake cookie that will expire immediately.
     * <strong>This method has a side effect on the {@code response} param</strong>
     * @param response the modified response
     */
    public static void deleteUserCookie(HttpServletResponse response) {
        Cookie cookieUserName = new Cookie(USERNAME_COOKIE, null);
        // 0 second. (this cookie will expire immediately)
        cookieUserName.setMaxAge(0);
        response.addCookie(cookieUserName);
    }

}