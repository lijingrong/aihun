package com.danye.aihun.service;

import com.danye.aihun.model.WXJsApiTicket;
import com.danye.aihun.model.WXOauth2Token;
import com.danye.aihun.model.WXOauthCode;
import com.danye.aihun.model.WXToken;
import com.danye.aihun.repository.WXJsApiTicketRepository;
import com.danye.aihun.repository.WXOauthCodeRepository;
import com.danye.aihun.utils.WXConstants;
import com.fasterxml.jackson.databind.JsonNode;
import net.sf.json.JSONException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 13:12
 */
@Service
@PropertySource("classpath:environment.properties")
public class WXTokenService {

    private static Logger log = LogManager.getLogger(WXTokenService.class);

    @Autowired
    private Environment environment;
    @Autowired
    private WXCommonService wxCommonService;
    @Autowired
    private WXSignService wxSignService;
    @Autowired
    private WXOAuth2TokenService wxoAuth2TokenService;
    @Autowired
    private WXOauthCodeRepository wxOauthCodeRepository;
    @Autowired
    private WXJsApiTicketRepository wxJsApiTicketRepository;

    /**
     * 获取接口访问凭证
     *
     * @param appid     凭证
     * @param appsecret 密钥
     * @return WXToken
     */
    public WXToken getToken(String appid, String appsecret) {
        WXToken wxToken = null;
        String requestUrl = WXConstants.TOKEN_URL.replace("APPID", appid);
        requestUrl = requestUrl.replace("APPSECRET", appsecret);
        // 发起 GET 请求获取凭证
        JsonNode jsonNode = wxCommonService.httpsRequest(requestUrl, "GET", null);
        if (null != jsonNode) {
            try {
                wxToken = new WXToken();
                wxToken.setAccessToken(jsonNode.get("access_token").asText());
                wxToken.setExpiresIn(jsonNode.get("expires_in").asInt());
            } catch (JSONException je) {
                wxToken = null;
                // 获取 token 失败
                String errInfo = "获取token失败";
                wxCommonService.portExceptionInfoLog(errInfo, log, jsonNode);
            }
        }
        return wxToken;
    }

    /**
     * 获取公众号用于调用微信JS接口的临时票据
     *
     * @param accessToken 调用接口凭证
     */
    public WXJsApiTicket getJsApiTicket(String accessToken) {
        WXJsApiTicket wxJsApiTicket = null;
        String requestUrl = WXConstants.JSAPI_TICKET_GET_URL.replace("ACCESS_TOKEN", accessToken);
        // 获取公众号用于调用微信JS接口的临时票据 jsapi_ticket
        JsonNode jsonNode = wxCommonService.httpsRequest(requestUrl, "GET", null);
        if (null != jsonNode) {
            try {
                wxJsApiTicket = new WXJsApiTicket();
                wxJsApiTicket.setJsapiTicket(jsonNode.get("ticket").asText());
                wxJsApiTicket.setExpiresIn(jsonNode.get("expires_in").asInt());
            } catch (Exception e) {
                wxJsApiTicket = null;
                String errInfo = "获取jsapi_ticket失败！";
                wxCommonService.portExceptionInfoLog(errInfo, log, jsonNode);
            }
        }
        return wxJsApiTicket;
    }

    public void getWechatJsApiConfig(HttpServletRequest request, Model model) {
        // 用户请求生成签名的 URL
        String requestUrl = request.getScheme() + "://" + request.getServerName() + request.getRequestURI() +
                (request.getQueryString() != null ? "?" + request.getQueryString().split("#")[0] : "");
        String timeStamp = wxCommonService.getCurrentTimeStamp();
        String nonceStr = wxCommonService.getRandomString(16);
        String signature = wxSignService.getSignature(getWechatJsApi().getJsapiTicket(), nonceStr, timeStamp, requestUrl);
        model.addAttribute("config_appId", environment.getProperty("APPID"));
        model.addAttribute("config_timestamp", timeStamp);
        model.addAttribute("config_nonceStr", nonceStr);
        model.addAttribute("config_signature", signature);
    }

    public WXJsApiTicket saveWechatJsApi() {
        WXToken wxToken = getToken(environment.getProperty("APPID"), environment.getProperty("APPSECRET"));
        WXJsApiTicket wxJsApiTicket = new WXJsApiTicket();
        wxJsApiTicket.setTicketId(UUID.randomUUID().toString());
        wxJsApiTicket.setAccessToken(wxToken.getAccessToken());
        wxJsApiTicket.setJsapiTicket(getJsApiTicket(wxToken.getAccessToken()).getJsapiTicket());
        wxJsApiTicket.setExpiresIn(wxToken.getExpiresIn());
        wxJsApiTicket.setCreateTime(new Date());
        wxJsApiTicketRepository.save(wxJsApiTicket);
        return wxJsApiTicket;
    }

    public WXJsApiTicket getWechatJsApi() {
        WXJsApiTicket wxJsApiTicket = wxJsApiTicketRepository.getTopByOrderByCreateTimeDesc();
        if (null == wxJsApiTicket) {
            wxJsApiTicket = saveWechatJsApi();
        } else {
            int expiresIn = wxJsApiTicket.getExpiresIn();
            long between = (new Date().getTime() - wxJsApiTicket.getCreateTime().getTime()) / 1000;
            if ((expiresIn - between) <= 200) {
                wxJsApiTicket = saveWechatJsApi();
            }
        }
        return wxJsApiTicket;
    }

    public WXOauthCode saveWechatOauthCode(String wxCode) {
        WXOauth2Token wxOauth2Token = wxoAuth2TokenService.getOauth2AccessToken(environment.getProperty("APPID"), environment.getProperty("APPSECRET"), wxCode);
        WXOauthCode wxOauthCode = new WXOauthCode();
        wxOauthCode.setCodeId(UUID.randomUUID().toString());
        wxOauthCode.setWxCode(wxCode);
        wxOauthCode.setAccessToken(wxOauth2Token.getAccessToken());
        wxOauthCode.setUnionId(wxOauth2Token.getUnionId() == null ? "" : wxOauth2Token.getUnionId());
        wxOauthCode.setOpenId(wxOauth2Token.getOpenId());
        wxOauthCode.setCreateTime(new Date());
        wxOauthCodeRepository.save(wxOauthCode);
        return wxOauthCode;
    }

    public WXOauthCode getWechatOauthCode(String wxCode) {
        WXOauthCode wxOauthCode = wxOauthCodeRepository.getWXOauthCodeByWxCode(wxCode);
        if (null == wxOauthCode) {
            wxOauthCode = saveWechatOauthCode(wxCode);
        }
        return wxOauthCode;
    }
}
