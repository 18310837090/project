package com.ncit.module_01.UC01_02.controller;

import com.ncit.common.Constants;
import com.ncit.common.model.RemoteProperties;
import com.ncit.common.util.fileUtil;
import com.ncit.module_01.UC01_02.model.UC01_02_FileInfo;
import com.ncit.module_01.UC01_02.model.UC01_02_TrafficDataSearchInput;
import com.ncit.module_init.model.UC_Init_loginInput;
import com.ncit.module_init.model.UC_Init_userInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UC01_02Controller {
    @Resource
    RemoteProperties remoteProperties;

    /**
     * 查看
     */
    @RequestMapping(value = "/trafficDataSearch", method = RequestMethod.POST)
    public ModelAndView trafficDataSearch(@ModelAttribute(value = "uc01_02_TrafficDataSearchInput") UC01_02_TrafficDataSearchInput input, HttpServletRequest request) {
        ModelAndView result = new ModelAndView();
        HttpSession session = request.getSession();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            result.addObject("userInfo", new UC_Init_loginInput());
            result.setViewName(Constants.PAGE_MODULE_INIT_010);
            return result;
        }

        // 开始日期
        String beginDate = input.getBeginDate();
        // 结束日期
        String afterDate = input.getAfterDate();

        if (beginDate.equals("")){
            result.addObject("beginDate", beginDate);
            result.addObject("afterDate", afterDate);
            result.addObject("message", Constants.Message_Module_01_020_NG_002);
            result.setViewName(Constants.PAGE_MODULE_01_020);
            return result;
        }else if (afterDate.equals("")){
            result.addObject("beginDate", beginDate);
            result.addObject("afterDate", afterDate);
            result.addObject("message", Constants.Message_Module_01_020_NG_003);
            result.setViewName(Constants.PAGE_MODULE_01_020);
            return result;
        }else if (beginDate.compareTo(afterDate) > 0){
            result.addObject("beginDate", beginDate);
            result.addObject("afterDate", afterDate);
            result.addObject("message", Constants.Message_Module_01_020_NG_001);
            result.setViewName(Constants.PAGE_MODULE_01_020);
            return result;
        }

        // TODO 根据检索条件（开始日期、结束日期）遍历 根 文件夹
        List<UC01_02_FileInfo> infs = fileUtil.listDirInfos(remoteProperties.getRootDirectory());
        if (!infs.isEmpty()) {
            for (int i = infs.size() - 1; i >= 0; i--) {
                UC01_02_FileInfo inf = infs.get(i);
                if (StringUtils.equals("1", inf.getFile_folder_kbn())) {
                    if (StringUtils.isNotBlank(beginDate)) {
                        if(inf.getName().compareTo(beginDate) < 0) {
                            infs.remove(inf);
                        }
                    }

                    if (StringUtils.isNotBlank(afterDate)) {
                        if(inf.getName().compareTo(afterDate) > 0) {
                            infs.remove(inf);
                        }
                    }
                }else {
                    infs.remove(inf);
                }
            }
        }

        result.addObject("beginDate", beginDate);
        result.addObject("afterDate", afterDate);

        // 上一层文件夹
        result.addObject("parentFolder", remoteProperties.getRootDirectory());
        // 结果集合
        result.addObject("fileinfos", infs);
        result.setViewName(Constants.PAGE_MODULE_01_020);
        return result;
    }

    /**
     * 查看
     */
    @RequestMapping(value="/trafficDataSearchLink", method = RequestMethod.GET)
    public ModelAndView trafficDataSearchLink(@RequestParam("directory") String directory, @RequestParam("beginDate") String beginDate, @RequestParam("afterDate") String afterDate, HttpServletRequest request) {
        ModelAndView result = new ModelAndView();
        HttpSession session = request.getSession();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            result.addObject("userInfo", new UC_Init_loginInput());
            result.setViewName(Constants.PAGE_MODULE_INIT_010);
            return result;
        }

        List<UC01_02_FileInfo> infs ;
        // 上一层文件夹
        if (!StringUtils.equals(directory, remoteProperties.getRootDirectory()))  {

            // TODO 根据检索条件（开始日期、结束日期）遍历 根 文件夹
            infs = fileUtil.listDirInfos(directory);

            UC01_02_FileInfo parentInfo = new UC01_02_FileInfo();
            parentInfo.setName("↑ 返回上一层");
            parentInfo.setFile_folder_kbn("0");
            parentInfo.setAbsolute_path(fileUtil.parentFolder(directory));
            infs.add(0, parentInfo);
        } else {

            // TODO 根据检索条件（开始日期、结束日期）遍历 根 文件夹 (根据日期重新检索)
            infs = fileUtil.listDirInfos(directory);

            if (!infs.isEmpty()) {
                for (int i = infs.size() - 1; i >= 0; i--) {
                    UC01_02_FileInfo inf = infs.get(i);
                    if (StringUtils.equals("1", inf.getFile_folder_kbn())) {
                        if (StringUtils.isNotBlank(beginDate)) {
                            if(inf.getName().compareTo(beginDate) < 0) {
                                infs.remove(inf);
                            }
                        }

                        if (StringUtils.isNotBlank(afterDate)) {
                            if(inf.getName().compareTo(afterDate) > 0) {
                                infs.remove(inf);
                            }
                        }
                    }
                }
            }
        }

        // 开始日期
        result.addObject("beginDate", beginDate);
        // 结束日期
        result.addObject("afterDate", afterDate);
        // 结果集合
        result.addObject("fileinfos", infs);
        result.setViewName(Constants.PAGE_MODULE_01_020);

        return result;
    }

}
