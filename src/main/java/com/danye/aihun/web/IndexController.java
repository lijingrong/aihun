package com.danye.aihun.web;

import com.danye.aihun.model.Contact;
import com.danye.aihun.model.GameTeam;
import com.danye.aihun.model.ResponseCode;
import com.danye.aihun.service.ContactService;
import com.danye.aihun.service.GameTeamRepository;
import com.danye.aihun.service.GameTeamService;
import com.danye.aihun.utils.OSSUtil;
import com.danye.aihun.utils.QRCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class IndexController {

    @Autowired
    private ContactService contactService;
    @Autowired
    private GameTeamService gameTeamService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/addContact")
    @ResponseBody
    public ResponseCode addContact(@RequestParam("zhName") String zhName,
                                   @RequestParam("telephone") String telephone,
                                   @RequestParam("address") String address) {
        Contact contact = new Contact();
        contact.setOpenId("");
        contact.setZhName(zhName);
        contact.setTelephone(telephone);
        contact.setAddress(address);
        contactService.addContact(contact);
        return ResponseCode.SUCCESS;
    }

    @RequestMapping("/getQRCode")
    @ResponseBody
    public Map<String, String> qrCode(ServletRequest request) {
        final String uid = UUID.randomUUID().toString();
        String url = request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + "?uid=" + uid;
        String imgName = OSSUtil.upload(QRCodeUtil.generateQRCodeStream(url));
        GameTeam gameTeam = new GameTeam();
        gameTeam.setId(UUID.randomUUID().toString());
        gameTeam.setUid(uid);
        gameTeamService.save(gameTeam);
        Map<String, String> result = new HashMap<>();
        result.put("imgUrl", "http://aihun-img.oss-cn-shanghai.aliyuncs.com/" + imgName);
        return result;
    }

    @RequestMapping("/getGameTeam")
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
            //todo:设置当前用户ID
            result.put("uid",UUID.randomUUID().toString());
        } else {
            //todo
            gameTeam.setFollowId(UUID.randomUUID().toString());
            gameTeamService.save(gameTeam);
            result.put("code", 1);
            result.put("data", gameTeam);
        }
        return result;
    }

}
