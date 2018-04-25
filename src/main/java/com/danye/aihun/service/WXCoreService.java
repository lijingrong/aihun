package com.danye.aihun.service;

import com.danye.aihun.model.WXRespTextMessage;
import com.danye.aihun.utils.WXConstants;
import com.danye.aihun.utils.WXMessageUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 微信公众号核心服务类
 *
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 10:13
 */
@Service
public class WXCoreService {

    /**
     * 处理微信发来的请求
     *
     * @param request 发送的请求
     * @return xml
     */
    public String processRequest(HttpServletRequest request, Environment environment) {
        // XML 格式的消息数据
        String responseXml = null;
        // 默认返回的文本消息内容
        String responseContent = "未知的消息类型！";
        WXMessageUtil wxMessageUtil = new WXMessageUtil();
        try {
            // 调用 parseXml 方法解析请求消息
            Map<String, String> requestMap = wxMessageUtil.parseXml(request);
            // 发送方账号
            String fromUserName = requestMap.get("FromUserName");
            // 开发者微信号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            // 回复文本消息
            WXRespTextMessage wxRespTextMessage = new WXRespTextMessage();
            wxRespTextMessage.setToUserName(fromUserName);
            wxRespTextMessage.setFromUserName(toUserName);
            wxRespTextMessage.setCreateTime(new Date().getTime());
            wxRespTextMessage.setMsgType(WXConstants.RESPONSE_MESSAGE_TYPE_TEXT);
            //文本消息
            if (msgType.equals(WXConstants.REQUEST_MESSAGE_TYPE_TEXT)) {
                // 文本消息内容
                String content = requestMap.get("Content");
                responseContent = "您发送的是 【" + content + "】!";
                wxRespTextMessage.setContent(responseContent);
                responseXml = wxMessageUtil.messageToXml(wxRespTextMessage);
            }
            // 事件推送
            else if (msgType.equals(WXConstants.REQUEST_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event");
                // 关注
                if (eventType.equals(WXConstants.EVENT_TYPE_SUBSCRIBE)) {
                    responseContent = new String(environment.getProperty("SUBSCRIBE_RESP_CONTENT").getBytes("ISO-8859-1"), "UTF-8");
                    wxRespTextMessage.setContent(responseContent);
                    responseXml = wxMessageUtil.messageToXml(wxRespTextMessage);
                }
            } else {
                // 当用户发消息时
                responseContent = "单页表单欢迎您！";
                wxRespTextMessage.setContent(responseContent);
                responseXml = wxMessageUtil.messageToXml(wxRespTextMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseXml;
    }
}
