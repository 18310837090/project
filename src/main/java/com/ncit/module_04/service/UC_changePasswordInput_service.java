package com.ncit.module_04.service;

import com.ncit.module_04.model.UC_changePasswordInput;
import com.ncit.module_init.model.UC_Init_userInfo;


/**
 * Created by jing.xiao on 2018/10/29
 */
public interface UC_changePasswordInput_service {
    String chpwd(UC_changePasswordInput uc_changePasswordInput, UC_Init_userInfo userInfo);
}
