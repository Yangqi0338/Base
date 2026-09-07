package com.newzkl.platform.base.biz.account.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.AdminDisableAccountReq;
import com.newzkl.platform.base.biz.account.model.req.AdminRegisterIdentityReq;
import com.newzkl.platform.base.biz.account.model.req.SubAccountQuery;
import com.newzkl.platform.base.biz.account.model.req.SubAccountSaveReq;
import com.newzkl.platform.base.biz.account.model.res.AccountAggRes;
import com.newzkl.platform.base.biz.account.model.res.SubAccountDetailRes;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;

import java.util.Collection;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:36
 */
public interface AccountService {


    /**
     * 账号 + 身份聚合分页
     * <p>
     * 按 query.identityList 跨端分页查 account (不按 client 过滤), 再按本页 ID 批量回填对应身份表。
     * 传了哪些身份就回填哪些槽; 身份行缺失时槽为 null, 该行仍保留, 以保证 records.size() 与 total 一致。
     * 角色按账号各自 client 分组查编码列表。
     *
     * @param query 聚合分页入参, identityList 必填 (controller 端非 admin 经 setIdentity 注入单值)
     * @return 聚合分页结果, 空结果返回空 Page 而非 null
     */
    Page<AccountAggRes> aggPage(AccountQuery query);

    /**
     * 账号 + 身份聚合详情
     * <p>
     * 与 aggPage 同一套契约: 账号主体 (不含密码) + 角色编码列表 + 身份槽。传了哪些身份就回填哪些槽,
     * 支持一个账号多身份。account 按 id 查, 不按 client 过滤。
     *
     * @param identityList 身份列表, 决定回填哪些身份槽
     * @param accountId    账号 ID, 必填
     * @return 聚合根, 永不为 null
     */
    AccountAggRes aggDetail(List<AccountEnum.Identity> identityList, Long accountId);

    /**
     * 禁用或者启用会员
     *
     * @param req
     */
    void disableAccount(AdminDisableAccountReq req);

    /**
     * 身份创建
     *
     * @param accountReq 账号创建请求
     */
    Long identityCreate(AdminRegisterIdentityReq accountReq);

    /**
     * 主账号新增/编辑子账号
     *
     * <p>id 空走新增: 子账号继承主账号 identity, pid=pidList头=mainAccountId=当前登录主账号id,
     * origin=MAIN_CREATE, password 必填, username 主账号内唯一。id 非空走编辑: 归属校验后改
     * 昵称/手机号, password 非空则重置。两路均按 roleIds 全量替换端内角色。子账号自身的改密/改名/注销
     * 由子账号自理(走 AuthController), 主账号侧不提供</p>
     *
     * @param req 子账号新增/编辑请求
     * @return 子账号id
     */
    Long subSave(SubAccountSaveReq req);

    /**
     * 分页当前主账号名下的子账号
     *
     * @param query 子账号查询(mainAccountId 由登录态注入)
     * @return 子账号详情分页
     */
    Page<SubAccountDetailRes> subPage(SubAccountQuery query);

    /**
     * 逻辑删除子账号并清空其角色关系
     *
     * <p>归属校验后逻辑删账号(del_flag=1)并清空该账号 ACCOUNT_ROLE 绑定。完全由主账号控制</p>
     *
     * @param id 子账号id
     */
    void subDelete(Long id);
}
