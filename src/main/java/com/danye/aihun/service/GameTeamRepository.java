package com.danye.aihun.service;

import com.danye.aihun.model.GameTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameTeamRepository extends JpaRepository<GameTeam,String>{

    GameTeam getGameTeamByUid(final String uid);
}
