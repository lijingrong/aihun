package com.danye.aihun.repository;

import com.danye.aihun.model.Draw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/29 14:26
 */
@Repository
public interface DrawRepository extends JpaRepository<Draw, String> {

    Draw getTopByUserIdAndIsDraw(String userId, Short isDraw);
}
