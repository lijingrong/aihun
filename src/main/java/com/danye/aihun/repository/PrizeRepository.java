package com.danye.aihun.repository;

import com.danye.aihun.model.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/29 14:25
 */
@Repository
public interface PrizeRepository extends JpaRepository<Prize, String> {

    @Query(value = " SELECT * FROM t_prize WHERE remain_count > 0 ", nativeQuery = true)
    List<Prize> getAvailablePrize();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = " UPDATE t_prize SET remain_count = remain_count - 1 WHERE prize_id = ?1 AND remain_count > 0 ", nativeQuery = true)
    Integer updatePrizeRemainCount(String prizeId);
}
