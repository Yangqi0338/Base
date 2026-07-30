package com.newzkl.platform.base.biz.user.model.interaction.query;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;

/**
 * 互动记录分页查询参数
 *
 * @author sijiwang
 */
@Data
public class InteractionPageQueryRPC implements Serializable {
    /**
     * 用户ID（由控制器自动填充当前登录用户）
     */
    private Long userId;

    /**
     * 门店 ID
     */
    private Long storeId;

    /**
     * 操作类型（LIKE/SHARE）
     */
    private String actionType;

    /**
     * 目标类型（可选，如VIDEO/PRODUCT，为空则查询所有类型）
     */
    private String targetType;

    /**
     * 页码（默认1）
     */
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNo = 1;

    /**
     * 每页条数（默认10）
     */
    @Min(value = 1, message = "每页条数不能小于1")
    private Integer pageSize = 10;
}
