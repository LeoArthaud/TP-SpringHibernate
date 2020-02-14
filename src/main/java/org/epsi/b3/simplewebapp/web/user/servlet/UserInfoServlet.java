package org.epsi.b3.simplewebapp.web.user.servlet;

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

/**
 * A servlet to show information on the current user.
 */
@WebServlet(urlPatterns = { "/userInfo" })
public class UserInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserInfoServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check if the user is logged in or not
        UserInfo loggedInUser = HttpUtils.getLoginedUser(session);

        // Not logged in
        if (loggedInUser == null) {
            // Redirect to the login page
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        // logged in - store user information in the request attribute before forwarding the request
        request.setAttribute("user", loggedInUser);

        // If the user is logged in, forward to the user info view
        RequestDispatcher dispatcher //
                = this.getServletContext().getRequestDispatcher("/WEB-INF/views/userInfoView.jsp");
        dispatcher.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}