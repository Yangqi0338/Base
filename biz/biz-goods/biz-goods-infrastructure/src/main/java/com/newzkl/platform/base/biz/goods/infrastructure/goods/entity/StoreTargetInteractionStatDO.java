package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import com.newzkl.platform.base.biz.goods.model.enums.user.InteractionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
/**
 * 门店-对象互动统计DO（数据库映射实体）
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class StoreTargetInteractionStatDO extends BaseDO {

    @Index
    private Long storeId;

    @Index
    private Long publisherId;

    @Index
    private InteractionEnum.TargetTypeEnum targetType;

    @Index
    private Long targetId;

    private Integer viewCount = 0;

    private Integer likeCount = 0;

    private Integer shareCount = 0;
    private String extJson = "{}";
}