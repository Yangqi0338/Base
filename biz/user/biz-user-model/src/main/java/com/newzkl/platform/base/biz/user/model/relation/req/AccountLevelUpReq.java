package com.newzkl.platform.base.biz.user.model.relation.req;

import cn.hutool.core.lang.Opt;
import lombok.Data;

import java.io.Serializable;

/**
 * 账号升级请求。
 *
 * @author fang
 */
@Data
public class AccountLevelUpReq extends ConditionReq implements Serializable {

    /**
     * 下单账号id
     */
    private Long accountId;

    /**
     * 获取升级账号id。
     *
     * @return 升级账号id
     */
    public Long findLevelUpAccountId() {
        return Opt.ofNullable(this.getId()).orElse(this.getAccountId());
    }

}
