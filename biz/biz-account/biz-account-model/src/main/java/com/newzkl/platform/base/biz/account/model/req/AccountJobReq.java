package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;

/**
 * 后台角色
 *
 * @author fang
 */
@Data
public class AccountJobReq extends BaseReq {
    /**
     * 名称
     */
    private String name;
    /**
     * 备注
     */
    private String comment;
}