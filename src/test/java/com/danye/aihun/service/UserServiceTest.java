package com.danye.aihun.service;

import com.danye.aihun.AihunApplicationTests;
import com.danye.aihun.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 15:50
 */
public class UserServiceTest extends AihunApplicationTests {

    @Autowired
    UserService userService;

    @Test
    public void saveUser() {
        User user = new User();
//        user.setUserId("1234567890");
        user.setOpenId("1234567890");
        user.setNickName("昵称");
        user.setGender(0 == 2 ? Short.valueOf("0") : Short.valueOf("1"));
        user.setCountry("中国");
        user.setProvince("江苏");
        user.setCity("南京");
        user.setAvatarUrl("");
//        user.setCreateTime(new Date());
        userService.saveUser(user);
    }

    @Test
    public void getUserByOpenId() {
        saveUser();

        User user = userService.getUserByOpenId("1234567890");

        System.out.println(user);
    }
}
