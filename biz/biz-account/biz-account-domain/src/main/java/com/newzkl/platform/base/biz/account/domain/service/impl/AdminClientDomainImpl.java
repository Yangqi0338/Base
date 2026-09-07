package com.newzkl.platform.base.biz.account.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.repository.EmpRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.AdminClientDomain;
import com.newzkl.platform.base.biz.account.model.assembler.identity.EmpAssembler;
import com.newzkl.platform.base.biz.account.model.dto.EmpDTO;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.EmpCreateReq;
import com.newzkl.platform.base.biz.account.model.req.EmpSaveCommand;
import com.newzkl.platform.base.biz.account.model.res.EmpRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.EmpVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminClientDomainImpl implements AdminClientDomain {

    private final EmpRepository empRepository;
    private final AccountRepository accountRepository;
    private final EmpAssembler empAssembler;
    private final AccountDomain accountDomain;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchEmpCreate(List<EmpCreateReq> empCreateReqList, Long parentAccountId) {
        if (CollUtil.isEmpty(empCreateReqList)) {
            return;
        }
        if (parentAccountId == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "主账号ID不能为空");
        }
        AccountEnum.Client client = AccountEnum.Client.ADMIN;
        AccountVO parentAccount = accountRepository.account(client, parentAccountId);
        if (parentAccount == null) {
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }
        for (EmpCreateReq req : empCreateReqList) {
            empCreate(req, parentAccount, client);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int empEdit(Long id, EmpSaveCommand command) {
        // nickname / realName / phone / head / pid 已随建模收敛到 account 表(emp 表无该五列), 命中时先落账号侧
        if (StrUtil.isNotBlank(command.getNickname()) || StrUtil.isNotBlank(command.getRealName())
                || StrUtil.isNotBlank(command.getPhone()) || StrUtil.isNotBlank(command.getHead())
                || command.getPid() != null) {
            AccountReq accountReq = TransferUtils.transfer(command, AccountReq::new,
                    (source, target) -> {
                        target.setId(id);
                        target.setIdentity(AccountEnum.Identity.EMP);
                    });
            accountDomain.accountEdit(accountReq);
        }
        // emp 表只有 type 一列, 未传时不空转一次 UPDATE
        if (command.getType() == null) {
            return 0;
        }
        EmpVO item = TransferUtils.transfer(command, EmpVO::new, (c, v) -> v.setId(id));
        return empRepository.edit(item);
    }

    @Override
    public EmpDTO empBase(Long empId) {
        return empAssembler.vo2DTO(this.loadEmp(empId));
    }

    @Override
    public EmpRes empDetail(Long empId) {
        EmpRes res = empAssembler.vo2Res(this.loadEmp(empId));
        // 副数据: 账号侧展示字段。empId 同时是账号ID, 账号缺失时 accountDomain 内部抛 NO_EXIST
        AccountVO account = accountDomain.account(AccountEnum.Client.ADMIN, empId);
        res.setUsername(account.getUsername());
        res.setRealName(account.getRealName());
        res.setNickname(account.getNickname());
        res.setHead(account.getHead());
        res.setPhone(account.getPhone());
        res.setYqm(account.getYqm());
        res.setAccountState(account.getState());
        res.setLastLoginTime(account.getLastLoginTime());
        res.setPid(account.getPid());
        return res;
    }

    /**
     * 按ID取员工主数据, 不存在直接抛
     *
     * @param empId 员工账号ID
     * @return 员工视图
     */
    private EmpVO loadEmp(Long empId) {
        EmpVO emp = empRepository.emp(empId);
        if (emp == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "员工");
        }
        return emp;
    }

    /**
     * 单个员工落库
     *
     * <p>旧 {@code EmpDomainImpl.empCreate} 一行写完 {@code emp} 表 (自带 username/password/account_id);
     * Base 拆表后先写 {@code account} 再写同主键的 {@code emp}</p>
     *
     * @param req           员工新增请求
     * @param parentAccount 主账号
     * @param client        端
     */
    private void empCreate(EmpCreateReq req, AccountVO parentAccount, AccountEnum.Client client) {
        String username = req.getUsername();
        if (StrUtil.equals(parentAccount.getUsername(), username)) {
            throw new PlatformException(AccountErrorCode.EXIST_USERNAME);
        }
        AccountQuery existQuery = new AccountQuery();
        existQuery.setUsername(username);
        existQuery.setClient(client);
        if (accountRepository.selectCount(existQuery) > 0) {
            throw new PlatformException(AccountErrorCode.EXIST_USERNAME);
        }

        // 企业角色: 旧入参 companyRoleId 为空时退化为平台员工, 与旧 emp 默认语义一致
        List<AccountEnum.Identity> identityList = CollUtil.isEmpty(req.getCompanyRoleId())
                ? List.of(AccountEnum.Identity.EMP)
                : req.getCompanyRoleId().stream().map(AccountEnum.Identity::getByCode).filter(Objects::nonNull).toList();
        if (CollUtil.isEmpty(identityList)) {
            throw new PlatformException(AccountErrorCode.NOT_AVAIL_ROLE);
        }
        identityList.forEach(identity -> {
            if (!StrUtil.contains(parentAccount.getIdentityList(), identity.getCodeStr())) {
                throw new PlatformException(AccountErrorCode.NOT_OPEN_ROLE);
            }
        });

        Long accountId = SnowflakeGenerator.getSnowflakeId();
        AccountVO account = new AccountVO();
        account.init(identityList, username, null, null);
        account.setId(accountId);
        account.setPassword(account.getNewPassword(req.getPassword()));
        accountRepository.accountSave(account);

//        EmpVO emp = new EmpVO();
//        emp.setId(accountId);
//        emp.setType(AuthEnum.EmpType.SIMPLE);
//        if (req.getRoleId() != null) {
//            emp.setJobIdList(String.valueOf(req.getRoleId()));
//        }
//        empRepository.save(emp);
    }
}
