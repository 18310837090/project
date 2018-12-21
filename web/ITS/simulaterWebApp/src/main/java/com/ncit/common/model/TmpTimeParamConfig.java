package com.ncit.common.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(ignoreUnknownFields = false,ignoreInvalidFields = true)
@PropertySource(value = {"file:temp_file/tmp.yml"},encoding="UTF-8")
@Component
public class TmpTimeParamConfig {
    private String uc01_03_beginDate;
    private String uc01_03_afterDate;

    private String uc02_02_beginDate;
    private String uc02_02_afterDate;
    private String uc02_02_beginTime;
    private String uc02_02_afterTime;

    private String uc03_02_date;
    private String uc03_02_timeFrom;
    private String uc03_02_timeTo;


    public String getUc01_03_beginDate() {
        return uc01_03_beginDate;
    }

    public void setUc01_03_beginDate(String uc01_03_beginDate) {
        this.uc01_03_beginDate = uc01_03_beginDate;
    }

    public String getUc01_03_afterDate() {
        return uc01_03_afterDate;
    }

    public void setUc01_03_afterDate(String uc01_03_afterDate) {
        this.uc01_03_afterDate = uc01_03_afterDate;
    }

    public String getUc02_02_beginDate() {
        return uc02_02_beginDate;
    }

    public void setUc02_02_beginDate(String uc02_02_beginDate) {
        this.uc02_02_beginDate = uc02_02_beginDate;
    }

    public String getUc02_02_afterDate() {
        return uc02_02_afterDate;
    }

    public void setUc02_02_afterDate(String uc02_02_afterDate) {
        this.uc02_02_afterDate = uc02_02_afterDate;
    }

    public String getUc02_02_beginTime() {
        return uc02_02_beginTime;
    }

    public void setUc02_02_beginTime(String uc02_02_beginTime) {
        this.uc02_02_beginTime = uc02_02_beginTime;
    }

    public String getUc02_02_afterTime() {
        return uc02_02_afterTime;
    }

    public void setUc02_02_afterTime(String uc02_02_afterTime) {
        this.uc02_02_afterTime = uc02_02_afterTime;
    }

    public String getUc03_02_date() {
        return uc03_02_date;
    }

    public void setUc03_02_date(String uc03_02_date) {
        this.uc03_02_date = uc03_02_date;
    }

    public String getUc03_02_timeFrom() {
        return uc03_02_timeFrom;
    }

    public void setUc03_02_timeFrom(String uc03_02_timeFrom) {
        this.uc03_02_timeFrom = uc03_02_timeFrom;
    }

    public String getUc03_02_timeTo() {
        return uc03_02_timeTo;
    }

    public void setUc03_02_timeTo(String uc03_02_timeTo) {
        this.uc03_02_timeTo = uc03_02_timeTo;
    }
}

