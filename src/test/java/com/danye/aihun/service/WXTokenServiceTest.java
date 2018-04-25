package com.danye.aihun.service;

import com.danye.aihun.AihunApplicationTests;
import com.danye.aihun.model.WXJsApiTicket;
import com.danye.aihun.model.WXOauthCode;
import com.danye.aihun.repository.WXJsApiTicketRepository;
import com.danye.aihun.repository.WXOauthCodeRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 16:01
 */
public class WXTokenServiceTest extends AihunApplicationTests {

    @Autowired
    WXJsApiTicketRepository wxJsApiTicketRepository;
    @Autowired
    WXOauthCodeRepository wxOauthCodeRepository;

    @Test
    public void saveWxJsApiTicket() {
        WXJsApiTicket wxJsApiTicket = new WXJsApiTicket();
        wxJsApiTicket.setTicketId(UUID.randomUUID().toString());
        wxJsApiTicket.setAccessToken(UUID.randomUUID().toString());
        wxJsApiTicket.setJsapiTicket(UUID.randomUUID().toString());
        wxJsApiTicket.setExpiresIn(12);
        wxJsApiTicket.setCreateTime(new Date());
        wxJsApiTicketRepository.save(wxJsApiTicket);
    }

    @Test
    public void batchSaveWxJsApiTicket() {
        List<WXJsApiTicket> wxJsApiTickets = new ArrayList<>();
        WXJsApiTicket wxJsApiTicket = new WXJsApiTicket();
        wxJsApiTicket.setTicketId(UUID.randomUUID().toString());
        wxJsApiTicket.setAccessToken(UUID.randomUUID().toString());
        wxJsApiTicket.setJsapiTicket(UUID.randomUUID().toString());
        wxJsApiTicket.setExpiresIn(12);
        wxJsApiTicket.setCreateTime(new Date());
        wxJsApiTickets.add(wxJsApiTicket);

        WXJsApiTicket wxJsApiTicket1 = new WXJsApiTicket();
        wxJsApiTicket1.setTicketId(UUID.randomUUID().toString());
        wxJsApiTicket1.setAccessToken(UUID.randomUUID().toString());
        wxJsApiTicket1.setJsapiTicket(UUID.randomUUID().toString());
        wxJsApiTicket1.setExpiresIn(12);
        wxJsApiTicket1.setCreateTime(new Date());
        wxJsApiTickets.add(wxJsApiTicket1);

        wxJsApiTicketRepository.saveAll(wxJsApiTickets);
    }

    @Test
    public void getFirstWxJsApiTicket() {
//        saveWxJsApiTicket();

        batchSaveWxJsApiTicket();

        WXJsApiTicket wxJsApiTicket = wxJsApiTicketRepository.getTopByOrderByCreateTimeDesc();

        System.out.println(wxJsApiTicket);
    }

    @Test
    public void saveWxOauthCode() {
        WXOauthCode wxOauthCode = new WXOauthCode();
        wxOauthCode.setCodeId(UUID.randomUUID().toString());
        wxOauthCode.setWxCode("1234567890");
        wxOauthCode.setAccessToken(UUID.randomUUID().toString());
        wxOauthCode.setUnionId(UUID.randomUUID().toString());
        wxOauthCode.setOpenId(UUID.randomUUID().toString());
        wxOauthCode.setCreateTime(new Date());
        wxOauthCodeRepository.save(wxOauthCode);
    }

    @Test
    public void getWxOauthCodeByWxCode() {
        saveWxOauthCode();

        WXOauthCode wxOauthCode = wxOauthCodeRepository.getWXOauthCodeByWxCode("1234567890");

        System.out.println(wxOauthCode);
    }
}
