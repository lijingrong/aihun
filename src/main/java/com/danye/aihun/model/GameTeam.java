package com.danye.aihun.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "t_game_team")
public class GameTeam {

    @Id
    private String id;

    private String uid;

    private String followId;

    @Column(insertable = false, updatable = false)
    private Date time;

}
