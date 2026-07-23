package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 后台角色
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountJobVO extends BaseRes {
    /**
     * 名称
     */
    private String name;
    /**
     * 备注
     */
    private String comment;
}