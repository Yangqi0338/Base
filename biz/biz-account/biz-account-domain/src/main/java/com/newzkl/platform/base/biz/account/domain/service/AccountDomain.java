package com.newzkl.platform.base.biz.account.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.auth.req.AccountSaveReq;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.AccountInfo;
import com.newzkl.platform.base.biz.account.model.res.SimpleAccountRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountStructureTreeVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.ddd.facade.IdentityRegisterRpcReq;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:44
 */
public interface AccountDomain {
    /**
     * 账号实体
     *
     * @param client
     * @param accountId 账号ID
     * @return 账号VO
     */
    @NotNull
    AccountVO account(AccountEnum.Client client, Long accountId);

    /**
     * 根据手机号查询账号信息
     *
     * @param query
     * @return 账号信息
     */
    AccountInfo accountInfo(AccountQuery query);

    /**
     * 修改基本信息
     *
     * @param req 账号基本信息请求
     */
    boolean accountEdit(AccountReq req);

    /**
     * 账号删除
     *
     * @param accountIdList 账号ID列表
     */
    void accountDelete(List<Long> accountIdList);

    /**
     * 注销角色
     *
     * @param accountId      账号ID
     * @param destroyRoleReq 注销角色请求
     */
    void destroy(Long accountId, DestroyRoleReq destroyRoleReq);

    /**
     * 查询范围内子账号结构
     *
     * @param accountId 账号ID
     * @return 子账号结构列表
     */
    List<AccountStructureTreeVO> findScopeSubAccountStructure(AccountEnum.Client client, Long accountId);

    /**
     * 个人注册
     *
     * @param customSaveReq
     */
    AccountVO customSave(AccountSaveReq customSaveReq);

    /**
     * 查询账号列表
     *
     * @param query
     */
    List<AccountVO> accountList(AccountQuery query);

    Page<SimpleAccountRes> simpleAccountPage(SimpleAccountQuery accountQuery);

    Page<AccountVO> accountPage(AccountQuery accountQuery);

    void register(IdentityRegisterRpcReq req);
}
