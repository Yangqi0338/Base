package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.facade.AccountFacade;
import com.newzkl.platform.base.biz.account.domain.adapt.api.AccountInfoDTO;
import com.newzkl.platform.base.biz.account.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PackUpCheckCommand;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import lombok.extern.slf4j.Slf4j;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
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
@Component("userAccountApi")
public class AccountApiImpl implements AccountApi {

    @RpcReference
    private AccountFacade accountFacade;

    @Override
    public AccountGroupVO accountInfo(Long accountId) {
        // TODO 礼包真的应该在user吗？现在业务只关于运营商
        return accountFacade.accountInfo(CommonEnum.Client.OPERATOR, accountId);
    }

    @Override
    public Integer packUpCheck(PackUpCheckCommand command) {
        log.warn("AccountLevelApi 未接线，礼包升级校验默认放行(返回 1)，生产必须接线，accountId={}",
                command == null ? null : command.getAccountId());
        return 1;
    }
}
