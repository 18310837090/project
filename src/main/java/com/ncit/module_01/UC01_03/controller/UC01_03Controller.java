package com.ncit.module_01.UC01_03.controller;

import com.ncit.module_01.UC01_03.service.UC01_03_service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class UC01_03Controller {

    @Resource
    UC01_03_service uc01_03_service;

    @RequestMapping(value = "/databaseImport",method = RequestMethod.POST)
    public void trafficDataUpload(@RequestParam("beginDate") String _beginDate, @RequestParam("afterDate") String _afterDate, HttpServletRequest req) {
        uc01_03_service.startThread(_beginDate, _afterDate);
    }

    @RequestMapping(value = "/databaseImportEnd",method = RequestMethod.GET)
    public void databaseImportEnd() {
        uc01_03_service.stopThread();
    }
}
