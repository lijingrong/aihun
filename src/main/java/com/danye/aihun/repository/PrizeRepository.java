package com.danye.aihun.repository;

import com.danye.aihun.model.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/29 14:25
 */
@Repository
public interface PrizeRepository extends JpaRepository<Prize, String> {

    @Query(value = " SELECT * FROM t_prize WHERE remain_count > 0 ", nativeQuery = true)
    List<Prize> getAvailablePrize();
}
