package com.danye.aihun.web;

import com.danye.aihun.model.User;
import com.danye.aihun.model.WXOauthCode;
import com.danye.aihun.model.WXSNSUserInfo;
import com.danye.aihun.service.UserService;
import com.danye.aihun.service.WXCommonService;
import com.danye.aihun.service.WXOAuth2TokenService;
import com.danye.aihun.service.WXTokenService;
import com.danye.aihun.utils.Constants;
import com.danye.aihun.utils.WXConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 12:57
 */
@Controller
@PropertySource("classpath:environment.properties")
public class WXOAuthController {

    @Autowired
    private Environment environment;
    @Autowired
    private WXCommonService wxCommonService;
    @Autowired
    private WXTokenService wxTokenService;
    @Autowired
    private WXOAuth2TokenService wxoAuth2TokenService;
    @Autowired
    private UserService userService;

    @RequestMapping("/permOA2Callback")
    public String permOA2Callback(@RequestParam(value = "callbackUrl", required = false) String callbackUrl,
                                  @RequestParam(value = "code", required = false) String oauth_code,
                                  @RequestParam(value = "state", required = false) String oauth_state)
            throws Exception {
        if (!"authdeny".equals(oauth_code) && !"".equals(oauth_code) && null != oauth_code) {
            WXOauthCode wxOauthCode = wxTokenService.getWechatOauthCode(oauth_code);
            User user = userService.getUserByOpenId(wxOauthCode.getOpenId());
            if (null == user) {
                user = new User();
                user.setUserId(UUID.randomUUID().toString());
                user.setOpenId(wxOauthCode.getOpenId());
                user.setCreateTime(new Date());
                userService.saveUser(user);
//                String redirectUrl = URLEncoder.encode(environment.getProperty("domain.web") + "permOA2SNSCallback?callbackUrl=" + URLEncoder.encode(callbackUrl, "UTF-8"), "UTF-8");
//                return "redirect:" + wxCommonService.wxOauth2Url(redirectUrl, WXConstants.OAUTH2_SNS_API_USER_INFO, oauth_state);
            }
            return "redirect:" + callbackUrl + (callbackUrl.contains("?") ? "&" : "?") + "userId=" + user.getUserId() + "&" + Constants.WECHAT_PERM_FLAG + "=" + oauth_state;
        }
        return null;
    }

    @RequestMapping("/permOA2SNSCallback")
    public String permOA2SNSCallback(@RequestParam(value = "callbackUrl", required = false) String callbackUrl,
                                     @RequestParam(value = "code", required = false) String oauth_code,
                                     @RequestParam(value = "state", required = false) String oauth_state) {
        if (!"authdeny".equals(oauth_code) && !"".equals(oauth_code) && null != oauth_code) {
            WXOauthCode wxOauthCode = wxTokenService.getWechatOauthCode(oauth_code);
            User user = userService.getUserByOpenId(wxOauthCode.getOpenId());
            if (null == user) {
                WXSNSUserInfo wxsnsUserInfo = wxoAuth2TokenService.getSNSUserInfo(wxOauthCode.getAccessToken(), wxOauthCode.getOpenId());
                user = new User();
                user.setUserId(UUID.randomUUID().toString());
                user.setOpenId(wxsnsUserInfo.getOpenId());
                user.setNickName(wxsnsUserInfo.getNickName());
                user.setGender(wxsnsUserInfo.getSex() == 2 ? Short.valueOf("0") : Short.valueOf("1"));
                user.setCountry(wxsnsUserInfo.getCountry());
                user.setProvince(wxsnsUserInfo.getProvince());
                user.setCity(wxsnsUserInfo.getCity());
                user.setAvatarUrl(wxsnsUserInfo.getHeadImgUrl());
                user.setCreateTime(new Date());
                userService.saveUser(user);
            }
            return "redirect:" + callbackUrl + (callbackUrl.contains("?") ? "&" : "?") + "userId=" + user.getUserId() + "&" + Constants.WECHAT_PERM_FLAG + "=" + oauth_state;
        }
        return null;
    }
}
