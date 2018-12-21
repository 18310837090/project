package com.ncit.module_01.UC01_02.model;

public class UC01_02_TrafficDataSearchInput {

    /**
     * 检索条件 开始日期
     */
    private String  beginDate;

    /**
     * 检索条件 结束日期
     */
    private String  afterDate;

    /**
     * 路径
     */
    private String directory;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String _beginDate) {
        this.beginDate = _beginDate;
    }

    public String getAfterDate() {
        return afterDate;
    }

    public void setAfterDate(String _afterDate) {
        this.afterDate = _afterDate;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String _directory) {
        this.directory = _directory;
    }
}
