package com.newzkl.platform.base.biz.sys.model.arole.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 后台角色分页查询。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AroleQuery extends BizPageQuery {

    /**
     * 角色名称 (模糊)。
     */
    private String name;
}
