package com.newzkl.platform.base.biz.store.model.store.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

/**
* 席位套餐分页查询
*/
@Data
public class SeatPackagePageReq extends PageQuery {

    /**
     * 查询内容：
     * 套餐名称/ID
     */
    private String searchContent;

    /**
     * 状态：0 禁用,1 启用
     */
    private Integer state;

    /**
     * 角色id
     */
    private Long roleId;

}
