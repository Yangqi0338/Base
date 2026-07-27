package com.newzkl.platform.base.biz.account.domain.service.impl;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.PhoneUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.repository.EmpRepository;
import com.newzkl.platform.base.biz.account.domain.service.AdminClientDomain;
import com.newzkl.platform.base.biz.account.domain.auth.repository.AuthRepository;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.EmpCreateReq;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import com.newzkl.platform.base.biz.account.model.res.EmpRes;
import com.newzkl.platform.base.biz.account.model.res.OpenSubAccountExcelData;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.EmpVO;
import com.newzkl.platform.base.biz.account.model.assembler.identity.EmpAssembler;
import com.newzkl.platform.base.common.core.utils.biz.BizUtil;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.EasyExcelUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminClientDomainImpl implements AdminClientDomain {

    private final EmpRepository empRepository;
    private final AuthRepository authRepository;
    private final AccountRepository accountRepository;
    private final EmpAssembler empAssembler;

    @Override
    public void batchEmpCreate(List<EmpCreateReq> empCreateReqList, Long parentAccountId) {

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
