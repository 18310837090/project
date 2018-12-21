package com.ncit.module_01.UC01_01.controller;

import com.ncit.common.Constants;
import com.ncit.common.model.RemoteProperties;
import com.ncit.common.util.ZipUtil;
import com.ncit.module_init.model.UC_Init_loginInput;
import com.ncit.module_init.model.UC_Init_userInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;

@Controller
public class UC01_01Controller {
    @Resource
    RemoteProperties remoteProperties;

    @RequestMapping(value = "/trafficDataUploadInit", method = RequestMethod.GET)
    public ModelAndView trafficDataUploadInit(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView result = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            result.addObject("userInfo", new UC_Init_loginInput());
            result.setViewName(Constants.PAGE_MODULE_INIT_010);
            return result;
        }
        result.setViewName(Constants.PAGE_MODULE_01_010);
        return result;
    }


    @RequestMapping(value = "/trafficDataUpload", method = RequestMethod.POST)
    public ModelAndView trafficDataUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView result = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            result.addObject("userInfo", new UC_Init_loginInput());
            result.setViewName(Constants.PAGE_MODULE_INIT_010);
            return result;
        }
        try {
            // 上传文件
            // TODO 上传文件保存的临时文件夹　待定
            File tempFile = new File(remoteProperties.getRootDirectory()+ File.separator + file.getName()+".zip" );
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile));
            // 指定目录
            out.write(file.getBytes());
            out.flush();
            out.close();

            // TODO 解压缩文件
            // TODO 解压缩文件保存的文件夹　待定

            ZipUtil.unZip(tempFile, remoteProperties.getRootDirectory());
            //删除该临时压缩文件
            tempFile.delete();
            result.addObject("message", Constants.Message_Module_01_010_OK_001);

        } catch (Exception e) {
            result.addObject("message", Constants.Message_Module_01_010_NG_001);
        }

        result.setViewName(Constants.PAGE_MODULE_01_010);
        return result;
    }

}
