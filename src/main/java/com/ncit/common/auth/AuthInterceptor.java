package com.ncit.common.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        boolean flag = true;
        HttpSession session = request.getSession();

        if (handler instanceof HandlerMethod) {
            Auth auth = ((HandlerMethod) handler).getMethod().getAnnotation(Auth.class);
            if (auth != null) {
                // 有权限控制的就要检查
                if (session.getAttribute("userInfo") == null) {
                    // 没登录就要求登录
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType("text/html; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write("{\"type\":\"nosignin\",\"msg\":\"请您先登录!\"}");
                    out.flush();
                    out.close();
                    flag = false;
                } else {
                    flag = false;
                }
            }
        }
        return flag;
    }
}