package org.epsi.b3.simplewebapp.web.login.entity;

import java.util.Objects;

/**
 * An entity to store the login information.
 */
public class LoginInfo {

    private final String userName;
    private final String password;
    private final boolean rememberMe;

    public LoginInfo(String userName, String password, boolean rememberMe) {
        this.userName = userName;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void validate() throws NullPointerException {
        Objects.requireNonNull(userName, "userName is null");
        Objects.requireNonNull(password, "password is null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginInfo loginInfo = (LoginInfo) o;
        return rememberMe == loginInfo.rememberMe &&
                userName.equals(loginInfo.userName) &&
                Objects.equals(password, loginInfo.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "userName='" + userName + '\'' +
                ", password='***'" +
                ", rememberMe=" + rememberMe +
                '}';
    }
}
