package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 身份账号查询入参
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class IdentityAccountQuery extends BizPageQuery {

    /** 账号 */
    private String username;
    /** 昵称 */
    private String nickname;
    /** 真实姓名 */
    private String realName;
    /** 邀请人ID */
    private Long invitedId;
    /** 搜索关键词 */
    private String search;

}
