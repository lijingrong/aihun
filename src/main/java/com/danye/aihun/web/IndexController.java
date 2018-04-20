package com.danye.aihun.web;

import com.danye.aihun.model.Contact;
import com.danye.aihun.model.ResponseCode;
import com.danye.aihun.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private ContactService contactService;

    @RequestMapping("/")
    @ResponseBody
    public ResponseCode index() {
        return ResponseCode.SUCCESS;
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

}
