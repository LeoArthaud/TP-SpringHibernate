package org.epsi.b3.simplewebapp.web.product.servlet;

import org.epsi.b3.simplewebapp.db.conn.ConnectionUtils;
import org.epsi.b3.simplewebapp.db.utils.DBUtils;
import org.epsi.b3.simplewebapp.products.Product;
import org.epsi.b3.simplewebapp.web.product.ProductUtils;
import org.epsi.b3.simplewebapp.web.product.entity.ProductViewBean;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import persistence.SessionSingleton;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A servlet to list all products.
 */
@WebServlet(urlPatterns = { "/productList" })
public class ProductListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(ProductListServlet.class.getName());
 
    public ProductListServlet() {
        super();
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String errorString = null;
            ArrayList<ProductViewBean> list = new ArrayList<>();

        SessionFactory session = SessionSingleton.getSession();

        Session currentSession =  session.getCurrentSession();

            final Transaction transaction =
                    currentSession.beginTransaction();
            try {
                CriteriaBuilder builder = currentSession.getCriteriaBuilder();
                CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
                criteria.from(Product.class);
                List<Product> listProduit = currentSession.createQuery(criteria).getResultList();

                for (Product product : listProduit)
                {
                    ProductViewBean productViewBean = new ProductViewBean(product.getCode(),product.getName(),String.valueOf(product.getPrice()));
                    list.add(productViewBean);
                }

            } catch (RuntimeException e) {
                errorString = e.getMessage();
                LOGGER.log(Level.WARNING, errorString, e);
                transaction.rollback();
            }

            currentSession.close();
//            throws ServletException, IOException {
//
//        String errorString = null;
//        List<ProductViewBean> list = null;
//        try {
//            try (Connection conn = ConnectionUtils.tryAndGetConnection()) {
//                list = DBUtils.queryProduct(conn)
//                .stream()
//                .map(ProductUtils::toViewModel)
//                .collect(Collectors.toList());
//                conn.commit();
//            }
//        } catch (SQLException e) {
//            errorString = e.getMessage();
//            LOGGER.log(Level.WARNING, "Unable to list products", e);
//        }
        // Saving information in request attribute before sending them to the views.
        request.setAttribute("errorString", errorString);
        request.setAttribute("productList", list);

        // Forwarding the the JSP view that lists products.
        RequestDispatcher dispatcher = request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/productListView.jsp");
        dispatcher.forward(request, response);
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
 
}