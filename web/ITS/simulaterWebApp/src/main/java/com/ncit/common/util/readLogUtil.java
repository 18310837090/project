package com.ncit.common.util;

import java.io.*;

public class readLogUtil {
    //读取log日志之类的文件
    public static String readLogFile(String path){
        BufferedReader br = null;
        String line;
        StringBuilder log = new StringBuilder();
        try {
            File file = new File(path);
            if (!file.exists()){
                file.createNewFile();
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((line = br.readLine())!= null){
                log.append(line).append("<br/>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeStream(br);
        }
        return log.toString();
    }

    //关闭流方法
    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
                e.printStackTrace();
            }
        }
    }
}
