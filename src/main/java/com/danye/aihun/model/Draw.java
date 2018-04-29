package com.danye.aihun.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/29 14:18
 */
@Data
@Entity
@Table(name = "t_draw")
public class Draw {

    @Id
    private String drawId;
    private String userId;
    private String prizeId;
    private Short isDraw = Short.valueOf("0");  // 是否中奖，1-是，0-否
    private Date drawTime = new Date();

    public Draw() {
    }

    public Draw(String drawId, String userId, String prizeId, Short isDraw) {
        this.drawId = drawId;
        this.userId = userId;
        this.prizeId = prizeId;
        this.isDraw = isDraw;
    }
}
