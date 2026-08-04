package com.newzkl.platform.base.biz.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
     * 礼包简介（旧列名 {@code desc} 为保留字，改名 {@code intro}）
     */
    private String intro;

    /**
     * 状态 0 下架 1 上架
     */
    private Integer state;
}
