package com.newzkl.platform.base.biz.store.model.store.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

/**
 * 门店专区领域对象
 */
@Data
public class StoreZonePageReq extends PageQuery {

    /**
     * 查询内容：
     * 门店专区名称/ID
     */
    private String searchContent;

    /**
     * 状态：0 禁用,1 启用
     */
    private Integer state;

}