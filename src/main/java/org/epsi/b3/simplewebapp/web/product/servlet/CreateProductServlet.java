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
import java.util.zip.DataFormatException;

/**
 * A servlet to manage the creation of a Product.
 */
@WebServlet(urlPatterns = { "/createProduct" })
public class CreateProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(CreateProductServlet.class.getName());
 
    public CreateProductServlet() {
        super();
    }
 
    // Print the view to create a product
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        RequestDispatcher dispatcher = request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
        dispatcher.forward(request, response);
    }

    // The method called after the user "Submit" the creation of the product.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final ProductViewBean viewBean = new ProductViewBean(
                request.getParameter("code"),
                request.getParameter("name"),
                request.getParameter("price")

        );
        Product product = null;
        String errorString = null;

        try {
            product = ProductUtils.parseFromView(viewBean);
            product.validate();
        } catch (NumberFormatException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Invalid price : " + viewBean.getPrice(), e);
            }
            errorString = "Invalid price";
        } catch (DataFormatException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Invalid product code : " + viewBean.getCode(), e);
            }
            errorString = "Product Code invalid!";
        }

        if (errorString == null) {
            try {
                try (Connection conn = ConnectionUtils.tryAndGetConnection()) {
                        DBUtils.insertProduct(conn, product);
                        conn.commit();
                }
            } catch (SQLException e) {
                errorString = e.getMessage();
                LOGGER.log(Level.WARNING, "Unable to insert the product " + viewBean, e);
            }
        }

        // Save the informations in the request attribute before sending them to the views.
        request.setAttribute("errorString", errorString);
        request.setAttribute("product", viewBean);
 
        // If errors found, forward them to the 'edit' page.
        if (errorString != null) {
            RequestDispatcher dispatcher = request.getServletContext()
                    .getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
            dispatcher.forward(request, response);
        }
        // No problem occured - redirect to the list of products
        else {
            response.sendRedirect(request.getContextPath() + "/productList");
        }
    }
 
}