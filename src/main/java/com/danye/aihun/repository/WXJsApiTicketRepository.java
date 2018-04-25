package com.danye.aihun.repository;

import com.danye.aihun.model.WXJsApiTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 15:38
 */
@Repository
public interface WXJsApiTicketRepository extends JpaRepository<WXJsApiTicket, String> {

    WXJsApiTicket getTopByOrderByCreateTimeDesc();
}
