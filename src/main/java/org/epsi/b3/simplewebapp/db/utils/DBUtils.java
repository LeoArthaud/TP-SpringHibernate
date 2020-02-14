package org.epsi.b3.simplewebapp.db.utils;

import org.epsi.b3.simplewebapp.products.Product;
import org.epsi.b3.simplewebapp.users.Gender;
import org.epsi.b3.simplewebapp.users.UserAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {

    public static UserAccount findUser(
            Connection conn,
            String userName,
            String password
    ) throws SQLException {
 
        String sql = "Select a.idUser, a.userName, a.password, a.gender from USER_ACCOUNT a " //
                + " where a.userName = ? and a.password= ?";
 
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, userName);
        pstm.setString(2, password);
        ResultSet rs = pstm.executeQuery();
 
        if (rs.next()) {
            UserAccount user = new UserAccount();
            user.setIdUser(rs.getInt("idUser"));
            user.setUserName(userName);
            user.setPassword(password);
            user.setGender(Gender.values()[rs.getInt("gender")]);
            return user;
        }
        return null;
    }
 
    public static UserAccount findUser(Connection conn, String userName) throws SQLException {
 
        String sql = "Select a.idUser, a.userName, a.password, a.gender from USER_ACCOUNT a "//
                + " where a.userName = ? ";
 
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, userName);
 
        ResultSet rs = pstm.executeQuery();
 
        if (rs.next()) {
            int idUser = rs.getInt("idUser");
            String password = rs.getString("password");
            UserAccount user = new UserAccount();
            user.setIdUser(idUser);
            user.setUserName(userName);
            user.setPassword(password);
            user.setGender(Gender.values()[rs.getInt("gender")]);
            return user;
        }
        return null;
    }
 
    public static List<Product> queryProduct(Connection conn) throws SQLException {
        String sql = "Select a.code, a.name, a.price from PRODUCT a ";
 
        PreparedStatement pstm = conn.prepareStatement(sql);
 
        ResultSet rs = pstm.executeQuery();
        List<Product> list = new ArrayList<Product>();
        while (rs.next()) {
            String code = rs.getString("code");
            String name = rs.getString("name");
            float price = rs.getFloat("price");
            Product product = new Product();
            product.setCode(code);
            product.setName(name);
            product.setPrice(price);
            list.add(product);
        }
        return list;
    }
 
    public static Product findProduct(Connection conn, String code) throws SQLException {
        String sql = "Select a.code, a.name, a.price from PRODUCT a where a.code=?";
 
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, code);
 
        ResultSet rs = pstm.executeQuery();
 
        while (rs.next()) {
            String name = rs.getString("name");
            float price = rs.getFloat("price");
            Product product = new Product(code, name, price);
            return product;
        }
        return null;
    }
 
    public static void updateProduct(Connection conn, Product product) throws SQLException {
        String sql = "Update PRODUCT set name =?, price=? where code=? ";
 
        PreparedStatement pstm = conn.prepareStatement(sql);
 
        pstm.setString(1, product.getName());
        pstm.setFloat(2, product.getPrice());
        pstm.setString(3, product.getCode());
        pstm.executeUpdate();
    }
 
    public static void insertProduct(Connection conn, Product product) throws SQLException {
        String sql = "Insert into PRODUCT(code, name,price) values (?,?,?)";
 
        PreparedStatement pstm = conn.prepareStatement(sql);
 
        pstm.setString(1, product.getCode());
        pstm.setString(2, product.getName());
        pstm.setFloat(3, product.getPrice());
 
        pstm.executeUpdate();
    }
 
    public static void deleteProduct(Connection conn, String code) throws SQLException {
        String sql = "Delete From PRODUCT where code= ?";
 
        PreparedStatement pstm = conn.prepareStatement(sql);
 
        pstm.setString(1, code);
 
        pstm.executeUpdate();
    }
 
}
