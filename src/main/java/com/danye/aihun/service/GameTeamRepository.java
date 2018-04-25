package com.danye.aihun.service;

import com.danye.aihun.model.GameTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameTeamRepository extends JpaRepository<GameTeam, String> {

    Page<GameTeam> getGameTeamByUidOrderByTimeDesc(final String uid, Pageable pageable);
}
