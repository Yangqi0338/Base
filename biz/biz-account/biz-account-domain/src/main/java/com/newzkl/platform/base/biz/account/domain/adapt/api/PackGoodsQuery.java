package com.newzkl.platform.base.biz.account.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 入会礼包商品查询入参 (跨域出站)
 *
 * <p>旧实现复用 {@code PackGoodsReq} 作查询条件, 此处收敛为按 (类型, 等级) 查询。</p>
 *
 * @author KC
 */
@Data
public class PackGoodsQuery implements Serializable {

    /**
     * 类型 (角色 ID)
     */
    private Integer type;

    /**
     * 礼包对应等级值
     */
    private Integer level;
}
