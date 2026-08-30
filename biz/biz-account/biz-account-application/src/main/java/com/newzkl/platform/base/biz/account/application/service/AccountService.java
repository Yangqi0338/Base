package com.newzkl.platform.base.biz.account.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.AdminDisableAccountReq;
import com.newzkl.platform.base.biz.account.model.req.AdminRegisterIdentityReq;
import com.newzkl.platform.base.biz.account.model.req.SubAccountQuery;
import com.newzkl.platform.base.biz.account.model.req.SubAccountSaveReq;
import com.newzkl.platform.base.biz.account.model.res.SubAccountDetailRes;

import java.util.Collection;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:36
 */
public interface AccountService {


    /**
     * 分页查询会员账号
     *
     * @param query
     * @return
     */
    Page<?> pageAccount(AccountQuery query);

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
     * 为账号绑定角色, 全量替换
     *
     * <p>校验账号存在后委托角色领域按当前端执行绑定与权限重算。
     * 兼容 adopt-chicken 单端 {@code EmpController#bindRoles}: 中台多端下端由当前登录 client 推导, 角色须同端。</p>
     *
     * @param accountId 账号ID
     * @param roleIds   角色ID集合, 空集视为清空
     */
    void bindRoles(Long accountId, Collection<Long> roleIds);

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
