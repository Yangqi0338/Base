package com.newzkl.platform.base.biz.user.model.pack.vo;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
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
public class PackGoodsVO {

    /**
     * 类型
     */
    private RoleEnum.CompanyRole type;

    /**
     * 礼包等级
     */
    private Integer level;

    /**
     * 礼包金额
     */
    private Money amount;

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
    private CommonEnum.YesOrNo state;

    /**
     * 能否购买
     */
    private CommonEnum.YesOrNo canBuy;
}
