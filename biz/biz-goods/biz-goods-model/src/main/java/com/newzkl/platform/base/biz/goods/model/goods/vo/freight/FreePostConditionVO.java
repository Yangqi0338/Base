package com.newzkl.platform.base.biz.goods.model.goods.vo.freight;

import lombok.Data;

/**
 * @Description: 包邮条件信息
 * @Author: niu
 * @Date: 2023/4/27 14:36
 */
@Data
public class FreePostConditionVO {

    /**
     * 件数
     */
    private Integer piece;

    /**
     * 重量
     */
    private Integer weight;

    /**
     * 体积
     */
    private Integer bulk;

    /**
     * 金额
     */
    private Integer amount;
}
