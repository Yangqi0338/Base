package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入会礼包商品(pack_goods)持久化对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class PackGoodsDO extends BaseDO {

    /**
     * 类型
     * @ext 角色 ID
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
    @TableField("`desc`")
    private String desc;

    /**
     * 状态
     */
    private CommonEnum.YesOrNo state;
}
