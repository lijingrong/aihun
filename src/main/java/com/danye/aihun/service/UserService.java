package com.danye.aihun.service;

import com.danye.aihun.model.User;
import com.danye.aihun.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 15:03
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUser(String userId) {
        return userRepository.getUserByUserId(userId);
    }

    public User getUserByOpenId(String openId) {
        return userRepository.getUserByOpenId(openId);
    }

}
