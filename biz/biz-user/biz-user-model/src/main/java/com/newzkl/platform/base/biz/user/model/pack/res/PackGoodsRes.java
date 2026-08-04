package com.newzkl.platform.base.biz.user.model.pack.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入会礼包商品出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.packgoods.model.vo.PackGoodsVO}。
 * id/createTime/updateTime 由 {@code BaseRes} 提供。</p>
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PackGoodsRes extends BaseRes {

    /**
     * 类型（角色 ID）
     */
    private Integer type;

    /**
     * 礼包等级
     */
    private Integer level;

    /**
     * 礼包金额（分）
     */
    private Integer amount;

    /**
     * 礼包名称
     */
    private String name;

    /**
     * 礼包图片
     */
    private String img;

    /**
     * 礼包简介
     */
    private String desc;

    /**
     * 状态 0 下架 1 上架
     */
    private Integer state;

    /**
     * 能否购买
     */
    private Boolean canBuy;
}
