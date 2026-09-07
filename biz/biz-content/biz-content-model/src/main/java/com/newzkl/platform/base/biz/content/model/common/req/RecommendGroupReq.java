package com.newzkl.platform.base.biz.content.model.common.req;

import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 推荐人群入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.req.RecommendGroupReq}。
 * 旧 {@code ArticleCategoryController#getCategoryList} 收此入参但并未使用其字段,
 * 实际推荐人群由登录角色换算, 为保持前端契约(POST 带 body)原样保留。</p>
 *
 * @author KC
 */
@Data
public class RecommendGroupReq implements Serializable {

    /**
     * 推荐人群名称集合
     */
    private List<RecommendGroupEnum> groupNames;
}
