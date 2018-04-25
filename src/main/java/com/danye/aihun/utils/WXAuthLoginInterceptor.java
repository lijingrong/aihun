package com.danye.aihun.utils;

import com.danye.aihun.service.WXCommonService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 18:07
 */
@Component
@PropertySource("classpath:environment.properties")
public class WXAuthLoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private Environment environment;
    @Autowired
    private WXCommonService wxCommonService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getHeader("User-Agent").contains("MicroMessenger")) {
            String flag = request.getParameter(Constants.WECHAT_PERM_FLAG);
            if (StringUtils.isNotBlank(flag) && flag.equals(Constants.WECHAT_AUTH_LOGIN)) {
                return true;
            } else {
                String callbackUrl = URLEncoder.encode(request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString().split("#")[0] : ""), "UTF-8");
                String redirectUrl = URLEncoder.encode(environment.getProperty("domain.web") + "permOA2Callback?callbackUrl=" + callbackUrl, "UTF-8");
                response.sendRedirect(wxCommonService.wxOauth2Url(redirectUrl, WXConstants.OAUTH2_SNS_API_BASE, Constants.WECHAT_AUTH_LOGIN));
                return false;
            }
        } else {
            request.getRequestDispatcher("/noWechat").forward(request, response);
            return false;
        }
    }
}
