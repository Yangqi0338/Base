package com.newzkl.platform.base.biz.socialbang.model.event.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 结算明细分页查询请求对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SettleDetailReq extends BizPageQuery {

    /**
     * 结算id
     */
    private String settlementId;

    /**
     * 角色等级
     */
    private String roleName;


    /**
     * 角色名称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String phone;
}
