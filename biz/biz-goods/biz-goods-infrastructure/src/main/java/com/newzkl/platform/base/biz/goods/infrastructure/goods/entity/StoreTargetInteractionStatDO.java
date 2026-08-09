package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

/**
 * 门店-对象互动统计DO（数据库映射实体）
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class StoreTargetInteractionStatDO extends BaseDO {

    /**
     * 门店ID
     */
    @Index
    private Long storeId;

    /**
     * 发布者ID
     */
    @Index
    private Long publisherId;

    /**
     * 互动对象类型
     */
    @Index
    private InteractionEnum.TargetTypeEnum targetType;

    /**
     * 互动对象ID
     */
    @Index
    private Long targetId;

    /**
     * 浏览次数
     */
    private Integer viewCount = 0;

    /**
     * 点赞次数
     */
    private Integer likeCount = 0;

    /**
     * 分享次数
     */
    private Integer shareCount = 0;

    /**
     * 扩展信息
     * @ext JSON
     */
    @JsonSerializable
    private String extJson = "{}";
}
