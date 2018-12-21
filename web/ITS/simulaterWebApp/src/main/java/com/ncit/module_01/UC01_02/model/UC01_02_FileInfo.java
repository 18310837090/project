package com.ncit.module_01.UC01_02.model;

/**
 *
 */
public class UC01_02_FileInfo {

    /**
     * 文件名
     */
    private String name;

    /**
     * 路径
     */
    private String absolute_path;

    /**
     * 文件大小
     */
    private String file_size;

    /**
     * 记录数
     */
    private String record_count;

    /**
     * 文件 文件夹 区分 1:文件夹 2:文件
     */
    private String file_folder_kbn;


    public String getName() {
        return name;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public String getAbsolute_path() {
        return absolute_path;
    }

    public void setAbsolute_path(String _absolute_path) {
        this.absolute_path = _absolute_path;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String _file_size) {
        this.file_size = _file_size;
    }

    public String getRecord_count() {
        return record_count;
    }

    public void setRecord_count(String _record_count) {
        this.record_count = _record_count;
    }

    public String getFile_folder_kbn() {
        return file_folder_kbn;
    }

    public void setFile_folder_kbn(String _file_folder_kbn) {
        this.file_folder_kbn = _file_folder_kbn;
    }

}
