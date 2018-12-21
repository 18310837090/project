package com.ncit.module_04.controller;

import com.ncit.common.Constants;
import com.ncit.common.SessionListener;
import com.ncit.module_04.model.UC_changePasswordInput;
import com.ncit.module_04.service.UC_changePasswordInput_service;
import com.ncit.module_init.model.UC_Init_loginInput;
import com.ncit.module_init.model.UC_Init_userInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class changePwdController {
    @Resource
    UC_changePasswordInput_service uc_changePasswordInput_service;

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ModelAndView changePassword(@ModelAttribute UC_changePasswordInput uc_changePasswordInput, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            modelAndView.addObject("userInfo", new UC_Init_loginInput());
            modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
            return modelAndView;
        }
        //修改密码
        String resultMessage = uc_changePasswordInput_service.chpwd(uc_changePasswordInput,userInfo);

        if (resultMessage == Constants.Message_Module_04_010_OK_001){
            //修改成功
            SessionListener.removeLogonUser(request.getSession());
            modelAndView.addObject("message", resultMessage);
            modelAndView.setViewName(Constants.PAGE_MODULE_INIT_010);
        }else {
            //修改失败
            modelAndView.addObject("message", resultMessage);
            modelAndView.addObject("uc_changePasswordInput", uc_changePasswordInput);
            modelAndView.setViewName(Constants.PAGE_MODULE_04_010);
        }

        return modelAndView;
    }
}
