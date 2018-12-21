package com.ncit.common;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Map;


public class SessionListener implements HttpSessionListener {
	
	private static Map<Object, Object> loginUserMap = new HashMap<Object, Object>();
	
    /**以下是实现HttpSessionListener中的方法**/
	@Override
    public void sessionCreated(HttpSessionEvent sessionEvent){
		HttpSession session = sessionEvent.getSession();
		session.setAttribute("active", "yes");
    }

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent){
    	HttpSession session = sessionEvent.getSession();
        
    	if (session != null && loginUserMap.size() != 0) {
            loginUserMap.remove(session);
    	}
    }

	/***
     * 检查当前userId是否已经登录
     * @param userId 用户ID
     * @return 已登录session
     */
	public static HttpSession checkIsLogin(String userId) {
        // 遍历所有已登录user，检查当前user是否已经登录
		if (loginUserMap != null && loginUserMap.size() != 0 && userId != null) {
	        for (Map.Entry<Object, Object> entry : loginUserMap.entrySet()) {
	        	HttpSession eachSession = (HttpSession) entry.getKey();
	        	String eachUserId =  entry.getValue().toString();
	            if (userId.equals(eachUserId)) {
	            	return eachSession;
	            }
	        }
		}
		return null;
	}
	
    /***
     * 将已登录用户删除
     * @param alreadyLoginSession 已登录的session
     */
	public static void removeLogonUser(HttpSession alreadyLoginSession) {
        if (loginUserMap != null && loginUserMap.size() != 0) {
        	loginUserMap.remove(alreadyLoginSession);
        	String isActiveString = alreadyLoginSession.getAttribute("active").toString();
        	if (isActiveString != null && "yes".equals(isActiveString)) {
        		alreadyLoginSession.invalidate();
        	}
        }
	}
	
    /***
     * 保存登录信息
     * @param session session
     * @param userId 当前登录用户ID
     */
	public static void addLogonUser(HttpSession session, String userId) {
        if (loginUserMap != null) {
        	loginUserMap.put(session, userId);
        }
	}
}
