package org.epsi.b3.simplewebapp.web.login.servlet;

import org.epsi.b3.simplewebapp.db.conn.ConnectionUtils;
import org.epsi.b3.simplewebapp.db.utils.DBUtils;
import org.epsi.b3.simplewebapp.users.UserAccount;
import org.epsi.b3.simplewebapp.web.login.entity.LoginInfo;
import org.epsi.b3.simplewebapp.web.user.entity.UserInfo;
import org.epsi.b3.simplewebapp.web.utils.HttpUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A servlet to manage the login of a user.
 */
@WebServlet(urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
 
    public LoginServlet() {
        super();
    }
 
    // Show the login page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        // Forward to the login view
        // (The user cannot directly access to
        // the JSP page in the WEB-INF folder).
        RequestDispatcher dispatcher //
                = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
 
        dispatcher.forward(request, response);
 
    }

    // When the user enters userName & password, and clicks on the Submit button
    // This method is executed
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean hasError = false;
        String errorString = null;
        UserAccount user = null;

        final LoginInfo loginInfo = new LoginInfo(
                    request.getParameter("userName"),
                    request.getParameter("password"),
                    "Y".equals(request.getParameter("rememberMe"))
            );
        try {
            // validate the received data
            loginInfo.validate();

            // Search for the user in the DB
            try(final Connection conn = ConnectionUtils.tryAndGetConnection()) {
                user = DBUtils.findUser(conn, loginInfo.getUserName(), loginInfo.getPassword());
                conn.commit();
            }

            if (user == null) {
                hasError = true;
                errorString = "User Name or password invalid";
            }
        } catch (RuntimeException | SQLException e) {
            LOGGER.log(Level.WARNING, "Unable to retrieve the user", e);
            hasError = true;
            errorString = e.getMessage();
        }

        // In case of error
        // forward to the login view again
        if (hasError) {

            // Saving datas in request params
            request.setAttribute("errorString", errorString);
            request.setAttribute("user", loginInfo);
 
            // Forward to the JSP view
            RequestDispatcher dispatcher //
                    = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
 
            dispatcher.forward(request, response);
        } else {
            // No error
            // Save user information in the session
            // and forward to the user info page
            HttpSession session = request.getSession();
            HttpUtils.storeLoginedUser(session, UserInfo.fromAccount(user));
 
            // If the user selects the "Remember me" checkbok store the user in a cookie
            if (loginInfo.isRememberMe()) {
                HttpUtils.storeUserCookie(response, user.getUserName());
            }
            // Else, delete the cookie
            else {
                HttpUtils.deleteUserCookie(response);
            }
 
            // Redirect to the user info page
            response.sendRedirect(request.getContextPath() + "/userInfo");
        }
    }
 
}