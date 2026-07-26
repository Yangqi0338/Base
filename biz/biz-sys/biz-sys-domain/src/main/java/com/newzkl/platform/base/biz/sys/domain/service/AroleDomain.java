package com.newzkl.platform.base.biz.sys.domain.service;

import com.newzkl.platform.base.biz.sys.model.arole.query.AroleQuery;
import com.newzkl.platform.base.biz.sys.model.arole.req.AroleReq;
import com.newzkl.platform.base.biz.sys.model.arole.res.AroleRes;

import java.util.List;

/**
 * 后台角色领域服务。
 *
 * @author KC
 */
public interface AroleDomain {

    /**
     * 角色创建/更新。
     *
     * @param req 角色请求
     * @return 角色 id
     */
    Long aroleSave(AroleReq req);

    /**
     * 角色删除。
     *
     * @param idList 角色 id 列表
     */
    void aroleDelete(List<Long> idList);

    /**
     * 角色详情。
     *
     * @param id 角色 id
     * @return 角色视图对象
     */
    AroleRes aroleVO(Long id);

    /**
     * 角色列表。
     *
     * @param query 角色查询
     * @return 角色列表
     */
    List<AroleRes> aroleList(AroleQuery query);
}
