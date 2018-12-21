package com.ncit.module_03.UC03_03.service;

import com.ncit.common.Constants;
import com.ncit.common.model.RemoteProperties;
import com.ncit.module_03.UC03_03.model.xlsxProperties;
import com.ncit.common.util.TransportFile;
import com.ncit.module_03.UC03_03.model.UC03_03_Output;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jing.xiao  on 2018/10/29
 */
@Service
public class UC03_03_serviceImp implements UC03_03_service {
    @Resource
    RemoteProperties remoteProperties;
    //    TransportFile transportFile;
    /*
     *遍历文件，获取文件名
     */
    @Override
    public  ArrayList listDir(String path){
        ArrayList arrayList = new ArrayList();
        File file = new File(path);
        if (file.exists()){
            File[] files = file.listFiles();
            if (files == null || files.length == 0){
                return arrayList;
            }else {
                for (File file2:files){
                    if (file2.isDirectory()){
                        listDir(file2.getAbsolutePath());
                    }else {
                        arrayList.add(file2.getName());
                    }
                }
            }
        }
        return arrayList;
    }


    /*
     *读取xlsx文件，获取文件内容
     */
    @Override
    public  UC03_03_Output fileDetails(String fullPath) {
        UC03_03_Output uc03_03_Output = new UC03_03_Output();
        Map map = new HashMap();
        ArrayList arrayList = new ArrayList();
        xlsxProperties xp = new xlsxProperties();
        Workbook wb;
        try {
            File excel = new File(fullPath);
            if (excel.isFile() && excel.exists()) {
                //判断文件是否存在
                String[] split = excel.getName().split("\\.");
                // .是特殊字符，需要转义！！！！！
                //根据文件后缀（xls/xlsx）进行判断
                if ( "xls".equals(split[1])){
                    FileInputStream fis = new FileInputStream(excel);
                    wb = new HSSFWorkbook(fis);
                }else if ("xlsx".equals(split[1])){
                    wb = new XSSFWorkbook(excel);
                }else {
                    uc03_03_Output.setMessageId(Constants.Message_Module_03_030_NG_002);
                    return uc03_03_Output;
                }
                //开始解析
                Sheet sheet = wb.getSheetAt(0);
                //读取sheet
                for (int i = 1; i <= sheet.getLastRowNum(); i++){
                    if (i%4==2){
                        xp = new xlsxProperties();
                    }
                    for (int j = 1;j< 5;j++){
                        if (sheet.getRow(i) == null){
                            sheet.createRow(i);
                        }
                        //第一行
                        if (i==1&&j==1){
                            //设置日期
                            map.put("date",sheet.getRow(i).getCell(j).toString());
                        }
                        //第二行
                        if ((i%4==2)){
                            if (j==1){
                                //设置时间
                                xp.setTime(sheet.getRow(i).getCell(j).toString());
                            }
                        }
                        //第三行
                        if (i%4==3){
                            if (j==2){xp.setValue1(Float.parseFloat(sheet.getRow(i).getCell(j).toString()));}
                            if (j==3){xp.setValue2(Float.parseFloat(sheet.getRow(i).getCell(j).toString()));}
                            if (j==4){
                                float a = (xp.getValue1()-xp.getValue2())/xp.getValue1();
                                //设置第一行第三个值
                                xp.setPeriod1(a);
                            }
                        }
                        //第四行
                        if (i%4==0){
                            if (j==2){xp.setValue3(Float.parseFloat(sheet.getRow(i).getCell(j).toString()));}
                            if (j==3){xp.setValue4(Float.parseFloat(sheet.getRow(i).getCell(j).toString()));}
                            if (j==4){
                                float a = (xp.getValue3()-xp.getValue4())/xp.getValue3();
                                //设置第二行第三个值
                                xp.setPeriod2(a);
                                arrayList.add(xp);
                            }
                        }
                    }
                }
                map.put("xlsxProperties",arrayList);
                uc03_03_Output.setResult(map);
                uc03_03_Output.setMessageId(Constants.Message_Module_03_030_OK_001);

            } else {
                uc03_03_Output.setMessageId(Constants.Message_Module_03_030_NG_001);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uc03_03_Output;
    }

    /**
     * 将服务器端生成的CSV文件提供给客户端下载
     * @param response
     * @param xlsxFileNameList
     * @return
     */
    @Override
    public void download(HttpServletResponse response, String xlsxFileNameList){
        TransportFile transportFile = new TransportFile();

        String[] fileName = xlsxFileNameList.split(",");
        File file;
        String fileFullPath;
        if (fileName.length > 1){
            //大于一个文件时采用zip压缩包下载方式
            ArrayList<File> arrayList = new ArrayList<File>();
            for (int index =0;index<fileName.length;index++){
                fileFullPath = remoteProperties.getXLSX_FILE_PATH() + "/" + fileName[index];
                file = new File(fileFullPath);
                arrayList.add(file);
            }
            File zipFile = new File(remoteProperties.getXLSX_FILE_PATH()+".zip");
            transportFile.zipFiles(arrayList,zipFile);
            transportFile.downFile(response,zipFile);
        }else if (fileName.length == 1){
            //只有一个文件时，采用单个文件下载方式
            fileFullPath = remoteProperties.getXLSX_FILE_PATH() + "/" + fileName[0];
            file = new File(fileFullPath);
            transportFile.downFile(response,file);
        }
    }
}