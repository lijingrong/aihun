package com.danye.aihun;

import com.danye.aihun.model.Contact;
import com.danye.aihun.model.GameTeam;
import com.danye.aihun.service.ContactRepository;
import com.danye.aihun.service.GameTeamRepository;
import com.danye.aihun.service.GameTeamService;
import com.danye.aihun.utils.OSSUtil;
import com.danye.aihun.utils.QRCodeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class AihunApplicationTests {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private GameTeamService gameTeamService;

    @Test
    public void contextLoads() {

    }

    @Test
    public void addContact() {
        Contact contact = new Contact();
        contact.setOpenId("123456");
        contact.setTelephone("15951076347");
        contact.setZhName("梁静茹");
        contact.setAddress("江苏省南京市江宁区秣周东路悠谷3号楼p349");
        contactRepository.save(contact);
    }

    @Test
    public void qrCode(){
        String imgName = OSSUtil.upload(QRCodeUtil.generateQRCodeStream("http://www.baidu.com"));
        System.out.println(imgName);

    }

    @Test
    public void addGameTeam(){
        String uid = UUID.randomUUID().toString();
        GameTeam gameTeam = new GameTeam();
        gameTeam.setId(UUID.randomUUID().toString());
        gameTeam.setUid(uid);
        gameTeam.setFollowId(UUID.randomUUID().toString());
        gameTeamService.save(gameTeam);
        gameTeamService.getLatestGameTeamByUid(uid);
    }
}
