package com.ncit.common.util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


 /**
  * 压缩算法类
  * 实现文件压缩，文件夹压缩，以及文件和文件夹的混合压缩
  * @author ljheee
  */
 public class TransportFile {
     /**
       * 完成的结果文件--输出的压缩文件
       */
     File targetFile;
     public TransportFile() {}

     public TransportFile(File target) {
         targetFile = target;
         if (targetFile.exists())
             targetFile.delete();
     }


     /** 压缩文件
       *
       * @param srcfile
       */
     public void zipFiles(File srcfile) {
         ZipOutputStream out = null;
         try {
             out = new ZipOutputStream(new FileOutputStream(targetFile));
             if(srcfile.isFile()){
                 zipFile(srcfile, out, "");
             } else{
                 File[] list = srcfile.listFiles();
                 for (int i = 0; i < list.length; i++) {
                     compress(list[i], out, "");
                 }
             }
             System.out.println("压缩完毕");
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             try {
                 if (out != null) out.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }


     /**
       * 压缩文件夹里的文件
       * 起初不知道是文件还是文件夹---统一调用该方法
       * @param file
       * @param out
       * @param basedir
       */
     private void compress(File file, ZipOutputStream out, String basedir) {
         /* 判断是目录还是文件 */
         if (file.isDirectory()) {
             this.zipDirectory(file, out, basedir);
         } else {
             this.zipFile(file, out, basedir);
         }
     }


     /**
       * 压缩单个文件
       *
       * @param srcfile
       */
     public void zipFile(File srcfile, ZipOutputStream out, String basedir) {
         if (!srcfile.exists())
             return;
         byte[] buf = new byte[1024];
         FileInputStream in = null;
         try {
             int len; in = new FileInputStream(srcfile);
             out.putNextEntry(new ZipEntry(basedir + srcfile.getName()));
             while ((len = in.read(buf)) > 0) { out.write(buf, 0, len);
             }
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             try {
                 if (out != null) out.closeEntry();
                 if (in != null) in.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }
     /** 压缩文件夹
      *
      * @param dir
      * @param out
      * @param basedir
      */
     public void zipDirectory(File dir, ZipOutputStream out, String basedir) {
         if (!dir.exists()) return;
         File[] files = dir.listFiles();
         for (int i = 0; i < files.length; i++) {
             /* 递归 */
             compress(files[i], out, basedir + dir.getName() + "/");
         }
     }

     /**
       * 将多个Excel打包成zip文件
       * @param srcfile
       * @param zipfile
       */
         public void zipFiles(List<File> srcfile, File zipfile) {
             byte[] buf = new byte[1024];
             try {
                 // Create the ZIP file
                 ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
                 // Compress the files
                 for (int i = 0; i < srcfile.size(); i++) {
                     File file = srcfile.get(i);
                     FileInputStream in = new FileInputStream(file);
                     // Add ZIP entry to output stream.
                     out.putNextEntry(new ZipEntry(file.getName()));

                     // Transfer bytes from the file to the ZIP file
                     int len;
                     while ((len = in.read(buf)) > 0) {
                         out.write(buf, 0, len);
                     }
                     // Complete the entry
                     out.closeEntry();
                     in.close();
                 }
                 // Complete the ZIP file
                 out.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     /**
      * 将多个Excel打包成zip文件
      * @param fileName
      * @param zipfile
      */
     public void zipDirFiles(String baseDir,String[] fileName, File zipfile) {
         byte[] buf = new byte[1024];
         try {
             // Create the ZIP file
             ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
             // Compress the files
             for (int i = 0; i < fileName.length; i++) {
                 String fileFullPath = baseDir+"/"+fileName[i];
                 FileInputStream in = new FileInputStream(new File(fileFullPath));
                 // Add ZIP entry to output stream.
                 out.putNextEntry(new ZipEntry(fileName[i]));
                 // Transfer bytes from the file to the ZIP file
                 int len;
                 while ((len = in.read(buf)) > 0) {
                     out.write(buf, 0, len);
                 }
                 // Complete the entry
                 out.closeEntry();
                 in.close();
             }
             // Complete the ZIP file
             out.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }


     public void downFile(HttpServletResponse response,File tempFile) {
         String fileDownLoad = tempFile.toString();
         String fileDisplay = tempFile.getName();
         OutputStream outputStream = null;
         FileInputStream inputStream = null;
         try {
             //设置文件的格式
             fileDisplay = URLEncoder.encode(fileDisplay, "UTF-8");
             response.addHeader("Content-Disposition", "attachment;filename=" + fileDisplay);
             outputStream = response.getOutputStream();
             inputStream = new FileInputStream(fileDownLoad);
             byte[] b = new byte[1024];
             int i;
             while ((i = inputStream.read(b)) > 0) {
                 outputStream.write(b, 0, i);
             }
             outputStream.flush();
         } catch (Exception e) {
             e.printStackTrace();
         }finally {
             if (inputStream != null) {
                 try {
                     inputStream.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
             if (outputStream != null) {
                 try {
                     outputStream.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }

     }

}
