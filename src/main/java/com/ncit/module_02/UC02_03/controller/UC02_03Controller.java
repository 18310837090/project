package com.ncit.module_02.UC02_03.controller;

import com.ncit.common.Constants;
import com.ncit.common.model.RemoteProperties;
import com.ncit.common.util.fileUtil;
import com.ncit.common.util.readLogUtil;
import com.ncit.module_02.UC02_03.model.UC02_03_FileInfo;
import com.ncit.module_init.model.UC_Init_loginInput;
import com.ncit.module_init.model.UC_Init_userInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class UC02_03Controller {
    @Resource
    RemoteProperties remoteProperties;

    @RequestMapping(value = "/logReviewInit", method = RequestMethod.GET)
    public ModelAndView logReviewInit(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView result = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            result.addObject("userInfo", new UC_Init_loginInput());
            result.setViewName(Constants.PAGE_MODULE_INIT_010);
        }else {
        // 遍历 LOG 文件夹
        List<UC02_03_FileInfo> infs = fileUtil.listDirLogInfos(remoteProperties.getRootLogDirectory()+"/"+Constants.UC02_02_LOG);
        result.addObject("fileinfos", infs);
        result.setViewName(Constants.PAGE_MODULE_02_030);
        }
        return result;
    }

    /**
     * 查看
     */
    @RequestMapping(value="/getLogInfoLink", method = RequestMethod.POST)
    public String getLogInfoLink(@RequestParam("directory") String directory) {
        return readLogUtil.readLogFile(directory);
    }

}
