package com.danye.aihun.service;

import com.danye.aihun.model.GameTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class GameTeamService {

    @Autowired
    private GameTeamRepository gameTeamRepository;

    public void save(GameTeam gameTeam) {
        gameTeamRepository.save(gameTeam);
    }

    public GameTeam getLatestGameTeamByUid(String uid) {
        Page<GameTeam> page = gameTeamRepository.getGameTeamByUidOrderByTimeDesc(uid, PageRequest.of(0, 1));
        if (page.getContent().isEmpty()) {
            return null;
        }
        return page.getContent().get(0);
    }
}
