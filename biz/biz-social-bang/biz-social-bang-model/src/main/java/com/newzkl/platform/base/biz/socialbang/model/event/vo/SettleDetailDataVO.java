package com.newzkl.platform.base.biz.socialbang.model.event.vo;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class SettleDetailDataVO {

    /**
     * 结算id
     */
    private String settlementId;


    /**
     * 分红流水统计
     */
    private SettleSumDataVO settleSumDataVO;

    /**
     * 结算单状态 PENDING_CONFIRMATION: 待确认 ,CONFIRMED:已确认 DELETED:已删除
     * {@Link {@link ActivityEnum.SettleState}}
     */
    private String state;

    public String getStateDesc() {
        return ActivityEnum.SettleState.getDescByCode(this.state);
    }


    /**
     * 分红流水记录
     */
    private Page<SettleDetailDataListVO> pageInfo;


    /**
     * 甄选师分红奖金合计
     */
    private BigDecimal selectorTotalAmount = BigDecimal.ZERO;


    /**
     * 交易师分红奖金合计
     */
    private BigDecimal dealerTotalAmount = BigDecimal.ZERO;

    /**
     * 运营商分红奖金合计
     */
    private BigDecimal operatorTotalAmount = BigDecimal.ZERO;
}
