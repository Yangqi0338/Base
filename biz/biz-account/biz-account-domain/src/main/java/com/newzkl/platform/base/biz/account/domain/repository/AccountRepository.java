package com.newzkl.platform.base.biz.account.domain.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.support.VerificationCodeReq;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.res.SubAccount;
import com.newzkl.platform.base.biz.account.model.res.UpIdRes;
import com.newzkl.platform.base.biz.account.model.res.UserCountRes;
import com.newzkl.platform.base.biz.account.model.vo.*;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2211:44
 */
public interface AccountRepository {

    /**
     * 保存账号
     *
     * @param account 账号实体
     */
    boolean accountSave(AccountVO account);

    /**
     * 删除账号
     *
     * @param idList 账号ID列表
     */
    void accountDelete(List<Long> idList);

    /**
     * 编辑账号
     *
     * @param account 账号实体
     */
    boolean accountEdit(AccountVO account, AccountQuery query);

    /**
     * 查询账号
     *
     * @param client 客户端
     * @param id 账号ID
     * @return 账号VO
     */
    AccountVO account(CommonEnum.Client client, Long id);

    /**
     * 查询账号
     *
     * @param query 查询条件
     * @return 账号VO
     */
    AccountVO account(AccountQuery query);

    /**
     * 查询账号
     *
     * @param query 查询条件
     * @return 账号VO
     */
    List<AccountVO> accountList(AccountQuery query);

    /**
     * 查询账号分页
     *
     * @param query 查询条件
     * @return 账号VO
     */
    Page<AccountVO> accountPage(AccountQuery query);

    <T> List<T> listObj(AccountQuery query, Class<T> clazz);

    <T> Page<T> pageObj(AccountQuery query, Class<T> clazz);

    <T> T getOne(AccountQuery query, Class<T> clazz);

    /**
     * 按条件统计账号数量
     *
     * @param accountQuery 查询条件
     * @return 账号数量
     */
    int accountCountByQuery(AccountQuery accountQuery);

    /**
     * 保存子账号
     *
     * @param subAccount 子账号实体
     */
    void subAccountSave(SubAccount subAccount);

    /**
     * 编辑子账号
     *
     * @param subAccount 子账号实体
     */
    void subAccountEdit(SubAccount subAccount);

    /**
     * 查询子账号
     *
     * @param id 子账号ID
     * @return 子账号实体
     */
    SubAccount subAccount(Long id);

    /**
     * 根据查询条件查找账号ID
     *
     * @param accountQuery 查询条件
     * @return 账号ID
     */
    Long findId(AccountQuery accountQuery);

    /**
     * 根据查询条件查找账号ID列表
     *
     * @param accountQuery 查询条件
     * @return 账号ID
     */
    List<Long> findIdList(AccountQuery accountQuery);

    /**
     * 注销角色
     *
     * @param accountVO      账号
     * @param role 注销角色
     */
    boolean destroy(AccountVO accountVO, RoleEnum.CompanyRole role);

    /**
     * 获取渠道运营商ID
     *
     * @param accountId 账号ID
     * @return 上级ID结果
     */
    UpIdRes channelOperatorId(Long accountId);

    /**
     * 根据账号ID查询范围内子账号结构
     *
     * @param accountId 账号ID
     * @return 子账号结构列表
     */
    List<AccountStructureVO> findScopeSubAccountStructure(Long accountId);

    /**
     * 统计指定账号下的子账号数量
     *
     * @param query 账号ID
     * @return 子账号数量
     */
    Long selectCount(AccountQuery query);

    /**
     * 查询运营商账号列表
     *
     * @return 运营商账号列表查询结果
     */
    OperatorListQueryVO operatorAccountListQuery();

    /**
     * 查询账号及其子账号的RPC列表
     *
     * @param accountList 账号ID列表
     * @return 账号RPC结果列表
     */
    List<AccountRPCResVO> accountAndSonAccountRpcList(List<Long> accountList);

    /**
     * 查询账单用户RPC列表
     *
     * @return 账单用户RPC列表
     */
    List<AccountBillUserRpcVO> accountBillUserRpcList();

    /**
     * 根据用户账号查询账号
     *
     * @param userAccount
     * @return
     */
    AccountVO selectByUserAccount(String userAccount);

    List<AccountVO> listAccountByIds(List<Long> accountIdList);

    void verificationCode(VerificationCodeReq verificationCodeReq);

    UserCountRes userCount();

    List<GroupCountRes> groupCount(TimeQuery timeQuery);
}
