package com.danye.aihun.web;

import com.danye.aihun.model.Contact;
import com.danye.aihun.model.GameTeam;
import com.danye.aihun.model.ResponseCode;
import com.danye.aihun.service.ContactService;
import com.danye.aihun.service.DrawService;
import com.danye.aihun.service.GameTeamService;
import com.danye.aihun.service.WXTokenService;
import com.danye.aihun.utils.OSSUtil;
import com.danye.aihun.utils.QRCodeUtil;
import com.danye.aihun.utils.UserIdHolder;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@PropertySource("classpath:environment.properties")
public class IndexController {

    @Autowired
    private Environment environment;
    @Autowired
    private ContactService contactService;
    @Autowired
    private GameTeamService gameTeamService;
    @Autowired
    private WXTokenService wxTokenService;
    @Autowired
    private DrawService drawService;

    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model) {
        wxTokenService.getWechatJsApiConfig(request, model);
        model.addAttribute("share_url", environment.getProperty("domain.web"));
        return "index";
    }

    @RequestMapping("/test")
    public String testIndex() {
        return "test";
    }

    @RequestMapping("/audio")
    public String audio() {
        return "audio";
    }

    @RequestMapping("/aihun/addContact")
    @ResponseBody
    public ResponseCode addContact(@RequestParam("zhName") String zhName,
                                   @RequestParam("telephone") String telephone,
                                   @RequestParam("address") String address) {
        Contact contact = new Contact();
        contact.setUid(UserIdHolder.getUserId());
        contact.setZhName(zhName);
        contact.setTelephone(telephone);
        contact.setAddress(address);
        contactService.addContact(contact);
        return ResponseCode.SUCCESS;
    }

    @RequestMapping("/aihun/getQRCode")
    @ResponseBody
    public Map<String, String> qrCode(ServletRequest request) {
        final String uid = UserIdHolder.getUserId();
        String url = request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + "?uid=" + uid;
        String imgName = OSSUtil.upload(QRCodeUtil.generateQRCodeStream(url));
        GameTeam gameTeam = new GameTeam();
        gameTeam.setId(UUID.randomUUID().toString());
        gameTeam.setUid(uid);
        gameTeamService.save(gameTeam);
        Map<String, String> result = new HashMap<>();
        result.put("imgUrl", "http://aihun-img-hz.oss-cn-hangzhou.aliyuncs.com/" + imgName);
        result.put("gameTeamId", gameTeam.getId());
        return result;
    }

    @RequestMapping("/aihun/getGameTeam")
    @ResponseBody
    public Map<String, Object> getGameTeam(@RequestParam(value = "uid", required = false) String uid) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isEmpty(uid)) {
            result.put("code", 0);
            return result;
        }
        GameTeam gameTeam = gameTeamService.getLatestGameTeamByUid(uid);
        if (gameTeam == null) {
            result.put("code", 0);
            result.put("uid", UserIdHolder.getUserId());
        } else {
            gameTeam.setFollowId(UserIdHolder.getUserId());
            gameTeamService.save(gameTeam);
            result.put("code", 1);
            result.put("data", gameTeam);
        }
        return result;
    }

    @MessageMapping("/buildGameTeam")
    @SendTo("/topic/pushGameTeam")
    public Map<String, Object> buildGameTeam(@RequestBody Map<String, Object> data) throws Exception {
        String uid = MapUtils.getString(data, "uid");
        String userId = MapUtils.getString(data, "userId");
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isEmpty(uid)) {
            result.put("code", 0);
            return result;
        }
        GameTeam gameTeam = gameTeamService.getLatestGameTeamByUid(uid);
        if (gameTeam == null) {
            result.put("code", 0);
            result.put("uid", userId);
        } else {
            gameTeam.setFollowId(userId);
            gameTeamService.save(gameTeam);
            result.put("code", 1);
            result.put("data", gameTeam);
        }
        return result;
    }

    @RequestMapping("/aihun/isFollowerJoin")
    @ResponseBody
    public ResponseCode isFollowerJoin(@RequestParam("gameTeamId") String gameTeamId) {
        if (StringUtils.isEmpty(gameTeamId))
            return ResponseCode.FAILURE;
        GameTeam gameTeam = gameTeamService.getGameTeam(gameTeamId);
        if (gameTeam == null || StringUtils.isEmpty(gameTeam.getFollowId()))
            return ResponseCode.FAILURE;
        return ResponseCode.SUCCESS;
    }


    @PostMapping("/aihun/postSharkTime")
    @ResponseBody
    public ResponseCode postSharkTime(@RequestParam("gameTeamId") String gameTeamId, @RequestParam("uid") String uid) {
        if (StringUtils.isEmpty(gameTeamId))
            return ResponseCode.FAILURE;
        GameTeam gameTeam = gameTeamService.getGameTeam(gameTeamId);
        if (gameTeam == null)
            return ResponseCode.FAILURE;
        if (StringUtils.equals(uid, gameTeam.getUid()))
            gameTeam.setUSharkTime(new Date());
        if (StringUtils.equals(uid, gameTeam.getFollowId()))
            gameTeam.setFSharkTime(new Date());
        gameTeamService.save(gameTeam);
        return ResponseCode.SUCCESS;
    }

    /**
     * 双人玩游戏时，判断一伴是否摇动了手机
     *
     * @param gameTeamId 组队的Id
     * @return code=1 摇了，code=2 未摇
     */
    @RequestMapping("/aihun/isPartnerSharked")
    @ResponseBody
    public ResponseCode isPartnerSharked(@RequestParam("gameTeamId") String gameTeamId) {
        if (StringUtils.isEmpty(gameTeamId))
            return ResponseCode.FAILURE;
        GameTeam gameTeam = gameTeamService.getGameTeam(gameTeamId);
        if (gameTeam == null)
            return ResponseCode.FAILURE;
        if (StringUtils.equals(UserIdHolder.getUserId(), gameTeam.getUid()) && gameTeam.getFSharkTime() != null)
            return ResponseCode.SUCCESS;
        if (StringUtils.equals(UserIdHolder.getUserId(), gameTeam.getFollowId()) && gameTeam.getUSharkTime() != null)
            return ResponseCode.SUCCESS;
        return ResponseCode.FAILURE;
    }

    @RequestMapping("/aihun/draw")
    @ResponseBody
    public Map<String, Object> draw() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("drawResult", drawService.draw(UserIdHolder.getUserId()));
        return result;
    }

    @PostMapping("/aihun/postGameStatus")
    @ResponseBody
    public ResponseCode postGameStatus(@RequestParam("gameTeamId") String gameTeamId,
                                       @RequestParam("gameStatus") Integer gameStatus) {
        if (StringUtils.isEmpty(gameTeamId) || (gameStatus != 1 && gameStatus != 0))
            return ResponseCode.FAILURE;
        GameTeam gameTeam = gameTeamService.getGameTeam(gameTeamId);
        if (gameTeam == null)
            return ResponseCode.FAILURE;
        if (StringUtils.equals(UserIdHolder.getUserId(), gameTeam.getUid())) {
            gameTeam.setUGameStatus(gameStatus);
        } else if (StringUtils.equals(UserIdHolder.getUserId(), gameTeam.getFollowId())) {
            gameTeam.setFGameStatus(gameStatus);
        }
        gameTeamService.save(gameTeam);
        return ResponseCode.SUCCESS;
    }

    @GetMapping("/aihun/getGameStatus")
    @ResponseBody
    public ResponseCode getGameStatus(@RequestParam("gameTeamId") String gameTeamId) {
        if (StringUtils.isEmpty(gameTeamId))
            return ResponseCode.FAILURE;
        GameTeam gameTeam = gameTeamService.getGameTeam(gameTeamId);
        if (gameTeam == null)
            return ResponseCode.FAILURE;
        if (StringUtils.equals(UserIdHolder.getUserId(), gameTeam.getUid())) {
            if (-1 == gameTeam.getFGameStatus())
                return ResponseCode.CODE_1;
            if (0 == gameTeam.getFGameStatus())
                return ResponseCode.FAILURE_0;
            if (1 == gameTeam.getFGameStatus())
                return ResponseCode.SUCCESS;
        }
        if (StringUtils.equals(UserIdHolder.getUserId(), gameTeam.getFollowId())) {
            if (-1 == gameTeam.getUGameStatus())
                return ResponseCode.CODE_1;
            if (0 == gameTeam.getUGameStatus())
                return ResponseCode.FAILURE_0;
            if (1 == gameTeam.getUGameStatus())
                return ResponseCode.SUCCESS;
        }
        return ResponseCode.FAILURE;
    }

    @RequestMapping("/noWechat")
    public String noWechat() {
        return "noWechat";
    }

    @RequestMapping("/statistics")
    public String statistics(Model model) {
        model.addAttribute("firstPrizeCount", drawService.getDrawCountByPrizeId("1234567890"));
        model.addAttribute("secondPrizeCount", drawService.getDrawCountByPrizeId("1234567891"));
        model.addAttribute("thirdPrizeCount", drawService.getDrawCountByPrizeId("1234567892"));
        model.addAttribute("accessCount", wxTokenService.getWechatOauthCodeCount());
        return "statistics";
    }

}
