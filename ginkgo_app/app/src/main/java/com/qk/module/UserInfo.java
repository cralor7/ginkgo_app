package com.qk.module;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/13
 */
public class UserInfo {
        private String no;
        private String phone;
        private String name;
        private String mobile;
        private String company;
        private String office;
        private String email;
        private String username;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompany() {
        return company;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserInfo(String no, String phone, String name, String mobile, String company, String office, String email, String username) {
        this.no = no;
        this.phone = phone;
        this.name = name;
        this.mobile = mobile;
        this.company = company;
        this.office = office;
        this.email = email;
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "no='" + no + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", company='" + company + '\'' +
                ", office='" + office + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
