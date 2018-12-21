package com.ncit.common.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(ignoreUnknownFields = false,ignoreInvalidFields = true)
@PropertySource(value = {"file:application.yml"},encoding="UTF-8")
@Component
public class RemoteProperties {
    private String ID;
    private String USERNAME;
    private String PASSWORD;
    private String XLSX_FILE_PATH;
    private String CSV_FILE_PATH;
    private String RESULTASSESS_ShellCommand;
    private String PATH_1;
    private String PATH_1_NEW;
    private String PATH_2;
    private String PATH_2_NEW;
    private String rootDirectory;
    private String rootLogDirectory;
    private String he_ShellCommand1;
    private String he_ShellCommand2;
    private String he_ShellCommand3;

    private String status;
    private String enterPage;

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getXLSX_FILE_PATH() {
        return XLSX_FILE_PATH;
    }

    public void setXLSX_FILE_PATH(String XLSX_FILE_PATH) {
        this.XLSX_FILE_PATH = XLSX_FILE_PATH;
    }

    public String getCSV_FILE_PATH() {
        return CSV_FILE_PATH;
    }

    public void setCSV_FILE_PATH(String CSV_FILE_PATH) {
        this.CSV_FILE_PATH = CSV_FILE_PATH;
    }

    public String getRESULTASSESS_ShellCommand() {
        return RESULTASSESS_ShellCommand;
    }

    public void setRESULTASSESS_ShellCommand(String RESULTASSESS_ShellCommand) {
        this.RESULTASSESS_ShellCommand = RESULTASSESS_ShellCommand;
    }

    public String getPATH_1() {
        return PATH_1;
    }

    public void setPATH_1(String PATH_1) {
        this.PATH_1 = PATH_1;
    }

    public String getPATH_1_NEW() {
        return PATH_1_NEW;
    }

    public void setPATH_1_NEW(String PATH_1_NEW) {
        this.PATH_1_NEW = PATH_1_NEW;
    }

    public String getPATH_2() {
        return PATH_2;
    }

    public void setPATH_2(String PATH_2) {
        this.PATH_2 = PATH_2;
    }

    public String getPATH_2_NEW() {
        return PATH_2_NEW;
    }

    public void setPATH_2_NEW(String PATH_2_NEW) {
        this.PATH_2_NEW = PATH_2_NEW;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public String getHe_ShellCommand1() {
        return he_ShellCommand1;
    }

    public void setHe_ShellCommand1(String he_ShellCommand1) {
        this.he_ShellCommand1 = he_ShellCommand1;
    }

    public String getHe_ShellCommand2() {
        return he_ShellCommand2;
    }

    public void setHe_ShellCommand2(String he_ShellCommand2) {
        this.he_ShellCommand2 = he_ShellCommand2;
    }

    public String getHe_ShellCommand3() {
        return he_ShellCommand3;
    }

    public void setHe_ShellCommand3(String he_ShellCommand3) {
        this.he_ShellCommand3 = he_ShellCommand3;
    }

    public String getEnterPage() {
        return enterPage;
    }

    public void setEnterPage(String enterPage) {
        this.enterPage = enterPage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRootLogDirectory() {
        return rootLogDirectory;
    }

    public void setRootLogDirectory(String rootLogDirectory) {
        this.rootLogDirectory = rootLogDirectory;
    }
}

