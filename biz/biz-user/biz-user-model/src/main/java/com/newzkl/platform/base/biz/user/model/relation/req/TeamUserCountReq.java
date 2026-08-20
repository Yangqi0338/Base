package com.newzkl.platform.base.biz.user.model.relation.req;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 团队用户数量请求
 *
 * @author fang
 */
@Data
public class TeamUserCountReq implements Serializable {
    /**
     * 角色等级
     */
    private Integer level;
    /**
     * 角色id
     */
    private AccountEnum.Identity type;
    /**
     * 数量
     */
    private Integer count;
}
