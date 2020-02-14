package org.epsi.b3.simplewebapp.web.product.servlet;

import org.epsi.b3.simplewebapp.db.conn.ConnectionUtils;
import org.epsi.b3.simplewebapp.db.utils.DBUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A servlet to manage the deletion of a product.
 */
@WebServlet(urlPatterns = { "/deleteProduct" })
public class DeleteProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(DeleteProductServlet.class.getName());

    public DeleteProductServlet() {
        super();
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
 
        String errorString = null;
 
        try {
            try (Connection conn = ConnectionUtils.tryAndGetConnection()) {
                DBUtils.deleteProduct(conn, code);
                conn.commit();
            }
        } catch (SQLException e) {
            errorString = e.getMessage();
            LOGGER.log(Level.WARNING, "Unable to delete the product with code " + code, e);
        } 
         
        // If errors found, forward them to the error page.
        if (errorString != null) {
            // Saving informations in the request attribute before sending them to the views.
            request.setAttribute("errorString", errorString);

            RequestDispatcher dispatcher = request.getServletContext()
                    .getRequestDispatcher("/WEB-INF/views/deleteProductErrorView.jsp");
            dispatcher.forward(request, response);
        }
        // No problem occured.
        // Redirect to the page that lists all products.
        else {
            response.sendRedirect(request.getContextPath() + "/productList");
        }
 
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
 
}