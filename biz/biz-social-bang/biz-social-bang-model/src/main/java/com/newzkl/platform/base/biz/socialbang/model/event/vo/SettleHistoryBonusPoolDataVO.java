package com.newzkl.platform.base.biz.socialbang.model.event.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description: 历史奖金池数据
 * @Author: niu
 * @Date: 2024/1/16 16:01
 */
@Data
public class SettleHistoryBonusPoolDataVO {

    /**
     * 分红流水记录
     */
    private Page<SettleHistoryBonusPoolDataListVO> pageInfo;


    /**
     * 订单总金额合计
     */
    private BigDecimal totalOrderAmount = BigDecimal.ZERO;


    /**
     * 预计可分配总金额合计
     */
    private BigDecimal estimatedAmount = BigDecimal.ZERO;


    /**
     * 实际可分配总金额合计
     */
    private BigDecimal actualAmount = BigDecimal.ZERO;


}
