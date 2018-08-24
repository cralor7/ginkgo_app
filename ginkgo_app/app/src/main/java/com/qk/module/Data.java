package com.qk.module;

import java.util.ArrayList;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 存放登录返回的数据
 */
public class Data {
    private String code;
    private String error;
    private String username;
    private String message;
    private String token;
    private LoginEntity data;
    private String tokenValid;
    private ArrayList<ArrayList<Menu>>  menuList;
    private UserInfo userInfo;


    public Data(String code, String error, String username, String message, String token, LoginEntity data, String tokenValid, ArrayList<ArrayList<Menu>> menuList, UserInfo userIfo) {
        this.code = code;
        this.error = error;
        this.username = username;
        this.message = message;
        this.token = token;
        this.data = data;
        this.tokenValid = tokenValid;
        this.menuList = menuList;
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "Data{" +
                "code='" + code + '\'' +
                ", error='" + error + '\'' +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", data=" + data +
                ", tokenValid='" + tokenValid + '\'' +
                ", menuList=" + menuList +
                ", userInfo=" + userInfo +
                '}';
    }

    public String getTokenValid() {
        return tokenValid;
    }

    public void setTokenValid(String tokenValid) {
        this.tokenValid = tokenValid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Data() { }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setData(LoginEntity data) {
        this.data = data;
    }

    public LoginEntity getData() {
        return data;
    }

    public ArrayList<ArrayList<Menu>> getMenuList() {
        return menuList;
    }

    public void setMenuList(ArrayList<ArrayList<Menu>> menuList) {
        this.menuList = menuList;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * 用户所属公司和部门
     */
    public class UserInfo {
        String username;
        String company;

        String office;

        public String getCompany() {
            return company;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getOffice() {
            return office;
        }

        public void setOffice(String office) {
            this.office = office;
        }

        public UserInfo(String username, String company, String office) {
            this.username = username;
            this.company = company;
            this.office = office;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "username='" + username + '\'' +
                    ", company='" + company + '\'' +
                    ", office='" + office + '\'' +
                    '}';
        }
    }

}
