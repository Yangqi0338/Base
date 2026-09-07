package com.newzkl.platform.base.biz.account.application.service.policy.identity;

import com.newzkl.platform.base.biz.account.domain.policy.AbsIdentityPolicy;
import com.newzkl.platform.base.biz.account.domain.repository.EmpRepository;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.EmpVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author muc_fang
 * @Description: 员工角色策略
 * @date 2024/1/911:42
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmpIdentityPolicy extends AbsIdentityPolicy {

    private final EmpRepository empRepository;

    @Override
    public AccountEnum.Identity support() {
        return AccountEnum.Identity.EMP;
    }

    @Override
    public IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq) {
        Long accountId = customSaveReq.getId();
        // 建员工身份行: 主键与 account 行共用, 类型默认普通员工
        EmpVO emp = new EmpVO();
        emp.setId(accountId);
        emp.setType(AccountEnum.EmpType.SIMPLE);
        empRepository.save(emp);
        // 按入参绑定账号-角色
        bindRoles(accountId, customSaveReq.getRoleIdList());
        return new IdentityRegisterRes(null, accountId);
    }

    @Override
    public boolean destroy(AccountVO accountVO, String destroyReason) {
        Long accountId = accountVO.getId();
        // 移除员工身份: 删 emp 行并解绑账号-角色
        empRepository.delete(Collections.singletonList(accountId));
        unbindRoles(accountId);
        return true;
    }

    @Override
    public void saveByAccount(AccountReq req) {

    }
}
