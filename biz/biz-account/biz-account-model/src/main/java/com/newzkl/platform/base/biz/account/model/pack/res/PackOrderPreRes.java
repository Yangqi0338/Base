package com.newzkl.platform.base.biz.account.model.pack.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.io.Serializable;

/**
 * 入会礼包订单预创建出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.packorder.model.vo.PackOrderPreVO}。</p>
 *
 * @author KC
 */
@Data
public class PackOrderPreRes extends BaseRes {

    /**
     * 订单金额（分）
     */
    private Money amount;

    /**
     * 订单明细 JSON（结构：List&lt;PackGoodsRes&gt;）
     */
    private String packOrderItemList;

    /**
     * 礼包等级
     */
    private Integer packLevel;

    /**
     * 礼包类型（角色 ID）
     */
    private Integer packType;

    /**
     * 礼包名称
     */
    private String levelName;

    /**
     * 下单账号ID
     */
    private Long accountId;
}
