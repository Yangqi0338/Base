package com.newzkl.platform.base.biz.user.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 礼包升级校验出站入参
 *
 * <p>字段迁自旧 {@code IAccountFacade.packUpCheck(accountId, PackGoodsInfo)} 的 accountId + 礼包信息。</p>
 *
 * @author KC
 */
@Data
public class PackUpCheckCommand implements Serializable {

    /**
     * 账号ID
     */
    private Long accountId;

    /**
     * 礼包商品ID
     */
    private Long packGoodsId;

    /**
     * 礼包金额（分）
     */
    private Integer amount;

    /**
     * 礼包等级
     */
    private Integer level;

    /**
     * 礼包类型（角色 ID）
     */
    private Integer type;
}
