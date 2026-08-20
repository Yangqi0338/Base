package com.newzkl.platform.base.biz.account.domain.policy;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
// TODO[cross-domain relation]: import com.zkl.scm.user.domain.relation.service.ILevelDomain; (relation biz, 迁 biz-user)
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountStructureTreeVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.assembler.IdentityAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
// TODO[cross-domain relation]: relation.req.AccountLevelUpReq/ConditionReq/TeamUserCountReq, relation.res.PackGoodsInfo, relation.vo.LevelVO (迁 biz-user)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
