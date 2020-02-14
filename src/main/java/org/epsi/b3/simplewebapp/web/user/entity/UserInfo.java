package org.epsi.b3.simplewebapp.web.user.entity;

import org.epsi.b3.simplewebapp.users.UserAccount;

import java.util.Objects;

/**
 * An entity to model the view of a user.
 */
public class UserInfo {

    private final String name;
    private final String gender;

    public UserInfo(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }

    public static UserInfo fromAccount(UserAccount userAccount) {
        return new UserInfo(userAccount.getUserName(), userAccount.getGender().name());
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return name.equals(userInfo.name) &&
                Objects.equals(gender, userInfo.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
