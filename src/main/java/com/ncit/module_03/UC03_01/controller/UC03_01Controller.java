package com.ncit.module_03.UC03_01.controller;

import com.ncit.common.Constants;
import com.ncit.common.model.RemoteProperties;
import com.ncit.common.util.readLogUtil;
import com.ncit.module_03.UC03_01.model.UC03_01_Output;
import com.ncit.module_03.UC03_01.service.UC03_01_service;
import com.ncit.module_init.model.UC_Init_loginInput;
import com.ncit.module_init.model.UC_Init_userInfo;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class UC03_01Controller {
    @Resource
    UC03_01_service uc03_01_service;
    @Resource
    RemoteProperties remoteProperties;

    //页面初始化
    @RequestMapping(value = "/timingResultDownload", method = RequestMethod.GET)
    public ModelAndView timingResultDownload(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            modelAndView.addObject("userInfo", new UC_Init_loginInput());
            modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
        } else {
            UC03_01_Output uc03_01_output = uc03_01_service.listDir(remoteProperties.getCSV_FILE_PATH());
            modelAndView.addObject("result", uc03_01_output);
            session.setAttribute("result",uc03_01_output);
            modelAndView.setViewName(Constants.PAGE_MODULE_03_010);
        }
        return modelAndView;
    }
    //查看文件内容
    @RequestMapping(value = "/searchCSVFileByFileName", method = RequestMethod.POST)
    public String searchFileByFileName(@ModelAttribute(value = "filename") String CSVFileName, HttpServletRequest request) {
        //读取excel文件，并将内容返回
        String CSVFileInfo = readLogUtil.readLogFile(remoteProperties.getCSV_FILE_PATH() + "/" + CSVFileName);
        return CSVFileInfo;
    }

    //下载文件
    @RequestMapping(value = "timingResultDownloadByFileName" , method = RequestMethod.GET)
    public void timingResultDownloadByFileName(@ModelAttribute(value = "fileNameList") String CSVFileNameList, HttpServletResponse response) {
        uc03_01_service.download(response,CSVFileNameList);
    }
}
