package org.epsi.b3.simplewebapp.web.product.servlet;

import org.epsi.b3.simplewebapp.db.conn.ConnectionUtils;
import org.epsi.b3.simplewebapp.db.utils.DBUtils;
import org.epsi.b3.simplewebapp.products.Product;
import org.epsi.b3.simplewebapp.web.product.ProductUtils;
import org.epsi.b3.simplewebapp.web.product.entity.ProductViewBean;

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
@WebServlet(urlPatterns = { "/editProduct" })
public class EditProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(EditProductServlet.class.getName());

    public EditProductServlet() {
        super();
    }

    // Show the view to edit a product.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");

        ProductViewBean productViewBean = null;

        String errorString = null;

        try {
            try(final Connection conn = ConnectionUtils.tryAndGetConnection()) {
                final Product product = DBUtils.findProduct(conn, code);
                conn.commit();
                if (product != null) {
                    productViewBean = ProductUtils.toViewModel(product);
                }
            }
        } catch (SQLException e) {
            errorString = e.getMessage();
            LOGGER.log(Level.WARNING, "Cannot find product " + code, e);
        }

        // No error.
        // The product to edit does not exist.
        // Redirect to the page that lists products.
        if (errorString != null && productViewBean == null) {
            response.sendRedirect(request.getServletPath() + "/productList");
            return;
        }

        // Saving information in request attribute before sending them to the views.
        request.setAttribute("errorString", errorString);
        request.setAttribute("product", productViewBean);

        RequestDispatcher dispatcher = request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/editProductView.jsp");
        dispatcher.forward(request, response);

    }

    // Once the user modified the product info and clicked on Submit.
    // This method will be executed.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String errorString = null;

        final ProductViewBean viewBean = new ProductViewBean(
                request.getParameter("code"),
                request.getParameter("name"),
                request.getParameter("price")
        );

        try {
            Product product = ProductUtils.parseFromView(viewBean);
            try(final Connection conn = ConnectionUtils.tryAndGetConnection()) {
                DBUtils.updateProduct(conn, product);
                conn.commit();
            }
        } catch (NumberFormatException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Invalid price : " + viewBean.getPrice(), e);
            }
            errorString = "Invalid price";
        } catch (SQLException e) {
            errorString = e.getMessage();
            LOGGER.log(Level.WARNING, "Unable to update product " + viewBean, e);
        }
        // Saving information in request attribute before sending them to the views.
        request.setAttribute("errorString", errorString);
        request.setAttribute("product", viewBean);

        // Errors found - forwarding to the edit page.
        if (errorString != null) {
            RequestDispatcher dispatcher = request.getServletContext()
                    .getRequestDispatcher("/WEB-INF/views/editProductView.jsp");
            dispatcher.forward(request, response);
        }
        // No error occured.
        // Forwarding to the list of products view.
        else {
            response.sendRedirect(request.getContextPath() + "/productList");
        }
    }

}