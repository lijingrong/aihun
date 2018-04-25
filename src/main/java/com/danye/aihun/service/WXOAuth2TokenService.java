package com.danye.aihun.service;

import com.danye.aihun.model.WXOauth2Token;
import com.danye.aihun.model.WXSNSUserInfo;
import com.danye.aihun.utils.WXConstants;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 网页授权
 *
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 14:08
 */
@Service
public class WXOAuth2TokenService {

    private static Logger log = LogManager.getLogger(WXOAuth2TokenService.class);

    @Autowired
    private WXCommonService wxCommonService;

    /**
     * 获取网页授权凭证
     *
     * @param appId     公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code      获取网页授权access_token的票据
     * @return Oauth2Token
     */
    public WXOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
        String requestUrl = WXConstants.OAUTH2_ACCESSTOKEN_URL.replace("APPID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        requestUrl = requestUrl.replace("CODE", code);
        return setOauth2TokenResult(requestUrl, "GET", null);
    }

    /**
     * 刷新网页授权凭证
     *
     * @param appId        公众账号的唯一标识
     * @param refreshToken 用于刷新凭证
     * @return Oauth2Token
     */
    public WXOauth2Token refreshOauth2AccessToken(String appId, String refreshToken) {
        String requestUrl = WXConstants.OAUTH2_REFRESHTOKEN_URL.replace("APPID", appId);
        requestUrl = requestUrl.replace("REFRESH_TOKEN", refreshToken);
        return setOauth2TokenResult(requestUrl, "GET", null);
    }

    /**
     * 网页授权凭证调用接口返回结果的处理
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return Oauth2Token
     */
    public WXOauth2Token setOauth2TokenResult(String requestUrl,
                                              String requestMethod,
                                              String outputStr) {
        WXOauth2Token wxOauth2Token = null;
        // 刷新网页授权凭证
        JsonNode jsonNode = wxCommonService.httpsRequest(requestUrl, requestMethod, outputStr);
        if (null != jsonNode) {
            try {
                wxOauth2Token = new WXOauth2Token();
                wxOauth2Token.setAccessToken(jsonNode.get("access_token").asText());
                wxOauth2Token.setExpiresIn(jsonNode.get("expires_in").asInt());
                wxOauth2Token.setRefreshToken(jsonNode.get("refresh_token").asText());
                wxOauth2Token.setOpenId(jsonNode.get("openid").asText());
                wxOauth2Token.setScope(jsonNode.get("scope").asText());
                if (null != jsonNode.get("unionid"))  // 用户UnionId
                    wxOauth2Token.setUnionId(jsonNode.get("unionid").asText());
            } catch (Exception e) {
                wxOauth2Token = null;
                String errInfo = "获取网页授权凭证失败";
                wxCommonService.portExceptionInfoLog(errInfo, log, jsonNode);
            }
        }
        return wxOauth2Token;
    }

    /**
     * 通过网页授权获取用户信息
     *
     * @param accessToken 网页授权接口调用凭证
     * @param openId      用户标识
     * @return SNSUserInfo
     */
    @SuppressWarnings({"deprecation", "unchecked"})
    public WXSNSUserInfo getSNSUserInfo(String accessToken, String openId) {
        WXSNSUserInfo wxsnsUserInfo = null;
        String requestUrl = WXConstants.SNS_USERINFO_URL.replace("ACCESS_TOKEN", accessToken);
        requestUrl = requestUrl.replace("OPENID", openId);
        // 通过网页授权获取用户信息
        JsonNode jsonNode = wxCommonService.httpsRequest(requestUrl, "GET", null);
        if (null != jsonNode) {
            try {
                wxsnsUserInfo = new WXSNSUserInfo();
                // 用户标识
                wxsnsUserInfo.setOpenId(jsonNode.get("openid").asText());
                // 昵称
                wxsnsUserInfo.setNickName(jsonNode.get("nickname").asText());
                // 性别（1 是男性， 2 是女性， 0 是未知）
                wxsnsUserInfo.setSex(jsonNode.get("sex").asInt());
                // 用户所在国家
                wxsnsUserInfo.setCountry(jsonNode.get("country").asText());
                // 用户所在省份
                wxsnsUserInfo.setProvince(jsonNode.get("province").asText());
                // 用户所在城市
                wxsnsUserInfo.setCity(jsonNode.get("city").asText());
                // 用户头像
                wxsnsUserInfo.setHeadImgUrl(jsonNode.get("headimgurl").asText());
                // 用户特权信息       不需要特权信息
//                wxsnsUserInfo.setPrivilegeList(jsonNode.get("privilege").asText());
                // 用户 UnionID
                if (null != jsonNode.get("unionid"))  {
                    wxsnsUserInfo.setUnionId(jsonNode.get("unionid").asText());
                }
            } catch (Exception e) {
                wxsnsUserInfo = null;
                String errInfo = "获取用户信息失败";
                wxCommonService.portExceptionInfoLog(errInfo, log, jsonNode);
            }
        }
        return wxsnsUserInfo;
    }

    /**
     * 检验授权凭证是否有效
     *
     * @param accessToken 网页授权接口调用凭证
     * @param openId      用户标识
     * @return true | false
     */

    public boolean isEffectiveAccessToken(String accessToken, String openId) {
        boolean result = false;
        String requestUrl = WXConstants.SNS_AUTH_URL.replace("ACCESS_TOKEN", accessToken);
        requestUrl = requestUrl.replace("OPENID", openId);
        // 检验授权凭证是否有效
        JsonNode jsonNode = wxCommonService.httpsRequest(requestUrl, "GET", null);
        if (null != jsonNode) {
            String info = "检验授权凭证是否有效";
            result = wxCommonService.portDealInfoLog(info, log, jsonNode);
        }
        return result;
    }
}
