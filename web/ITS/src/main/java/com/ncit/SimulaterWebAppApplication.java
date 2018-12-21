package com.ncit;

import com.ncit.common.Constants;
import com.ncit.common.util.PasswordencryptUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimulaterWebAppApplication {
	public static void main(String[] args) {
		try {
			//添加线程运行状态标志，用于浏览器异常退出时重新加载返回该页面
			PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.STATUS,Constants.IF_NOT_HAVE);
			PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.ENTER_PAGE,"/simulaterWebApp/index");
//			PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.STATUS,Constants.IF_HAVE);
//			PasswordencryptUtil.changePW(Constants.DB_FILE_PATH,Constants.ENTER_PAGE,"/simulaterWebApp/systemStartInit");
			SpringApplication.run(SimulaterWebAppApplication.class, args);
		}catch (Exception e){
			e.printStackTrace();
		}

	}
}