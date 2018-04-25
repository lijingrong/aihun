package com.danye.aihun.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/25 14:42
 */
@Data
@Entity
@Table(name = "t_user_info")
public class User {

    @Id
    private String userId;
    private String openId;
    private String nickName;
    private Short gender = Short.valueOf("1"); // 性别（0-女，1-男）
    private String country;
    private String province;
    private String city;
    private String avatarUrl;
    @Column(updatable = false)
    private Date createTime;
}
