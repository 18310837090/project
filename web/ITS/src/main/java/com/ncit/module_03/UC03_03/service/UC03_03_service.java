package com.ncit.module_03.UC03_03.service;

import com.ncit.module_03.UC03_03.model.UC03_03_Output;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public interface UC03_03_service {
    ArrayList listDir(String path);

    UC03_03_Output fileDetails(String fullPath);

    void download(HttpServletResponse response,String xlsxFileNameList);
}
