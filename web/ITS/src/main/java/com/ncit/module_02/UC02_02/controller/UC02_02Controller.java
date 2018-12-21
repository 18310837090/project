package com.ncit.module_02.UC02_02.controller;

import com.ncit.module_02.UC02_02.service.UC02_02_service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UC02_02Controller {
    @Resource
    UC02_02_service uc02_02_service;

    @RequestMapping(value = "/systemStop",method = RequestMethod.GET)
    public void systemStop() {
        uc02_02_service.stopThread1();
    }

    @RequestMapping(value = "/systemStartUp",method = RequestMethod.GET)
    public void systemStartUp(@RequestParam("beginDate") String _beginDate,
                       @RequestParam("afterDate") String _afterDate,
                       @RequestParam("beginTime") String _beginTime,
                       @RequestParam("afterTime") String _afterTime) {
        uc02_02_service.startThread(_beginDate, _afterDate,_beginTime,_afterTime);
    }

}
