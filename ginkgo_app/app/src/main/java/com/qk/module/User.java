package com.qk.module;

import java.util.Date;
import java.util.List;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/13
 * 用户实体类
 */
public class User {

    private String id;
    private boolean isNewRecord;
    private String remarks;
    private Date createDate;
    private Date updateDate;
    private String loginName;
    private String password;
    private String no;
    private String name;
    private String email;
    private String phone;
    private String mobile;
    private String userType;
    private String loginIp;
    private Date loginDate;
    private String loginFlag;
    private String photo;
    private String oldLoginName;
    private String newPassword;
    private String oldLoginIp;
    private Date oldLoginDate;
    private List<String> roleList;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }
    public boolean getIsNewRecord() {
        return isNewRecord;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public String getRemarks() {
        return remarks;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getCreateDate() {
        return createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getLoginName() {
        return loginName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setNo(String no) {
        this.no = no;
    }
    public String getNo() {
        return no;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getMobile() {
        return mobile;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getUserType() {
        return userType;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }
    public String getLoginFlag() {
        return loginFlag;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getPhoto() {
        return photo;
    }

    public void setOldLoginName(String oldLoginName) {
        this.oldLoginName = oldLoginName;
    }
    public String getOldLoginName() {
        return oldLoginName;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }

    public void setOldLoginIp(String oldLoginIp) {
        this.oldLoginIp = oldLoginIp;
    }
    public String getOldLoginIp() {
        return oldLoginIp;
    }

    public void setOldLoginDate(Date oldLoginDate) {
        this.oldLoginDate = oldLoginDate;
    }
    public Date getOldLoginDate() {
        return oldLoginDate;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }
    public List<String> getRoleList() {
        return roleList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", isNewRecord=" + isNewRecord +
                ", remarks='" + remarks + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", no='" + no + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", mobile='" + mobile + '\'' +
                ", userType='" + userType + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", loginDate=" + loginDate +
                ", loginFlag='" + loginFlag + '\'' +
                ", photo='" + photo + '\'' +
                ", oldLoginName='" + oldLoginName + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", oldLoginIp='" + oldLoginIp + '\'' +
                ", oldLoginDate=" + oldLoginDate +
                ", roleList=" + roleList +
                '}';
    }
}