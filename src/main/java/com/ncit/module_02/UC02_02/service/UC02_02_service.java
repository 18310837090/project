package com.ncit.module_02.UC02_02.service;

import java.util.Map;

public interface UC02_02_service {
    Map init();
    void startThread(String _beforeDate, String _afterDate, String _beginTime,String _afterTime);
    void stopThread1();
}
