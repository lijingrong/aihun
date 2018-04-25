package com.danye.aihun.model;

import lombok.Data;

import java.util.List;

/**
 * 通过网页授权获取的用户信息
 *
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 14:14
 */
@Data
public class WXSNSUserInfo {

    // 用户标识
    private String openId;
    // 用户昵称
    private String nickName;
    // 性别（1 是男性， 2 是女性， 0 是未知）
    private int sex;
    // 国家
    private String country;
    // 省份
    private String province;
    // 城市
    private String city;
    // 用户头像链接
    private String headImgUrl;
    // 用户特权信息
    private List<String> privilegeList;
    // 开发者拥有多个移动应用、网站应用和公众帐号，可通过获取用户基本信息中的unionid来区分用户的唯一性
    // （只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段）
    private String unionId;
}
