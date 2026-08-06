package com.newzkl.platform.base.biz.store.model.template.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 样板店门店VO
 *
 * @author fang
 */
@Data
public class ModelShopStorePageRes extends BaseRes {
    /**
     * 主键
     */
    private Long storeId;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 样板店ID
     */
    private Long modelShopId;

    /**
     * 总支付金额 (Money, 落库 BIGINT 分)
     */
    private Money totalPayAmount;

    /**
     * 总支付笔数
     */
    private Integer totalPayNum;

    /**
     * 样板店名称
     */
    private String modelShopName;

    /**
     * 是否正在使用
     */
    private boolean inUse = false;

    /**
     * 使用时间
     */
    private LocalDateTime createTime;
}