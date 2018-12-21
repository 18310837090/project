package com.ncit.module_init.model;

public class UC_Init_userInfo {
    //用户ID
    private String userId = null;
    //密码
    private String password = null;
    //用户姓名
    private String userName = null;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
