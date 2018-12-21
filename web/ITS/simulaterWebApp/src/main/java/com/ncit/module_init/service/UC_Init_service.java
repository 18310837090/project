package com.ncit.module_init.service;

import com.ncit.module_init.model.UC_Init_loginInput;
import com.ncit.module_init.model.UC_Init_loginOutput;

public interface UC_Init_service {
    UC_Init_loginOutput login(UC_Init_loginInput ucInit_loginInput);
    void changePW(String url);
}
