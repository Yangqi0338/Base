package com.newzkl.platform.base.biz.user.model.interaction.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 检查/统计用户互动的查询参数
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.collection.model.vo.InteractionQuery}。
 * 旧类落 vo 包但语义是查询入参, 迁移归位 query 包。</p>
 *
 * @author KC
 */
@Data
public class InteractionQuery implements Serializable {

    /**
     * 用户ID（由控制器填充当前登录账号）
     */
    private Long userId;

    /**
     * 目标ID（如商品ID、视频ID）
     */
    @NotNull(message = "目标ID不能为空")
    private Long targetId;

    /**
     * 目标类型（对应 InteractionEnum.TargetTypeEnum 编码）
     */
    @NotBlank(message = "目标类型不能为空")
    private String targetType;

    /**
     * 操作类型（对应 InteractionEnum.ActionTypeEnum 编码）
     */
    @NotBlank(message = "操作类型不能为空")
    private String actionType;
}
