package com.danye.aihun.utils;

/**
 * 微信常量
 *
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 10:35
 */
public class WXConstants {

    /**------------------------PortUrl 常量--------------------------*/
    /**
     * 凭证常量
     */
    // 凭证获取（GET）
    // @param grant_type:获取 access_token 填写 client_credential
    // @param appid:第三方用户唯一凭证
    // @param secret:第三方用户唯一凭证密钥，即 appsecret
    public static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/" +
            "token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /**
     * 公众号用于调用微信JS接口的临时票据 Url 常量
     */
    // 获取公众号用于调用微信JS接口的临时票据（GET）
    // @param access_token:调用接口凭证
    // @param type:获取公众号用于调用微信JS接口的临时票据类型，填写jsapi
    public static final String JSAPI_TICKET_GET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/" +
            "getticket?access_token=ACCESS_TOKEN&type=jsapi";

    /**
     * 网页授权 Url 常量
     */
    // 用户同意授权 Url 格式，获取 code
    // @param appid:公众号的唯一标识
    // @param redirect_uri:授权后重定向的回调链接地址，请使用 urlencode 对链接进行处理
    // @param response_type:返回类型，请填写 code
    // @param scope:应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户 openid），
    //                            snsapi_userinfo （弹出授权页面，可通过 openid 拿到昵称、性别、所在地。
    //                                      并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
    // @param state:重定向后会带上 state 参数，开发者可以填写 a-zA-Z0-9 的参数值，最多 128 字节
    // @param #wechat_redirect:无论直接打开还是做页面302重定向时候，必须带此参数
    public static final String OAUTH2_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/" +
            "authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    // 获取网页授权凭证接口（GET）
    // @param appid:公众号的唯一标识
    // @param secret:公众号的 appsecret
    // @param code:填写用户同意授权获取的 code 参数
    // @param grant_type:填写为 authorization_code
    public static final String OAUTH2_ACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/" +
            "access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 刷新网页授权凭证接口（GET）
    // @param appid:公众号的唯一标识
    // @param grant_type:填写为 refresh_token
    // @param refresh_token:填写通过 access_token 获取到的 refresh_token 参数
    public static final String OAUTH2_REFRESHTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/" +
            "refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
    // 通过网页授权获取用户信息（GET）
    // @param access_token:网页授权接口调用凭证,注意：此 access_token 与基础支持的 access_token 不同
    // @param openid:用户的唯一标识
    // @param lang:返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
    public static final String SNS_USERINFO_URL = "https://api.weixin.qq.com/sns/" +
            "userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    // 检验授权凭证（access_token）是否有效（GET）
    // @param access_token:网页授权接口调用凭证,注意：此 access_token 与基础支持的 access_token 不同
    // @param openid:用户的唯一标识
    public static final String SNS_AUTH_URL = "https://api.weixin.qq.com/sns/" +
            "auth?access_token=ACCESS_TOKEN&openid=OPENID";

    // 网页授权应用作用域
    public static final String OAUTH2_SNS_API_BASE = "snsapi_base";
    public static final String OAUTH2_SNS_API_USER_INFO = "snsapi_userinfo";

    /**
     * 用户 Url 常量
     */
    // 获取用户基本信息（GET）
    // @param access_token:调用接口凭证
    // @param openid:用户的唯一标识
    // @param lang:返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
    public static final String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/" +
            "info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    // 获取关注者列表（GET）
    // @param access_token:调用接口凭证
    // @param next_openid:第一个拉取的 OPENID，不填默认从头开始拉取
    public static final String USER_GET_URL = "https://api.weixin.qq.com/cgi-bin/user/" +
            "get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";


    /**
     * -----------------------------微信消息类型常量--------------------------------
     */
    // 请求消息类型：文本
    public static final String REQUEST_MESSAGE_TYPE_TEXT = "text";
    // 请求消息类型：事件推送
    public static final String REQUEST_MESSAGE_TYPE_EVENT = "event";
    // 事件类型：subscribe （订阅）
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";
    // 响应消息类型：文本
    public static final String RESPONSE_MESSAGE_TYPE_TEXT = "text";
}
