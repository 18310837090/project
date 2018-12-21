package com.ncit.module_init.controller;

import com.ncit.common.SessionListener;
import com.ncit.common.Constants;
import com.ncit.common.model.RemoteProperties;
import com.ncit.common.util.GetTimeUtil;
import com.ncit.module_01.UC01_03.service.UC01_03_service;
import com.ncit.module_02.UC02_02.service.UC02_02_service;
import com.ncit.module_03.UC03_02.service.UC03_02_service;
import com.ncit.module_04.model.UC_changePasswordInput;
import com.ncit.module_init.model.UC_Init_loginInput;
import com.ncit.module_init.model.UC_Init_loginOutput;
import com.ncit.module_init.model.UC_Init_userInfo;
import com.ncit.module_init.service.UC_Init_service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class InitController {
    @Resource
    UC_Init_service uc_init_service;

    @Resource
    UC02_02_service uc02_02_service;

    @Resource
    UC03_02_service uc03_02_service;

    @Resource
    UC01_03_service uc01_03_service;

    @Resource
    RemoteProperties remoteProperties;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            modelAndView.addObject("userInfo", new UC_Init_loginInput());
            modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
        } else {
            modelAndView.addObject("userInfo", userInfo);
            modelAndView.setViewName(Constants.PAGE_MODULE_INIT_020);
        }
        return modelAndView;
    }

    @RequestMapping(value="/login",method = RequestMethod.POST)
    public ModelAndView login(@ModelAttribute(value = "uc01_010_loginInput") UC_Init_loginInput uc_Init_loginInput, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        UC_Init_loginOutput result = uc_init_service.login(uc_Init_loginInput);
        switch (result.getMessageId()) {
            case Constants.Message_module_INIT_OK_001:
                HttpSession session = request.getSession();
                if (session != null) {
                    // 检查当前用户是否已登录
                    HttpSession alreadyLoginSession = SessionListener.checkIsLogin(result.getUserInfo().getUserId());

                    if (alreadyLoginSession != null) {
                        // 已登录时，踢掉上一个登录的session，将当前登录信息保存
                        SessionListener.removeLogonUser(alreadyLoginSession);
                        SessionListener.addLogonUser(session, result.getUserInfo().getUserId());
                    } else {
                        // 未登录时将当前登录信息保存
                        SessionListener.addLogonUser(session, result.getUserInfo().getUserId());
                    }
                    session.setAttribute("userInfo", result.getUserInfo());
                    session.setAttribute("active", Constants.USER_ACTIVE);
                }

                modelAndView.addObject("userInfo", result.getUserInfo());
                modelAndView.addObject("enterPage", remoteProperties.getEnterPage());
                modelAndView.setViewName(Constants.PAGE_MODULE_INIT_020);
                break;
//            case Constants.Message_module_INIT_NG_001:
//                modelAndView.addObject("message", Constants.Message_module_INIT_NG_001);
//                modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
//                break;
            case Constants.Message_module_INIT_NG_002:
                modelAndView.addObject("message",Constants.Message_module_INIT_NG_002);
                modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
                break;
//            case Constants.Message_module_INIT_NG_003:
//                modelAndView.addObject("message", Constants.Message_module_INIT_NG_003);
//                modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
//                break;
            case Constants.Message_module_INIT_NG_004:
                modelAndView.addObject("message", Constants.Message_module_INIT_NG_004);
                modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
                break;
            case Constants.Message_module_INIT_NG_005:
                modelAndView.addObject("message", Constants.Message_module_INIT_NG_005);
                modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
                break;
            case Constants.Message_module_INIT_NG_006:
                modelAndView.addObject("message", Constants.Message_module_INIT_NG_006);
                modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
                break;
            default:
                modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
                break;
        }
        return modelAndView;
    }

    //注销账户
    @RequestMapping(value = { "/","/logout"}, method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        //用户登录信息不存在
        if (userInfo == null) {
            modelAndView.addObject("uc_Init_loginInput", new UC_Init_loginInput());
        } else {
            SessionListener.removeLogonUser(request.getSession());
        }
        modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
        return modelAndView;
    }

    /**
     * 画面初始化
     */
    @RequestMapping(value = "/trafficDataSearchInit", method = RequestMethod.GET)
    public ModelAndView trafficDataSearchInit(HttpServletRequest request) {
        ModelAndView result = new ModelAndView();
        HttpSession session = request.getSession();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            result.addObject("userInfo", new UC_Init_loginInput());
            result.setViewName(Constants.PAGE_MODULE_INIT_010);
            return result;
        }
        // 开始日期
        String beginDate = GetTimeUtil.getNowDate();
        // 结束日期
        String afterDate = beginDate;
        result.addObject("beginDate", beginDate);
        result.addObject("afterDate", afterDate);
        result.setViewName(Constants.PAGE_MODULE_01_020);
        return result;
    }

    @RequestMapping(value = "/assessResult", method = RequestMethod.GET)
    public ModelAndView assessResult(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            modelAndView.addObject("userInfo", new UC_Init_loginInput());
            modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
        } else {
            Map map = uc03_02_service.init();
            modelAndView.addObject("beginDate", map.get("beginDate"));
            modelAndView.addObject("timeFrom", map.get("timeFrom"));
            modelAndView.addObject("timeTo", map.get("timeTo"));
            modelAndView.addObject("log1",map.get("log1"));
            modelAndView.addObject("log2",map.get("log2"));
            modelAndView.addObject("status", map.get("status"));
            modelAndView.setViewName(Constants.PAGE_MODULE_03_020);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/databaseImportInit", method = RequestMethod.GET)
    public ModelAndView databaseImportInit(HttpServletRequest request) {
        ModelAndView result = new ModelAndView();
        HttpSession session = request.getSession();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            result.addObject("userInfo", new UC_Init_loginInput());
            result.setViewName(Constants.PAGE_MODULE_INIT_010);
            return result;
        }
        Map map = uc01_03_service.init();
        result.addObject("status", map.get("status"));
        result.addObject("beginDate", map.get("beginDate"));
        result.addObject("afterDate", map.get("afterDate"));
        result.addObject("log1",map.get("log1"));
        result.addObject("log2",map.get("log2"));
        result.setViewName(Constants.PAGE_MODULE_01_030);
        return result;
    }

    @RequestMapping(value = "/systemStartInit", method = RequestMethod.GET)
    public ModelAndView systemStartInit(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView result = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            result.addObject("userInfo", new UC_Init_loginInput());
            result.setViewName(Constants.PAGE_MODULE_INIT_010);
            return result;
        }
        Map map = uc02_02_service.init();
        // 世代数
        result.addObject("generation_max", map.get("generation_max"));
        // 人口数
        result.addObject("population_size", map.get("population_size"));
        // 人口择优范围
        result.addObject("tournament_size", map.get("tournament_size"));
        // 模拟时间
        result.addObject("sim_duration", map.get("sim_duration"));
        // 评价指标
        result.addObject("goal", map.get("goal"));
        // mas 个数
        result.addObject("mas_num", map.get("mas_num"));

        //从文件里读取四个log日志文件
        result.addObject("log1", map.get("sb1"));
        result.addObject("log2", map.get("sb2"));
        result.addObject("log3", map.get("sb3"));
        result.addObject("log4", map.get("sb4"));
        //临时文件中读取时间信息
        result.addObject("beginDate", map.get("beginDate"));
        result.addObject("afterDate", map.get("afterDate"));
        result.addObject("beginTime", map.get("beginTime"));
        result.addObject("afterTime", map.get("afterTime"));
        result.addObject("status", map.get("status"));
        result.setViewName(Constants.PAGE_MODULE_02_020);
        return result;
    }

    @RequestMapping(value = "/changePasswordInit", method = { RequestMethod.POST,RequestMethod.GET })
    public ModelAndView changePassword(@ModelAttribute UC_changePasswordInput uc_changePasswordInput, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            modelAndView.addObject("userInfo", new UC_Init_loginInput());
            modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
            return modelAndView;
        }
        modelAndView.setViewName(Constants.PAGE_MODULE_04_010);
        return modelAndView;
    }

    @RequestMapping(value = "/closeBrowser", method = RequestMethod.GET)
    public void closeBrowser(@ModelAttribute(value = "enterPage") String url) {
        uc_init_service.changePW(url);
    }

}