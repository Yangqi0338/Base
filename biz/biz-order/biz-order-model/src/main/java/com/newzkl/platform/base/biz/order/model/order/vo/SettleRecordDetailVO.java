package com.newzkl.platform.base.biz.order.model.order.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 结算记录表
 * @author fang
 */
@Data
public class SettleRecordDetailVO {
     /**
     * 结算单信息
     */
     private SettleRecordVO settleRecordVO;
     /**
      * 结算单明细
      */
     private List<SettleRecordItemVO> settleRecordItemVOList;
}