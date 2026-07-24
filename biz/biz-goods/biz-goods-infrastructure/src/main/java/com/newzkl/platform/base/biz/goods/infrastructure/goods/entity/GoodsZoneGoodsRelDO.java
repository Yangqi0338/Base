package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
/**
 * <p>
 * 商品分组-商品关联表
 * </p>
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap = true)
public class GoodsZoneGoodsRelDO extends BaseDO {

    /**
     * 分组ID
     */
    @Index
    private Long groupId;

    /**
     * 商品ID
     */
    @Index
    private Long spuId;

    /**
     * 商品名称快照
     */
    private String spu;
}