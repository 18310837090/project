package com.ncit.module_03.UC03_01.service;

import com.ncit.common.Constants;
import com.ncit.common.model.RemoteProperties;
import com.ncit.common.util.TransportFile;
import com.ncit.module_03.UC03_01.model.UC03_01_Output;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jing.xiao  on 2018/11/12
 */
@Service
public class UC03_01_serviceImp implements UC03_01_service {
    @Resource
    RemoteProperties remoteProperties;
    /*
     *遍历文件，获取文件夹与文件名
     */
    @Override
    public  UC03_01_Output listDir(String path){
        UC03_01_Output uc03_01_output = new UC03_01_Output();
        Map map = new HashMap();
        Map map_dir = new HashMap();
        File file = new File(path);
        File[] dirs = file.listFiles();
        if (dirs == null || dirs.length == 0){
            map.put("dirs",null);
            uc03_01_output.setResult(map);
            uc03_01_output.setMessageId(Constants.Message_Module_03_010_NG_001);
            return uc03_01_output;
        }else {
            for (int i = 0;i < dirs.length; i++){
                ArrayList arrayList_file = new ArrayList();
                File[] files = dirs[i].listFiles();
                if (files == null){
                    continue;
                }
                for (int j =0;j< files.length;j++){
                    arrayList_file.add(files[j].getName());
                }
                map_dir.put(dirs[i].getName(),arrayList_file);
            }
            map.put("dirs",map_dir);
            uc03_01_output.setResult(map);
            uc03_01_output.setMessageId(Constants.Message_Module_03_010_OK_001);
        }
        return uc03_01_output;
    }

    /**
     * 将服务器端生成的CSV文件提供给客户端下载
     * @param response
     * @param CSVFileNameList
     * @return
     */
    @Override
    public void download(HttpServletResponse response, String CSVFileNameList){
        TransportFile transportFile = new TransportFile();
        String[] fileName = CSVFileNameList.split(",");
        File file;
        String fileFullPath;
        if (fileName.length > 1){
            //大于一个文件时采用zip压缩包下载方式
            File zipFile = new File(remoteProperties.getCSV_FILE_PATH()+".zip");
            transportFile.zipDirFiles(remoteProperties.getCSV_FILE_PATH(),fileName,zipFile);
            transportFile.downFile(response,zipFile);
        }else if (fileName.length == 1){
            //只有一个文件时，采用单个文件下载方式
            fileFullPath = remoteProperties.getCSV_FILE_PATH() + "/" + fileName[0];
            file = new File(fileFullPath);
            transportFile.downFile(response,file);
        }
    }
}