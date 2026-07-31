package com.newzkl.platform.base.biz.activity.model.event.vo;

import lombok.Data;

/**
 * @Description: 奖品设置
 * @Author: niu
 * @Date: 2024/1/9 16:52
 */
@Data
public class AwardVO {

    /**
     * 奖品id
     */
    private Long awardId;

    /**
     * 奖品类型 0：现金 1：优惠券 2：实物 3：其他
     */
    private Integer awardType;

    /**
     * 奖品名称
     */
    private String awardName;

    /**
     * 奖品内容
     */
    private String awardContent;
}
