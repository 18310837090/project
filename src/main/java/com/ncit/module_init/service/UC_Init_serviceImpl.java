package com.ncit.module_init.service;

import com.ncit.common.model.RemoteProperties;
import com.ncit.common.util.commonCheckUtil;
import com.ncit.module_init.model.UC_Init_loginInput;
import com.ncit.module_init.model.UC_Init_loginOutput;
import com.ncit.module_init.model.UC_Init_userInfo;
import com.ncit.common.Constants;
import com.ncit.common.util.PasswordencryptUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;

@Service
public class UC_Init_serviceImpl implements UC_Init_service {

    @Resource
    RemoteProperties remoteProperties;

    @Override
    public UC_Init_loginOutput login(UC_Init_loginInput uc_Init_loginInput) {
        String id = uc_Init_loginInput.getId();
        String password = uc_Init_loginInput.getPassWord();
        UC_Init_loginOutput uc_Init_loginOutput = new UC_Init_loginOutput();
        UC_Init_userInfo uc_Init_userInfo = new UC_Init_userInfo();
        String pw = null;

        // 用户名为空
        if (commonCheckUtil.isEmpty(id)) {
            uc_Init_loginOutput.setMessageId(Constants.Message_module_INIT_NG_001);
            return uc_Init_loginOutput;
        }
        // 用户名超长
        if (commonCheckUtil.isOverLength(id, Constants.USER_ID_MAX_LENGTH)) {
            uc_Init_loginOutput.setMessageId(Constants.Message_module_INIT_NG_002);
            return uc_Init_loginOutput;
        }

        // 密码为空
        if (commonCheckUtil.isEmpty(password)) {
            uc_Init_loginOutput.setMessageId(Constants.Message_module_INIT_NG_003);
            return uc_Init_loginOutput;
        }
        // 密码超长
        if (commonCheckUtil.isOverLength(password, Constants.PASSWORD_MAX_LENGTH)) {
            uc_Init_loginOutput.setMessageId(Constants.Message_module_INIT_NG_004);
            return uc_Init_loginOutput;
        }
        try {
            //密码加密
            pw = PasswordencryptUtil.eccrypt(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //用户判断
        if ((remoteProperties.getID()).equals(id)&&(remoteProperties.getPASSWORD()).equals(password)){
            uc_Init_loginOutput.setMessageId(Constants.Message_module_INIT_OK_001);
            uc_Init_userInfo.setUserId(id);
            uc_Init_userInfo.setPassword(password);
            uc_Init_loginOutput.setUserInfo(uc_Init_userInfo);
        }else {
            uc_Init_loginOutput.setMessageId(Constants.Message_module_INIT_NG_005);
            return uc_Init_loginOutput;
        }
        return uc_Init_loginOutput;
    }

    @Override
    public void changePW(String url) {
//        try {
//            PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.ENTER_PAGE,url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        remoteProperties.setEnterPage(url);

    }
}
