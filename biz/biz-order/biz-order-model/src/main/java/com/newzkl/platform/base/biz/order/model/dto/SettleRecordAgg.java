package com.newzkl.platform.base.biz.order.model.dto;


import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/1116:06
 */
@Data
public class SettleRecordAgg {
    /**
     * 结算记录表
     */
    private SettleRecordDTO settleRecord;
    /**
     * 结算记录明细表
     */
    private List<SettleRecordItemDTO> settleRecordItemList;

    public SettleRecordAgg(SettleRecordDTO settleRecord, List<SettleRecordItemDTO> settleRecordItemList) {
        this.settleRecord = settleRecord;
        this.settleRecordItemList = settleRecordItemList;
    }
}
