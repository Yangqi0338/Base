package com.newzkl.platform.base.biz.account.model.res;


import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 后台角色
 *
 * @author fang
 */
@Data
public class AccountJobRes extends BaseRes {
    /**
     * 名称
     */
    private String name;
    /**
     * 备注
     */
    private String comment;
    /**
     * 用户数量
     */
    private Integer adminCount;
}