package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.biz.account.model.vo.OperatorClientBaseVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

/**
 * 市场交易师
 *
 * @author fang
 */
@Data
public class DealerOutRes extends OperatorClientBaseVO {
    /**
     * 分润比例
     */
    private Double serviceRate;
    /**
     * 绑定的二级市场数量
     */
    private Integer marketCount;
    /**
     * 分润收益 (Money, 落库 BIGINT 分)
     */
    private Money serviceFee;
}