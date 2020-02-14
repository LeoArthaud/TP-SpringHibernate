package org.epsi.b3.simplewebapp.web.product;

import org.epsi.b3.simplewebapp.products.Product;
import org.epsi.b3.simplewebapp.web.product.entity.ProductViewBean;

/**
 * A utility class to manipulate products.
 */
public class ProductUtils {

    /**
     * A parser to generate a {@link Product} from a {@link ProductViewBean}.
     * @param viewBean the bean from the view
     * @return the business object that models a product
     * @throws NumberFormatException if the price cannot be parsed correctly
     */
    public static Product parseFromView(ProductViewBean viewBean) throws NumberFormatException {

        final Product product = new Product();
        product.setCode(viewBean.getCode());
        product.setName(viewBean.getName());
        final String price = viewBean.getPrice();
        if (price != null && !price.isEmpty()) {
            product.setPrice(Float.parseFloat(price));
        }
        return product;

    }

    public static ProductViewBean toViewModel(Product product) {
        return new ProductViewBean(
                product.getCode(),
                product.getName(),
                Float.toString(product.getPrice())
        );
    }
}
