package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.biz.account.model.vo.OperatorClientBaseVO;
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
     * 分润收益
     */
    private Integer serviceFee;
}