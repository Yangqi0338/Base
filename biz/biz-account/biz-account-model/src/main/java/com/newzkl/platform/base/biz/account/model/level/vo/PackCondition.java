package com.newzkl.platform.base.biz.account.model.level.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 入会礼包升级条件值对象。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.level.model.condition.PackCondition}。
 * 旧类实现的 {@code Condition#isMeet} 等级计算行为属升级引擎, 未随本切片迁移
 * (调用方在分润/升级链, 见 slug 11)。</p>
 *
 * @author KC
 */
@Data
public class PackCondition implements Serializable {

    /**
     * 礼包价格
     */
    private Integer amount;

    /**
     * 礼包商品 ID
     */
    private Long packGoodsId;

    /**
     * 礼包名称
     */
    private String packName;

    /**
     * 礼包图片
     */
    private String packImg;
}
