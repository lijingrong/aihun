package com.danye.aihun.model;

import lombok.Data;

/**
 * 网页授权信息
 *
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 13:15
 */
@Data
public class WXOauth2Token extends WXToken{

    private String refreshToken; // 用于刷新凭证
    private String openId;  // 用户标识
    private String scope; // 用户授权作用域
    private String unionId; // 用户 UnionID （只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段）
}
