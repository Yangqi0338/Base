package com.newzkl.platform.base.biz.sys.model.dictitem.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典条目分页查询。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictItemQuery extends BizPageQuery {

    /**
     * 父字典 id 过滤。
     */
    private Long dictId;
}
