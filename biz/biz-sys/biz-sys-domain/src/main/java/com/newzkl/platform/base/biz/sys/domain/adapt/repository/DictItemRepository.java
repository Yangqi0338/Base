package com.newzkl.platform.base.biz.sys.domain.adapt.repository;

import com.newzkl.platform.base.biz.sys.model.dictitem.req.DictItemReq;
import com.newzkl.platform.base.biz.sys.model.dictitem.res.DictItemRes;

import java.util.List;

/**
 * 字典条目仓储端口。
 *
 * @author KC
 */
public interface DictItemRepository {

    /**
     * 条目新增/更新。
     *
     * @param req 条目请求
     * @return 条目 id
     */
    Long itemSave(DictItemReq req);

    /**
     * 按父字典 id 查条目列表。
     *
     * @param dictId 父字典 id
     * @return 条目列表, 永远非 null
     */
    List<DictItemRes> itemList(Long dictId);

    /**
     * 条目删除。
     *
     * @param idList 条目 id 列表
     */
    void itemDelete(List<Long> idList);
}
