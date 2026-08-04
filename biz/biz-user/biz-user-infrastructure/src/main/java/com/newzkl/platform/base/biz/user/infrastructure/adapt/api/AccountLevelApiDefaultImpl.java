package com.newzkl.platform.base.biz.user.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.user.domain.adapt.api.AccountLevelApi;
import com.newzkl.platform.base.biz.user.domain.adapt.api.PackUpCheckCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * {@code AccountLevelApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 账号等级校验归 biz-account，端口待接线。
 * <b>生产必须接线，否则等级校验失效。</b>未接线前返回 1（放行）保证本域可独立编排与测试；
 * 入口 starter 侧应以远程 consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Slf4j
@Component("userAccountLevelApiDefaultImpl")
public class AccountLevelApiDefaultImpl implements AccountLevelApi {

    @Override
    public Integer packUpCheck(PackUpCheckCommand command) {
        log.warn("AccountLevelApi 未接线，礼包升级校验默认放行(返回 1)，生产必须接线，accountId={}",
                command == null ? null : command.getAccountId());
        return 1;
    }
}
