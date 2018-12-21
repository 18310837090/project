package com.ncit.module_02.UC02_03.model;

/**
 *
 */
public class UC02_03_FileInfo {

    /**
     * 文件名
     */
    public String name;

    /**
     * 路径
     */
    public String absolute_path;

    /**
     * 文件 文件夹 区分 1:文件 2:文件夹
     */
    public String file_folder_kbn;


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

    public String getFile_folder_kbn() {
        return file_folder_kbn;
    }

    public void setFile_folder_kbn(String _file_folder_kbn) {
        this.file_folder_kbn = _file_folder_kbn;
    }

}
