package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品分组表（原专区表，按新设计重命名/调整字段）
 * </p>
 * @author sijiwang
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName
public class GoodsZoneDO extends BaseDO {

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 分组描述
     */
    private String groupDesc;

    /**
     * 背景图
     */
    private String backgroundImg;

    /**
     * 排序类型
     */
    private Integer sortType;

    /**
     * 搜索框显示状态（新增，0-不显示 1-显示，对应产品设计的「搜索框」选项）
     */
    private Integer searchBoxStatus;

    /**
     * 价格显示状态（新增，0-不显示 1-显示，对应产品设计的「显示价格」勾选）
     */
    private Integer priceShowStatus;

    /**
     * 门店显示状态（新增，0-不显示 1-显示，对应产品设计的「显示门店」勾选）
     */
    private Integer storeShowStatus;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 分组状态（0-禁用 1-启用）
     */
    private Integer state;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 创建人
     */
    private String createName;

}