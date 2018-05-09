package com.danye.aihun.repository;

import com.danye.aihun.model.DrawNot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/5/9 18:35
 */
@Repository
public interface DrawNotRepository extends JpaRepository<DrawNot, String> {
}
