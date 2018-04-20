package com.danye.aihun.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_contact")
public class Contact {

    @Id
    private String openId;
    private String zhName;
    private String telephone;
    private String address;
}
