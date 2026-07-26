package com.newzkl.platform.base.biz.sys.domain.service;

import com.newzkl.platform.base.biz.sys.model.adminaccount.query.AdminAccountQuery;
import com.newzkl.platform.base.biz.sys.model.adminaccount.req.AdminAccountReq;
import com.newzkl.platform.base.biz.sys.model.adminaccount.req.PasswordUpdateReq;
import com.newzkl.platform.base.biz.sys.model.adminaccount.res.AdminAccountRes;

import java.util.List;

/**
 * 平台账号领域服务。
 *
 * <p>[AUTH] 登录 (passwordLogin) / token 签发 / 当前登录注入归入口 starter (building-scm),
 * 本域仅暴露账号 CRUD + 密码校验 {@link #passwordVerify(String, String)}。</p>
 *
 * @author KC
 */
public interface AdminAccountDomain {

    /**
     * 账号创建 (明文密码 BCrypt 加密后落库)。
     *
     * @param req 账号请求
     * @return 账号 id
     */
    Long adminAccountCreate(AdminAccountReq req);

    /**
     * 账号更新 (传密码则重新 BCrypt 加密)。
     *
     * @param req 账号请求 (id 必填)
     */
    void adminAccountUpdate(AdminAccountReq req);

    /**
     * 账号删除。
     *
     * @param idList 账号 id 列表
     */
    void adminAccountDelete(List<Long> idList);

    /**
     * 账号详情。
     *
     * @param id 账号 id
     * @return 账号视图对象
     */
    AdminAccountRes adminAccountVO(Long id);

    /**
     * 账号列表。
     *
     * @param query 账号查询
     * @return 账号列表
     */
    List<AdminAccountRes> adminAccountList(AdminAccountQuery query);

    /**
     * 密码校验 (供入口 starter 登录动作: 校验通过返回账号, token 签发在入口)。
     *
     * @param username    登录名
     * @param rawPassword 明文密码
     * @return 校验通过的账号视图对象
     */
    AdminAccountRes passwordVerify(String username, String rawPassword);

    /**
     * 密码修改 (校验原密码, 更新为新密码密文)。
     *
     * @param req 密码修改请求
     */
    void passwordUpdate(PasswordUpdateReq req);
}
