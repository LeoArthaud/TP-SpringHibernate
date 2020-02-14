package org.epsi.b3.simplewebapp.web.filter;

import org.epsi.b3.simplewebapp.db.conn.ConnectionUtils;
import org.epsi.b3.simplewebapp.db.utils.DBUtils;
import org.epsi.b3.simplewebapp.users.UserAccount;
import org.epsi.b3.simplewebapp.web.user.entity.UserInfo;
import org.epsi.b3.simplewebapp.web.utils.HttpUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A filter to store and retrieve the current logged in user in a cookie.
 */
@WebFilter(filterName = "cookieFilter", urlPatterns = { "/*" })
public class CookieFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(CookieFilter.class.getName());

    public CookieFilter() {
    }

    @Override
    public void init(FilterConfig fConfig) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        // Retrieve the user info stored in the current session
        UserInfo userInSession = HttpUtils.getLoginedUser(session);
        if (userInSession != null) {
            session.setAttribute("COOKIE_CHECKED", "CHECKED");
            chain.doFilter(request, response);
            return;
        }

        // The COOKIE_CHECKED attribute flags the verif of the user cookie
        boolean checked = session.getAttribute("COOKIE_CHECKED") != null;
        if (!checked) {
            HttpUtils.getUserNameInCookie(req)
                    // userName found - retrieve associated user in DB and store logged in user
                    .ifPresent( userName -> {
                        try {
                            try (final Connection conn = ConnectionUtils.tryAndGetConnection()) {
                                UserAccount userAccount = DBUtils.findUser(conn, userName);
                                conn.commit();
                                if (userAccount != null) {
                                    HttpUtils.storeLoginedUser(session, UserInfo.fromAccount(userAccount));
                                }
                            }
                        } catch (SQLException e) {
                            LOGGER.log(Level.WARNING, "Unable to find user " + userName + " in DB", e);
                        }
                    });
            // Cookie verified
            session.setAttribute("COOKIE_CHECKED", "CHECKED");
        }

        chain.doFilter(request, response);
    }

}