package com.newzkl.platform.base.biz.user.domain.service;

import cn.hutool.core.lang.Pair;
import com.newzkl.platform.base.biz.user.model.relation.req.ConditionReq;
import com.newzkl.platform.base.biz.user.model.relation.req.LevelQuery;
import com.newzkl.platform.base.biz.user.model.relation.req.LevelReq;
import com.newzkl.platform.base.biz.user.model.relation.res.LevelDTO;
import com.newzkl.platform.base.biz.user.model.relation.vo.LevelVO;

import java.util.List;

/**
 * 等级领域服务接口。
 *
 * <p>迁移说明：源 pageList 返回 MyBatis-Plus Page，降级为 List。
 * TODO[page-meta] total 等元数据跨层丢失。</p>
 *
 * @author fang
 */
public interface LevelDomain {
    /**
     * 等级创建。
     *
     * @param levelCommand 等级请求
     * @return 主键ID
     */
    Long save(LevelReq levelCommand);

    /**
     * 等级列表（分页降级为 List）。
     *
     * @param levelQuery 查询条件
     * @return 等级视图列表
     */
    List<LevelVO> pageList(LevelQuery levelQuery);

    /**
     * 等级计算：null 表示不满足任何等级。
     *
     * @param conditionCommand 条件参数
     * @return 命中的等级视图与进度
     */
    Pair<LevelVO, Double> executeLevel(ConditionReq conditionCommand);

    /**
     * 等级列表。
     *
     * @param levelQuery 查询条件
     * @return 等级列表
     */
    List<LevelDTO> list(LevelQuery levelQuery);

    /**
     * 更新等级。
     *
     * @param levelCommand 等级请求
     */
    void update(LevelReq levelCommand);

    /**
     * 根据查询条件获取单个等级。
     *
     * @param levelQuery 查询条件
     * @return 等级
     */
    LevelDTO findByQuery(LevelQuery levelQuery);
}
