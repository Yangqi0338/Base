package com.newzkl.platform.base.biz.sys.domain.adapt.repository;

import com.newzkl.platform.base.biz.sys.model.adminaccount.query.AdminAccountQuery;
import com.newzkl.platform.base.biz.sys.model.adminaccount.req.AdminAccountReq;
import com.newzkl.platform.base.biz.sys.model.adminaccount.res.AdminAccountRes;

import java.util.List;

/**
 * 平台账号仓储端口。
 *
 * @author KC
 */
public interface AdminAccountRepository {

    /**
     * 账号新增/更新 (密码需上层预加密)。
     *
     * @param req 账号请求
     * @return 账号 id
     */
    Long adminAccountSave(AdminAccountReq req);

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
     * @return 账号视图对象, 不存在返回 null
     */
    AdminAccountRes adminAccountVO(Long id);

    /**
     * 按登录名查账号 (含密码密文, 供登录校验)。
     *
     * @param username 登录名
     * @return 账号视图对象, 不存在返回 null
     */
    AdminAccountRes adminAccountByUsername(String username);

    /**
     * 账号列表 (分页在实现内部执行, 降级为列表)。
     *
     * @param query 账号查询
     * @return 账号列表, 永远非 null
     */
    List<AdminAccountRes> adminAccountList(AdminAccountQuery query);
}
