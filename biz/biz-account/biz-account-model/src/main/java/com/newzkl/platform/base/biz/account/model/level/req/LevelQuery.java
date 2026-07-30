package com.newzkl.platform.base.biz.account.model.level.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 等级查询入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.level.model.query.LevelQuery}。
 * 旧 {@code id} / {@code idList} 字段由父类 {@code BizPageQuery} 提供, 此处不重复声明。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LevelQuery extends BizPageQuery {

    /**
     * 类型 (角色 ID)
     */
    private Integer type;

    /**
     * 等级值
     */
    private Integer value;
}
