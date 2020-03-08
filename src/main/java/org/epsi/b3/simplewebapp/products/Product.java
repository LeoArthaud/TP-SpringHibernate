package org.epsi.b3.simplewebapp.products;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.zip.DataFormatException;

/**
 * An entity to model the view of a product.
 */
@Entity
@Table(name="Product")
public class Product {

    @Id
    private String code;
    @Basic
    private String name;
    @Basic
    private Float price;

    public Product() {

    }

    public Product(String code, String name, Float price) {
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

    public float getPrice() {
        return (price == null) ? 0 : price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void validate() throws DataFormatException {;

        // The product ID is the literal [a-zA-Z_0-9]
        // with at least one letter
        String regex = "\\w+";

        if (code == null || !code.matches(regex)) {
            throw new DataFormatException("Invalid product code : " + code);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Float.compare(product.price, price) == 0 &&
                code.equals(product.code) &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "Product{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
