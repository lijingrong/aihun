package com.danye.aihun.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 13:00
 */
@Data
@Entity
@Table(name = "t_wechat_oauth_code")
public class WXOauthCode {
    // openId 与 code 映射表
    @Id
    private String codeId;
    private String wxCode;
    private String accessToken;
    private String unionId;
    private String openId;
    private Date createTime;
}
