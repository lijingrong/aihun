package com.danye.aihun.model;

import lombok.Data;

/**
 * 凭证
 *
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 13:13
 */
@Data
public class WXToken {

    private String accessToken; // 接口访问凭证
    private int expiresIn; // 凭证有效期，单位：秒
}
