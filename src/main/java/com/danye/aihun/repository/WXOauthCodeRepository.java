package com.danye.aihun.repository;

import com.danye.aihun.model.WXOauthCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 15:25
 */
@Repository
public interface WXOauthCodeRepository extends JpaRepository<WXOauthCode, String> {

    WXOauthCode getWXOauthCodeByWxCode(String wxCode);
}
