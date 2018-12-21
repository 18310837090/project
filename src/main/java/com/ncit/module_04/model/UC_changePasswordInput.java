package com.ncit.module_04.model;

/**
 * Created by kunpeng.ren on 2018/06/05
 */
public class UC_changePasswordInput {

    /**
     * 旧密码
     */
    private String oldPassword = null;
    public String getOldPassword() {
        return oldPassword;
    }
    public void setOldPassword(String pwd) {
        this.oldPassword = pwd;
    }

    /**
     * 新密码
     */
    private String newPassword = null;
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String pwd) {
        this.newPassword = pwd;
    }

    /**
     * 新密码(验证)
     */
    private String newPassword2 = null;
    public String getNewPassword2() {
        return newPassword2;
    }
    public void setNewPassword2(String pwd) {
        this.newPassword2 = pwd;
    }
}
