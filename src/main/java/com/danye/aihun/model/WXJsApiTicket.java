package com.danye.aihun.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * JsApi 获取jsapi_ticket
 *
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 13:33
 */
@Data
@Entity
@Table(name = "t_wechat_jsapi_ticket")
public class WXJsApiTicket {

    @Id
    private String ticketId;
    private String jsapiTicket; // 获取的jsapi_ticket
    private String accessToken; // 接口访问凭证
    private int expiresIn; // 凭证有效期，单位：秒
    private Date createTime;
}
