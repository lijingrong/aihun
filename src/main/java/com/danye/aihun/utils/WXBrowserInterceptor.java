package com.danye.aihun.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 18:18
 */
@Component
public class WXBrowserInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (StringUtils.isNotEmpty(request.getHeader("User-Agent"))
                && request.getHeader("User-Agent").contains("MicroMessenger")) {
            return true;
        } else {
            request.getRequestDispatcher("/noWechat").forward(request, response);
            return false;
        }
    }
}
