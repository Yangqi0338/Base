package com.newzkl.platform.base.biz.account.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.AdminDisableAccountReq;
import com.newzkl.platform.base.biz.account.model.req.AdminRegisterIdentityReq;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.MemberAccountVO;

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
    void bindRoles(Long accountId, java.util.Collection<Long> roleIds);
}
