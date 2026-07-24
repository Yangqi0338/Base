package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 渠道商
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class IdentityAccountQuery extends BizPageQuery {

    private String username;
    private String nickname;
    private String realName;
    private Long invitedId;
    private String search;

}
