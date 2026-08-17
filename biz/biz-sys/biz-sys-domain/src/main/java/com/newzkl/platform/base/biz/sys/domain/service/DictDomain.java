package com.newzkl.platform.base.biz.sys.domain.service;

import com.newzkl.platform.base.biz.sys.model.dict.query.DictQuery;
import com.newzkl.platform.base.biz.sys.model.dict.req.DictReq;
import com.newzkl.platform.base.biz.sys.model.dict.res.DictRes;

import java.util.List;

/**
 * 字典领域服务
 *
 * @author fang
 */
public interface DictDomain {
    /**
     * 字典创建/更新
     *
     * @param req 字典请求
     * @return 字典 id
     */
    Long dictSave(DictReq req);

    /**
     * 字典详情
     *
     * @param id 字典 id
     * @return 字典视图对象
     */
    DictRes dictVO(Long id);

    /**
     * 按业务键取字典详情
     *
     * @param code 字典业务键
     * @return 字典视图对象
     */
    DictRes dictVOByCode(Long code);

    /**
     * 字典列表 (分页降级为列表, 分页在基础设施层内部执行)
     *
     * @param dictQuery 字典查询
     * @return 字典列表
     */
    List<DictRes> dictList(DictQuery dictQuery);

    /**
     * 取指定业务键的下一个序列值, 不存在则从 1 开始并落库
     *
     * @param code 字典业务键
     * @return 下一个序列值
     */
    String nextCode(Long code);
}
