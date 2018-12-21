package com.ncit.common.util;

import com.ncit.module_01.UC01_02.model.UC01_02_FileInfo;
import com.ncit.module_02.UC02_03.model.UC02_03_FileInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class fileUtil {
    /*
     *遍历文件，获取文件名
     */
    public static ArrayList listDir(String path){
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
                        arrayList.add(file2.getAbsoluteFile());
                    }
                }
            }
        }
        return arrayList;
    }

    /*
     *遍历文件夹，获取文件名、文件夹名
     */
    public static List<UC01_02_FileInfo> listDirInfos(String _path) {
        List<UC01_02_FileInfo> result = new ArrayList();

        File file = new File(_path);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File itemFile : files) {
                UC01_02_FileInfo inf = new UC01_02_FileInfo();
                // 名称
                inf.setName(itemFile.getName());
                // 路径
                inf.setAbsolute_path(itemFile.getAbsolutePath());

                if (itemFile.isFile()) {
                    if (itemFile.isHidden()){
                        continue;
                    }
                    // 文件大小
                    BigDecimal b1 = new BigDecimal(itemFile.length());
                    BigDecimal b2 = new BigDecimal(1024);
                    inf.setFile_size(String.valueOf(b1.divide(b2, 0, BigDecimal.ROUND_HALF_UP)   ) + "KB");

                    // 记录数
                    inf.setRecord_count(String.valueOf(getRecordCount(itemFile)));
                    // 区分
                    inf.setFile_folder_kbn("2");
                } else {
                    // 区分
                    inf.setFile_folder_kbn("1");
                }

                result.add(inf);
            }
        }
        return result;
    }

    /*
     *遍历文件夹，获取文件名、文件夹名
     */
    public static List<UC02_03_FileInfo> listDirLogInfos(String _path) {
        List<UC02_03_FileInfo> result = new ArrayList();
        File file = new File(_path);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File itemFile : files) {
                UC02_03_FileInfo inf = new UC02_03_FileInfo();

                if (itemFile.isFile()) {
                    //排除文件名叫log1,log2,log4的文件
                    if (itemFile.getName().equals("log1")||itemFile.getName().equals("log2")||itemFile.getName().equals("log4")){
                        continue;
                    }
                    // 名称
                    inf.setName(itemFile.getName());
                    // 路径
                    inf.setAbsolute_path(itemFile.getAbsolutePath());
                    // 区分
                    inf.setFile_folder_kbn("2");
                }
                result.add(inf);
            }
        }
        return result;
    }

    public  static String parentFolder(String _path) {
        String result = "";

        File file = new File(_path);
        if (file.exists()) {
            result = file.getParent();
        } else {
            System.out.println("文件不存在");
        }

        return result;
    }

    /*
     *获取文件的记录数
     */
    public static long getRecordCount(File _file) {
        long linenumber = 0;

        try{
            if(_file.exists()){
                FileReader fr = new FileReader(_file);
                LineNumberReader lnr = new LineNumberReader(fr);
                while (lnr.readLine() != null){
                    linenumber++;
                }
                lnr.close();
            }else{
                System.out.println("File does not exists!");
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        return linenumber;
    }

    /*
     * 删除文件
     */
    public static boolean deleteFile(File _file) {
        if (_file == null) {
            return false;
        } else {
            if (_file.exists()) {
                return _file.delete();
            } else {
                return false;
            }
        }
    }

    public static void existDirectory(String path){
        File _file = new File(path);
        if (!_file.exists()){
            _file.mkdirs();
        }
    }

    /*
     * 创建文件，有则删除并新建
     */
    public static void CreateAndDeleteFile(String path) {
        try {
            File _file = new File(path);
            if (_file.exists()) {
                _file.delete();
            }else {
                _file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 拷贝文件
     */
    public static boolean copyFile(String _srcFileName, String _destFileName, boolean _overlay) {

        if (StringUtils.isBlank(_srcFileName) || StringUtils.isBlank(_destFileName)) {
            return false;
        }

        File srcFile = new File(_srcFileName);
        if ((!srcFile.exists()) || (!srcFile.isFile())) {
            return false;
        }

        File destFile = new File(_destFileName);
        if (destFile.exists()) {
            if (_overlay) {
                new File(_destFileName).delete();
            }
        }

        int byteread ;
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            while((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
        } catch (IOException e) {
            File fileDelete = new File(_destFileName);
            if (fileDelete.exists()) {
                fileDelete.delete();
            }
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /*
     * 移动文件
     */
    public static boolean moveFile(String _srcFileName, String _destFileName, boolean _overlay) {
        if (copyFile(_srcFileName, _destFileName, _overlay)) {
            File srcFile = new File(_srcFileName);
            if (srcFile.exists()) {
                srcFile.delete();
            }
            return true;
        } else {
            return false;
        }
    }
}
