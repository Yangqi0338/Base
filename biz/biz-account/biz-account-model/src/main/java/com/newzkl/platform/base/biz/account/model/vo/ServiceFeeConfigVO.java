package com.newzkl.platform.base.biz.account.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 服务费配置
 *
 * <p>迁移: 原 {@code com.zkl.scm.finance.model.account.vo.ServiceFeeConfigVO};
 * 因位于 account 对外服务签名 (IdentityService) 上, 降级为 account 本地共享内核类型。</p>
 *
 * @author KC
 */
@Data
public class ServiceFeeConfigVO implements Serializable {

    /**
     * 阶梯配置项
     */
    private List<AmountRateDTO> itemList;

    /**
     * 当前服务费率
     */
    private Double serviceFee;
}
