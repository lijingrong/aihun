package com.danye.aihun.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/5/9 18:32
 */
@Data
@Entity
@Table(name = "t_draw_not")
public class DrawNot {
    @Id
    private String drawId;
    private String userId;
    private String prizeId;
    private Short isDraw = Short.valueOf("0");  // 是否中奖，1-是，0-否
    private Date drawTime = new Date();

    public DrawNot() {
    }

    public DrawNot(String drawId, String userId, String prizeId, Short isDraw) {
        this.drawId = drawId;
        this.userId = userId;
        this.prizeId = prizeId;
        this.isDraw = isDraw;
    }
}
