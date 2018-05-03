package com.danye.aihun.config;

import com.danye.aihun.utils.UserIdInterceptor;
import com.danye.aihun.utils.WXAuthLoginInterceptor;
import com.danye.aihun.utils.WXBrowserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private WXAuthLoginInterceptor wxAuthLoginInterceptor;
    @Autowired
    private WXBrowserInterceptor wxBrowserInterceptor;
    @Autowired
    private UserIdInterceptor userIdInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(wxBrowserInterceptor).addPathPatterns("/**").excludePathPatterns("/noWechat",
                "/wxCoreServlet","/test","/audio","/res/**","/**/*.png","/**/*.js","/**/*.css");
        registry.addInterceptor(wxAuthLoginInterceptor).addPathPatterns("/");
        registry.addInterceptor(userIdInterceptor).addPathPatterns("/", "/aihun/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //todo 生产环境要去掉
        registry.addMapping("/**").allowedHeaders("*").allowedMethods("*").allowedOrigins("*");
    }
}
