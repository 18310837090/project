package com.ncit.module_03.UC03_01.service;

import com.ncit.module_03.UC03_01.model.UC03_01_Output;
import javax.servlet.http.HttpServletResponse;

public interface UC03_01_service {
    UC03_01_Output listDir(String path);

    void download(HttpServletResponse response, String CSVFileNameList);
}
