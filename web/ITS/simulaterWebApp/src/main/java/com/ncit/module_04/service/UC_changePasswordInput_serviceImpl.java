package com.ncit.module_04.service;

import com.ncit.common.model.RemoteProperties;
import com.ncit.common.util.PasswordencryptUtil;
import com.ncit.common.util.commonCheckUtil;
import com.ncit.module_04.model.UC_changePasswordInput;
import com.ncit.module_init.model.UC_Init_userInfo;
import com.ncit.common.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by jing.xiao  on 2018/10/29
 */
@Service("chpwdService")
public class UC_changePasswordInput_serviceImpl implements UC_changePasswordInput_service {
    @Resource
    RemoteProperties remoteProperties;

    @Override
    public String chpwd(UC_changePasswordInput uc_changePasswordInput, UC_Init_userInfo userInfo) {
        String oldPassword = uc_changePasswordInput.getOldPassword();
        String newPassword = uc_changePasswordInput.getNewPassword();
        String newPassword2 = uc_changePasswordInput.getNewPassword2();

        String resultMessage;
        String pw;

        // 旧密码为空
//        if (commonCheckUtil.isEmpty(oldPassword)) {
//            resultMessage = Constants.Message_Module_04_010_NG_001;
//            return resultMessage;
//        }

        // 新密码为空
//        if (commonCheckUtil.isEmpty(newPassword)) {
//            resultMessage = Constants.Message_Module_04_010_NG_002;
//            return resultMessage;
//        }

        // 新密码确认为空
//        if (commonCheckUtil.isEmpty(newPassword2)) {
//            resultMessage = Constants.Message_Module_04_010_NG_003;
//            return resultMessage;
//        }

        // 密码超长12
        if (commonCheckUtil.isOverLength(newPassword, Constants.PASSWORD_MAX_LENGTH)) {
            resultMessage = Constants.Message_Module_04_010_NG_004;
            return resultMessage;
        }

        // 新密码和新密码确认不一致
        if (!newPassword.equals(newPassword2)) {
            resultMessage = Constants.Message_Module_04_010_NG_005;
            return resultMessage;
        }

//        try {
//            //旧密码加密
//            pw = PasswordencryptUtil.eccrypt(oldPassword);
//        } catch (NoSuchAlgorithmException e) {
//            resultMessage = Constants.Message_Module_04_010_NG_006;
//            return resultMessage;
//        }

        //*******************验证用户密码*********************

        // 未取到数据
        if (!oldPassword.equals(remoteProperties.getPASSWORD())) {
            resultMessage = Constants.Message_Module_04_010_NG_008;
            return resultMessage;
        }
        if(userInfo.getPassword().equals(remoteProperties.getPASSWORD())){
            //修改用户密码文件
            try {
                PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.PASSWORD,newPassword2);
                remoteProperties.setPASSWORD(newPassword2);
            }catch (IOException e){
                e.printStackTrace();
            }
            resultMessage = Constants.Message_Module_04_010_OK_001;
        }else {
            resultMessage = Constants.Message_Module_04_010_NG_007;
        }
        return resultMessage;
    }
}
