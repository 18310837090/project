package com.ncit.module_03.UC03_02.controller;

import com.ncit.module_03.UC03_02.service.UC03_02_service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UC03_02Controller {
    @Resource
    UC03_02_service uc03_02_service;

    @RequestMapping(value = "/assessResultByDatetimeStart",method = RequestMethod.GET)
    public void assessResultByDatetime(@ModelAttribute(value = "date") String date,@ModelAttribute(value = "timeFrom") String timeFrom,@ModelAttribute(value = "timeTo") String timeTo) {
        uc03_02_service.startThread(date,timeFrom,timeTo);
    }

    @RequestMapping(value = "/assessResultByDatetimeStop",method = RequestMethod.GET)
    public void assessResultByDatetime() {
        uc03_02_service.stopThread1();
    }
}
