package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

import java.util.List;

/**
 * 结算记录表
 * @author fang
 */
@Data
public class SettleRecordDetailVO extends BaseVO {
     /**
     * 结算单信息
     */
     private SettleRecordVO settleRecordVO;
     /**
      * 结算单明细
      */
     private List<SettleRecordItemVO> settleRecordItemVOList;
}