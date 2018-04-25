package com.danye.aihun.repository;

import com.danye.aihun.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 15:01
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User getUserByOpenId(String openId);
}
