package com.newzkl.platform.base.biz.user.model.interaction.query;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 检查/统计用户互动的查询参数
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.collection.model.vo.InteractionQuery}。
 * 旧类落 vo 包但语义是查询入参, 迁移归位 query 包。</p>
 *
 * @author KC
 */
@Data
public class InteractionQuery extends PageQuery {

    /**
     * 用户ID
     */
    private List<Long> userIdList;

    public void setUserId(Long userId) {
        this.userIdList = doWrapperList(this.userIdList,userId);
    }

    /**
     * 门店 ID
     */
    private Long storeId;

    /**
     * 目标ID
     */
    @NotEmpty
    private List<Long> targetIdList;

    public void setTargetId(Long targetId) {
        this.targetIdList = doWrapperList(this.targetIdList, targetId);
    }

    public Long getTargetId() {
        return CollUtil.getFirst(this.targetIdList);
    }

    /**
     * 目标类型
     */
    @NotNull
    private InteractionEnum.TargetTypeEnum targetType;

    /**
     * 操作类型
     */
    @NotNull
    private InteractionEnum.ActionTypeEnum actionType;
}
