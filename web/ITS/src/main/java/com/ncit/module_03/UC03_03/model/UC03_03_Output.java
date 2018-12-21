package com.ncit.module_03.UC03_03.model;

import java.util.Map;

public class UC03_03_Output {
    /**
     * 返回信息提示
     */
    private String messageId = null;
    /**
     * 处理结果
     */
    private Map result = null;

    public Map getResult() {
        return result;
    }

    public void setResult(Map result) {
        this.result = result;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
