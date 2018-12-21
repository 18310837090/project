package com.ncit.common.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(ignoreUnknownFields = false,ignoreInvalidFields = true)
@PropertySource(value = {"classpath:db.yml"},encoding="UTF-8")
@Component
public class Config {
    private String ID;
    private String USERNAME;
    private String PASSWORD;
    private String status;
    private String enterPage;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnterPage() {
        return enterPage;
    }

    public void setEnterPage(String enterPage) {
        this.enterPage = enterPage;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
}

