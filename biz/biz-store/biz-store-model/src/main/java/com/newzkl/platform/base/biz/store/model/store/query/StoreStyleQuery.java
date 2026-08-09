package com.newzkl.platform.base.biz.store.model.store.query;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

/**
 * 门店样式领域对象
 */
@Data
public class StoreStyleQuery extends PageQuery {

    /**
     * 查询内容
     * @ext 匹配样式名称/ID
     */
    private String searchContent;

    /**
     * 状态
     */
    private CommonEnum.YesOrNo state;

}