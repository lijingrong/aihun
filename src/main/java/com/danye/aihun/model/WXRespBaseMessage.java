package com.danye.aihun.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 相应消息基类（公众账号 -> 普通用户）
 *
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 10:32
 */
@Data
public class WXRespBaseMessage {

    // 接收方账号（收到的 OpenID）
    @XStreamAlias("ToUserName")
    private String ToUserName;
    // 开发者微信号
    @XStreamAlias("FromUserName")
    private String FromUserName;
    // 消息创建时间（整型）
    @XStreamAlias("CreateTime")
    private long CreateTime;
    // 消息类型
    @XStreamAlias("MsgType")
    private String MsgType;
}
