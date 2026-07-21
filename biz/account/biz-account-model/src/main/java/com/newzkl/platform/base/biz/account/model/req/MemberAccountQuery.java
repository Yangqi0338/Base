package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员+账号关联分页查询条件
 *
 * @author fang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberAccountQuery extends PageQuery {
    /**
     * 账号手机号（模糊查询）
     */
    private String phone;
    /**
     * 会员昵称（模糊查询）
     */
    private String nickname;
    /**
     * 会员账号（模糊查询）
     */
    private String userAccount;
}