package com.newzkl.platform.base.biz.account.application.service.policy.identity;

import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 脉脉通渠道商身份策略
 *
 * <p>脉脉通渠道商端 (MMT_CHANNEL) 自助注册的默认身份, 建号即由
 * {@link AbsIdentityPolicy#autoBindDefaultSuperAdmin} 自动授本端超管。当前功能与
 * 普通渠道商一致但独立成端, 暂无额外身份行 (无 channel/finance 初始化), 后续特化功能在此扩展</p>
 *
 * @author KC
 */
@Slf4j
@Component
public class MmtChannelIdentityPolicy extends AbsIdentityPolicy {

    @Override
    public AccountEnum.Identity support() {
        return AccountEnum.Identity.MMT_CHANNEL;
    }

    @Override
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        return new IdentityRegisterRes(null, customSaveReq.getId());
    }

    @Override
    public Object detail(Long id) {
        return null;
    }

    @Override
    public boolean destroy(AccountVO accountVO, String destroyReason) {
        return true;
    }

    @Override
    public void saveByAccount(AccountReq req) {

    }
}
