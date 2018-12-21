package com.ncit.common.util;

import org.springframework.stereotype.Component;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jing.xiao on 2018/11/10.
 */
@Component
public class PasswordencryptUtil {

    /**
     * 字段进行MD5加密
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String eccrypt(String data) throws NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        MessageDigest mds;
        mds = MessageDigest.getInstance("MD5");
        mds.update(data.getBytes());
        for (byte b :mds.digest()) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    //参数为要修改的文件路径 以及要修改的属性名和属性值
    public static void changePW(String dbFilePath,String key,String value) throws IOException {
        File file ;
        FileOutputStream out = null;
        BufferedReader br = null;
        StringBuffer buf = new StringBuffer();
        String line;
        try {
            file = new File(dbFilePath);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            //通过类进行获取properties文件流
            while ((line = br.readLine())!=null){
                if (line.contains(key)){
                    buf = buf.append("  "+key+": "+value+"\r\n");
                }else {
                    buf = buf.append(line+"\r\n");
                }
            }
            out = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(out);
            pw.write(buf.toString().toCharArray());
            pw.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (br != null){
                br.close();
            }
            if (out != null){
                out.close();
            }
        }
    }


}
