package com.ncit.module_init.model;

import org.springframework.stereotype.Repository;

@Repository
public class UC_Init_loginOutput {
    /**
     * 处理结果messageId
     */
    private String messageId = null;
    /**
     * 用户情报
     */
    private UC_Init_userInfo userInfo = null;

    public UC_Init_userInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UC_Init_userInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
