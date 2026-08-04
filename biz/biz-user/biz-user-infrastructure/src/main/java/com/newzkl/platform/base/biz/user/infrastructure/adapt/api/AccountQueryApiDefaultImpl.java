package com.newzkl.platform.base.biz.user.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.user.domain.adapt.api.AccountInfoDTO;
import com.newzkl.platform.base.biz.user.domain.adapt.api.AccountQueryApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * {@code AccountQueryApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 账号查询归 biz-account，端口待接线。未接线前返回 null，
 * 入口 starter 侧应以远程 consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Slf4j
@Component("userAccountQueryApiDefaultImpl")
public class AccountQueryApiDefaultImpl implements AccountQueryApi {

    @Override
    public AccountInfoDTO accountInfo(Long accountId) {
        log.warn("AccountQueryApi 未接线，账号查询返回 null，accountId={}", accountId);
        return null;
    }
}
