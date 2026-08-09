package com.newzkl.platform.base.biz.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.OldColumnName;

/**
 * 入会礼包商品(pack_goods)持久化对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.entity.PackGoodsDO}（表 {@code pack_goods}）。</p>
 *
 * <p>迁移说明：中台 {@code DynamicTableNameInnerInterceptor} 按类名去 {@code DO} 后缀推表名
 * （{@code PackGoodsDO} → {@code pack_goods}），故 {@code @TableName} 不写显式表名；
 * 旧手写 id/actable 注释去除，主键雪花与 createTime/updateTime/delFlag/executor 由 {@code BaseDO} 承接；
 * 旧列 {@code desc} 为 MySQL 保留字，本仓字段改名 {@code intro} 规避（autotable 按字段名重建列），
 * 前端契约字段 {@code desc} 保留在 {@code PackGoodsCommand}/{@code PackGoodsRes}，
 * 由 {@code PackGoodsRepositoryImpl} 显式映射 {@code desc↔intro}。</p>
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
