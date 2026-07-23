package com.newzkl.platform.base.biz.store.model.store.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

/**
 * 门店样式领域对象
 */
@Data
public class StoreStylePageQuery extends PageQuery {

    /**
     * 查询内容：
     * 样式名称/ID
     */
    private String searchContent;

    /**
     * 状态：0 禁用,1 启用
     */
    private Integer state;

}