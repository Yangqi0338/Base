package com.newzkl.platform.base.biz.user.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.model.task.config.query.TaskConfigQuery;
import com.newzkl.platform.base.biz.user.model.task.config.res.TaskConfigRes;

/**
 * 营销任务类型仓储接口
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.taskConfig.repository.TaskConfigRepository}。
 * 旧 Entity 出入参归一为 {@code TaskConfigRes}（贫血模型，业务规则落 domain service）。</p>
 *
 * <p>迁移说明：源 pageQuery 返回 MyBatis-Plus IPage，本仓按 {@code rules/Architecture.md}
 * 保留 {@code Page} 分页壳直返，total/pages 元数据不丢。</p>
 *
 * @author KC
 */
public interface TaskConfigRepository {

    /**
     * 新增任务类型
     *
     * @param res 任务类型
     * @return 新增后的任务类型（带ID）
     */
    TaskConfigRes save(TaskConfigRes res);

    /**
     * 按ID更新任务类型
     *
     * @param res 任务类型
     * @return 更新后的任务类型
     */
    TaskConfigRes updateById(TaskConfigRes res);

    /**
     * 按ID删除任务类型
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 按ID查询任务类型
     *
     * @param id 主键ID
     * @return 任务类型, 不存在返回 null
     */
    TaskConfigRes getById(Long id);

    /**
     * 更新启用状态
     *
     * @param id        主键ID
     * @param isEnabled 目标状态（1-启用，0-禁用）
     * @return 更新后的任务类型
     */
    TaskConfigRes updateEnableStatus(Long id, Integer isEnabled);

    /**
     * 按类型+分组查询（业务唯一键）
     *
     * @param taskType  任务类型编码
     * @param taskGroup 任务分组编码
     * @return 任务类型, 不存在返回 null
     */
    TaskConfigRes getByTypeAndGroup(Integer taskType, Integer taskGroup);

    /**
     * 分页查询任务类型
     *
     * @param query 分页查询
     * @return 任务类型分页
     */
    Page<TaskConfigRes> pageQuery(TaskConfigQuery query);

    /**
     * 检查ID是否存在
     *
     * @param id 主键ID
     * @return 是否存在
     */
    boolean existsById(Long id);

    /**
     * 累加任务数量
     *
     * @param id    主键ID
     * @param delta 增量（可为负）
     * @return 是否成功
     */
    boolean incrTaskCount(Long id, int delta);
}
