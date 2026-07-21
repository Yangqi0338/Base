package com.newzkl.platform.base.biz.user.domain.adapt.repository;

import com.newzkl.platform.base.biz.user.model.relation.req.LevelQuery;
import com.newzkl.platform.base.biz.user.model.relation.res.Level;
import com.newzkl.platform.base.biz.user.model.relation.vo.LevelVO;

import java.util.List;

/**
 * 等级仓储接口。
 *
 * @author fang
 */
public interface LevelRepository {
    /**
     * 等级持久化。
     *
     * @param selectorLevel 等级领域模型
     * @return 主键ID
     */
    Long save(Level selectorLevel);

    /**
     * 等级详情。
     *
     * @param levelId 等级ID
     * @return 等级领域模型
     */
    Level detail(Long levelId);

    /**
     * 等级列表。
     *
     * @param levelQuery 查询条件
     * @return 等级列表
     */
    List<Level> list(LevelQuery levelQuery);

    /**
     * 等级计算列表。
     *
     * @param levelQuery 查询条件
     * @return 等级视图列表
     */
    List<LevelVO> calList(LevelQuery levelQuery);
}
