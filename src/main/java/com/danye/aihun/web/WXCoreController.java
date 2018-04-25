package com.danye.aihun.web;

import com.danye.aihun.service.WXCoreService;
import com.danye.aihun.service.WXSignService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 微信公众号请求处理的核心控制类
 *
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 10:08
 */
@Controller
@PropertySource("classpath:environment.properties")
@RequestMapping("/wxCoreServlet")
public class WXCoreController {

    private static final Logger log = LogManager.getLogger(WXCoreController.class);
    @Autowired
    private Environment environment;
    @Autowired
    private WXCoreService wxCoreService;
    @Autowired
    private WXSignService wxSignService;

    /**
     * 请求校验（确认请求来自微信服务器）
     */
    @RequestMapping(method = RequestMethod.GET)
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 将请求、响应的编码设置为 UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding(("UTF-8"));
        // 开发模式接口配置信息中的 Token
        String token = environment.getProperty("TOKEN");
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        PrintWriter out = response.getWriter();
        // 请求校验，若校验成功则原样返回 echostr，表示接入成功，否则接入失败
        if (wxSignService.checkSignature(signature, token, timestamp, nonce)) {
            log.info("==========>>验证成功！");
            out.print(echostr);
        }
        out.close();
    }

    /**
     * 处理微信服务器发来的消息
     */
    @RequestMapping(method = RequestMethod.POST)
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 将请求、响应的编码设置为 UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding(("UTF-8"));
        String token = environment.getProperty("TOKEN");
        //接收参数：微信加密签名、时间戳、随机数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        PrintWriter out = response.getWriter();
        // 请求校验
        if (wxSignService.checkSignature(signature, token, timestamp, nonce)) {
            // 调用核心服务类接收处理请求
            String responseXml = wxCoreService.processRequest(request, environment);
            out.print(responseXml);
        }
        out.close();
    }
}
