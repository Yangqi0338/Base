package com.newzkl.platform.base.biz.order.model.order.vo;

import com.newzkl.platform.base.biz.order.model.order.dto.SettleRecord;
import com.newzkl.platform.base.biz.order.model.order.dto.SettleRecordItem;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/1116:06
 */
@Data
public class SettleRecordAggVO {
    /**
     * 结算记录表
     */
    private SettleRecord settleRecord;
    /**
     * 结算记录明细表
     */
    private List<SettleRecordItem> settleRecordItemList;

    public SettleRecordAggVO(SettleRecord settleRecord, List<SettleRecordItem> settleRecordItemList) {
        this.settleRecord = settleRecord;
        this.settleRecordItemList = settleRecordItemList;
    }
}
