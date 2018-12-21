package com.ncit.module_03.UC03_03.controller;

import com.ncit.common.Constants;
import com.ncit.common.model.RemoteProperties;
import com.ncit.module_03.UC03_03.model.UC03_03_Output;
import com.ncit.module_03.UC03_03.service.UC03_03_service;
import com.ncit.module_init.model.UC_Init_loginInput;
import com.ncit.module_init.model.UC_Init_userInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class UC03_03Controller {
    @Resource
    UC03_03_service uc03_03_service;
    @Resource
    RemoteProperties remoteProperties;
    UC03_03_Output uc03_03_output;

    @RequestMapping(value = "/assessResultDownload", method = RequestMethod.GET)
    public ModelAndView assessResultDownload(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            modelAndView.addObject("userInfo", new UC_Init_loginInput());
            modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
        } else {
            ArrayList arrayList = uc03_03_service.listDir(remoteProperties.getXLSX_FILE_PATH());
            session.setAttribute("fileNameList", arrayList);
            modelAndView.addObject("fileNameList", arrayList);
            modelAndView.setViewName(Constants.PAGE_MODULE_03_030);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/searchXLSXFileByFileName", method = RequestMethod.GET)
    public ModelAndView searchFileByFileName(@ModelAttribute(value = "filename") String xlsxFileName, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            modelAndView.addObject("userInfo", new UC_Init_loginInput());
            modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
        } else {
        //读取excel文件，并将内容返回
        uc03_03_output = uc03_03_service.fileDetails(remoteProperties.getXLSX_FILE_PATH() + "/" + xlsxFileName);
        modelAndView.addObject("result", uc03_03_output);
        modelAndView.addObject("fileNameList", session.getAttribute("fileNameList"));
        modelAndView.setViewName(Constants.PAGE_MODULE_03_030);
        }
        return modelAndView;
    }

    @RequestMapping(value = "assessResultDownloadByFileName" , method = RequestMethod.GET)
    public void assessResultDownloadByFileName(@ModelAttribute(value = "fileNameList") String xlsxFileNameList,HttpServletResponse response) {
        uc03_03_service.download(response,xlsxFileNameList);
    }
}
