package com.newzkl.platform.base.biz.account.domain.service.impl;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.enums.AuthEnum;
import com.newzkl.platform.base.biz.account.model.exception.AccountErrorCode;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.repository.EmpRepository;
import com.newzkl.platform.base.biz.account.domain.service.AdminClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.EmpCreateReq;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import com.newzkl.platform.base.biz.account.model.res.EmpRes;
import com.newzkl.platform.base.biz.account.model.res.OpenSubAccountExcelData;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.EmpVO;
import com.newzkl.platform.base.biz.account.model.assembler.identity.EmpAssembler;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.office.EasyExcelUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminClientDomainImpl implements AdminClientDomain {

    private final EmpRepository empRepository;
    private final AccountRepository accountRepository;
    private final EmpAssembler empAssembler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchEmpCreate(List<EmpCreateReq> empCreateReqList, Long parentAccountId) {
        if (CollUtil.isEmpty(empCreateReqList)) {
            return;
        }
        if (parentAccountId == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "主账号ID不能为空");
        }
        CommonEnum.Client client = CommonEnum.Client.ADMIN;
        AccountVO parentAccount = accountRepository.account(client, parentAccountId);
        if (parentAccount == null) {
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }
        for (EmpCreateReq req : empCreateReqList) {
            empCreate(req, parentAccount, client);
        }
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
    private void empCreate(EmpCreateReq req, AccountVO parentAccount, CommonEnum.Client client) {
        String username = req.getUsername();
        if (StrUtil.equals(parentAccount.getUsername(), username)) {
            throw new PlatformException(AccountErrorCode.EXIST_USERNAME);
        }
        AccountQuery existQuery = new AccountQuery();
        existQuery.setUsername(username);
        existQuery.setMainAccountId(parentAccount.getId());
        existQuery.setClient(client);
        if (accountRepository.selectCount(existQuery) > 0) {
            throw new PlatformException(AccountErrorCode.EXIST_USERNAME);
        }

        // 企业角色: 旧入参 companyRoleId 为空时退化为平台员工, 与旧 emp 默认语义一致
        List<RoleEnum.CompanyRole> roleList = CollUtil.isEmpty(req.getCompanyRoleId())
                ? List.of(RoleEnum.CompanyRole.EMP)
                : req.getCompanyRoleId().stream().map(RoleEnum.CompanyRole::getByCode).filter(Objects::nonNull).toList();
        if (CollUtil.isEmpty(roleList)) {
            throw new PlatformException(AccountErrorCode.NOT_AVAIL_ROLE);
        }
        roleList.forEach(role -> {
            if (!StrUtil.contains(parentAccount.getRoleIdList(), role.getCodeStr())) {
                throw new PlatformException(AccountErrorCode.NOT_OPEN_ROLE);
            }
        });

        Long accountId = SnowflakeIdAble.getSnowflakeId();
        AccountVO account = new AccountVO();
        account.init(roleList, username, null, parentAccount.getId(), null);
        account.setId(accountId);
        account.setPassword(account.getNewPassword(req.getPassword()));
        accountRepository.accountSave(account);

        EmpVO emp = new EmpVO();
        emp.setId(accountId);
        emp.setType(AuthEnum.EmpType.SIMPLE);
        if (req.getRoleId() != null) {
            emp.setJobIdList(String.valueOf(req.getRoleId()));
        }
        empRepository.save(emp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int empEdit(EmpCreateReq empCreateReq) {
        Long id = empCreateReq.getId();
        if (id == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "缺少ID");
        }
        int count = 0;
        AccountVO account = new AccountVO();
        account.setId(id);
        account.setUsername(empCreateReq.getUsername());
        if (StrUtil.isNotEmpty(empCreateReq.getPassword())) {
            account.setPassword(account.getNewPassword(empCreateReq.getPassword()));
        }
        if (CollUtil.isNotEmpty(empCreateReq.getCompanyRoleId())) {
            account.setRoleIdList(CollUtil.join(empCreateReq.getCompanyRoleId(), ","));
        }
        if (accountRepository.accountEdit(account, null)) {
            count++;
        }
        if (empCreateReq.getRoleId() != null) {
            EmpVO emp = new EmpVO();
            emp.setId(id);
            emp.setJobIdList(String.valueOf(empCreateReq.getRoleId()));
            count += empRepository.edit(emp);
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int empDelete(List<Long> empIdList) {
        if (CollUtil.isEmpty(empIdList)) {
            return 0;
        }
        int count = empRepository.delete(empIdList);
        accountRepository.accountDelete(empIdList);
        return count;
    }

    @Override
    public Page<EmpRes> empPage(EmpQuery empQuery) {
        Page<EmpVO> pageList = empRepository.pageList(empQuery);
        List<Long> empIdList = pageList.getRecords().stream().map(EmpVO::getId).collect(Collectors.toList());
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setIdList(empIdList);
        accountQuery.setClient(CommonEnum.Client.ADMIN);
        List<EmpRes> accountEmpList = accountRepository.listObj(accountQuery, EmpRes.class);
        return TransferUtils.transferPage(pageList, empVO -> {
            EmpRes res = empAssembler.vo2Res(empVO);
            accountEmpList.stream().filter(it -> it.getId().equals(empVO.getId())).findFirst().ifPresent(empAccount -> {
                TransferUtils.transfer(empAccount, res);
            });
            return res;
        });
    }

    @Override
    public EasyExcelErrorVO excelCreateEmp(InputStream inputStream, Long accountId) {
        Set<String> successUsernames = new HashSet<>();
        CommonEnum.Client client = CommonEnum.Client.ADMIN;
        AccountVO accountVO = accountRepository.account(client, accountId);
        return EasyExcelUtil.importBiz(inputStream, OpenSubAccountExcelData.class, (data, currentRowNum) -> {
            String username = data.getUsername();
            //校验数据
            if (!PhoneUtil.isMobile(username)) {
                return EasyExcelUtil.getMessage(currentRowNum, "手机号格式异常");
            }
            if (!successUsernames.add(username)) {
                return EasyExcelUtil.getMessage(currentRowNum, "手机号重复");
            }
            if (accountVO.getUsername().equals(username)) {
                return EasyExcelUtil.getMessage(currentRowNum, "手机号与主账号重复");
            }

            AccountQuery accountQuery = new AccountQuery();
            accountQuery.setUsername(username);
            accountQuery.setMainAccountId(accountId);
            accountQuery.setClient(client);
            long count = accountRepository.selectCount(accountQuery);
            if (count > 0) {
                return EasyExcelUtil.getMessage(currentRowNum, "手机号与主账号重复");
            }
            String[] companyRoleSplit = data.getCompanyRole().split(",");
            for (String item : companyRoleSplit) {
                List<String> roleInfoVOMap = RoleEnumUtil.findClientRoleList(client).map(RoleEnum.CompanyRole::getValue).toList();
                if (!roleInfoVOMap.contains(item)) {
                    return EasyExcelUtil.getMessage(currentRowNum, "角色仅可选择" + CollUtil.join(roleInfoVOMap, "|"));
                }
            }
//            String[] empRoleSplit = data.getEmpRole().split(",");
//            for (String item : empRoleSplit) {
//                if (!rolePageMap.containsKey(item)) {
//                    return "主账号下无员工角色: " + data.getEmpRole();
//                }
//            }
            return null;
        }, (list) -> {
            List<EmpCreateReq> subProxySaveReqList = new ArrayList<>();
            for (OpenSubAccountExcelData openSubAccountExcelData : list) {
                EmpCreateReq empCommand = new EmpCreateReq();
                empCommand.setUsername(openSubAccountExcelData.getUsername());
                //组装企业角色ID
                List<Long> companyRoleIdList = RoleEnumUtil.findClientRoleList(client)
                        .filter(it -> openSubAccountExcelData.getCompanyRole().contains(it.getValue()))
                        .map(RoleEnum.CompanyRole::getCode).toList();

                //组装岗位ID
//                String empRole = openSubAccountExcelData.getEmpRole();
//                String[] empRoleSplit = empRole.split(",");
//                List<Long> empRoleIdList = new ArrayList<>();
//                for (String empRoleName : empRoleSplit) {
//                    Long roleId = this.rolePageMap.get(empRoleName).getId();
//                    empRoleIdList.add(roleId);
//                }
//                empCommand.setRoleId(empRoleIdList.get(0));
                empCommand.setCompanyRoleId(companyRoleIdList);
                subProxySaveReqList.add(empCommand);
            }
            this.batchEmpCreate(subProxySaveReqList, SecurityUtils.getAccountId());
        });
    }
}
