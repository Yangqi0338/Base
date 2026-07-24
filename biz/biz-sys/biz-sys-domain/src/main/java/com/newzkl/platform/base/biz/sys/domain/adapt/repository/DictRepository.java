package com.newzkl.platform.base.biz.sys.domain.adapt.repository;

import com.newzkl.platform.base.biz.sys.model.dict.query.DictQuery;
import com.newzkl.platform.base.biz.sys.model.dict.res.DictRes;

import java.util.List;

/**
 * 字典仓储端口。
 *
 * @author fang
 */
public interface DictRepository {
    /**
     * 字典新增/更新。
     *
     * @param dict 字典视图对象
     * @return 字典 id
     */
    Long dictSave(DictRes dict);

    /**
     * 字典删除。
     *
     * @param idList 字典 id 列表
     */
    void dictDelete(List<Long> idList);

    /**
     * 字典修改。
     *
     * @param dict 字典视图对象
     */
    void dictUpdate(DictRes dict);

    /**
     * 字典详情。
     *
     * @param id 字典 id
     * @return 字典视图对象
     */
    DictRes dictVO(Long id);

    /**
     * 字典列表 (分页在实现内部执行)。
     *
     * @param dictQuery 字典查询
     * @return 字典列表
     */
    List<DictRes> dictList(DictQuery dictQuery);

    /**
     * 加锁读取字典 (行锁, 用于序列自增)。
     *
     * @param id 字典 id
     * @return 字典视图对象
     */
    DictRes dictVOLock(Long id);
}
