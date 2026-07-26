package com.newzkl.platform.base.biz.sys.domain.adapt.repository;

import com.newzkl.platform.base.biz.sys.model.arole.query.AroleQuery;
import com.newzkl.platform.base.biz.sys.model.arole.req.AroleReq;
import com.newzkl.platform.base.biz.sys.model.arole.res.AroleRes;

import java.util.List;

/**
 * 后台角色仓储端口。
 *
 * @author KC
 */
public interface AroleRepository {

    /**
     * 角色新增/更新。
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
     * @return 角色视图对象, 不存在返回 null
     */
    AroleRes aroleVO(Long id);

    /**
     * 角色列表 (分页在实现内部执行, 降级为列表)。
     *
     * @param query 角色查询
     * @return 角色列表, 永远非 null
     */
    List<AroleRes> aroleList(AroleQuery query);
}
