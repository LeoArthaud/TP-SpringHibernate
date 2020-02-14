package org.epsi.b3.simplewebapp.web.product.entity;

import java.util.Objects;

/**
 * An entity to model the view of a product.
 */
public class ProductViewBean {

    private String code;
    private String name;
    private String price;

    public ProductViewBean() {

    }

    public ProductViewBean(String code, String name, String price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductViewBean that = (ProductViewBean) o;
        return code.equals(that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "ProductViewBean{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}