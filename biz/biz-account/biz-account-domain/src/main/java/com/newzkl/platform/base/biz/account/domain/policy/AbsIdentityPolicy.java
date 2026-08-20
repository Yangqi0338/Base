package com.newzkl.platform.base.biz.account.domain.policy;

import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import org.springframework.stereotype.Component;

/**
 * @author muc_fang
 * @Description: 角色策略
 * @date 2024/1/911:42
 */
@Component
public abstract class AbsIdentityPolicy {

    public abstract AccountEnum.Identity support();

    /**
     * 个人注册角色
     * @param customSaveReq
     * @return
     */
    public abstract IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq);

    public abstract Object detail(Long id);

    public abstract boolean destroy(AccountVO accountVO, String destroyReason);

    public abstract void saveByAccount(AccountReq req);
}
