package com.ncit.module_01.UC01_03.service;


import java.util.Map;

public interface UC01_03_service {
    void startThread(String _beforeDate, String _afterDate);
    void stopThread();
    Map init();
}
