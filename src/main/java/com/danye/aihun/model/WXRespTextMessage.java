package com.danye.aihun.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 10:33
 */
@Data
@XStreamAlias("xml")
public class WXRespTextMessage extends WXRespBaseMessage {

    // 回复的消息内容
    @XStreamAlias("Content")
    private String Content;
}
