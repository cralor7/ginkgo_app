package com.qk.module;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 *
 */
public class LoginEntity {

    private String username;
   private User user;
   private String token;

    public LoginEntity(String username, User user, String token) {
        this.username = username;
        this.user = user;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginEntity{" +
                "username='" + username + '\'' +
                ", user=" + user +
                ", token='" + token + '\'' +
                '}';
    }
}