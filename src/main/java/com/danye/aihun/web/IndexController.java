package com.danye.aihun.web;

import com.danye.aihun.model.Contact;
import com.danye.aihun.model.ResponseCode;
import com.danye.aihun.service.ContactService;
import com.danye.aihun.utils.OSSUtil;
import com.danye.aihun.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private ContactService contactService;

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
    public Map<String, String> qrCode() {
        String imgName = OSSUtil.upload(QRCodeUtil.generateQRCodeStream("http://www.baidu.com"));
        Map<String, String> result = new HashMap<>();
        result.put("imgUrl", "http://aihun-img.oss-cn-shanghai.aliyuncs.com/" + imgName);
        return result;
    }

}
