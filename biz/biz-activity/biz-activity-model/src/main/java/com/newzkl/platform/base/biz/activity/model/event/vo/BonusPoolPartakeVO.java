package com.newzkl.platform.base.biz.activity.model.event.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Description: 奖金池参与记录
 * @Author: niu
 * @Date: 2024/1/16 10:50
 */
@Data
public class BonusPoolPartakeVO {

    /**
     * 唯一id
     */
    private Long id;

    /**
     * 参与会员id
     */
    private Long memberId;

    /**
     * 交易单号
     */
    private String orderSn;

    /**
     * skuCode
     */
    private Long skuCode;

    /**
     * 购买数量
     */
    private Integer buyNum;

    /**
     * 奖金
     */
    private Integer bonus;

    /**
     * 创建时间
     */
    private Date createTime;
}
