package com.danye.aihun.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/29 14:15
 */
@Data
@Entity
@Table(name = "t_prize")
public class Prize {

    @Id
    private String prizeId;
    private String prizeName;
    @Column(updatable = false)
    private Short prizeType;  // 奖项类型，0-一等奖，1-二等奖，2-三等奖，3-无奖
    @Column(updatable = false)
    private Short sumCount;  // -1 数量不限
    private Short remainCount;
    @Column(updatable = false)
    private Integer rand;
}
