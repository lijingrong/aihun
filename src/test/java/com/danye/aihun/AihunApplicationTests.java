package com.danye.aihun;

import com.danye.aihun.model.Contact;
import com.danye.aihun.service.ContactRepository;
import com.danye.aihun.utils.OSSUtil;
import com.danye.aihun.utils.QRCodeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class AihunApplicationTests {

    @Autowired
    private ContactRepository contactRepository;

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

}
