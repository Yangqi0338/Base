package com.newzkl.platform.base.biz.user.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.model.task.info.query.TaskInfoQuery;
import com.newzkl.platform.base.biz.user.model.task.info.res.TaskInfoRes;

/**
 * 营销任务仓储端口
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.taskInfo.repository.TaskInfoRepository}。</p>
 *
 * <p>迁移说明：旧 {@code pageQuery} 用平铺参数 + 返回 {@code IPage}，中台改为收敛到
 * {@code TaskInfoQuery}，按 {@code rules/Architecture.md} 保留 {@code Page} 分页壳直返。</p>
 *
 * @author KC
 */
public interface TaskInfoRepository {

    /**
     * 新增任务
     *
     * @param res 任务数据
     * @return 新增后的任务
     */
    TaskInfoRes save(TaskInfoRes res);

    /**
     * 按ID更新任务
     *
     * @param res 任务数据
     * @return 更新后的任务
     */
    TaskInfoRes updateById(TaskInfoRes res);

    /**
     * 按ID删除任务
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 按ID查询任务
     *
     * @param id 主键ID
     * @return 任务，不存在返回 null
     */
    TaskInfoRes getById(Long id);

    /**
     * 更新显示状态
     *
     * @param id     主键ID
     * @param isShow 目标显示状态
     * @return 更新后的任务
     */
    TaskInfoRes updateShowStatus(Long id, Integer isShow);

    /**
     * 按任务编号查询
     *
     * @param taskNum 任务编号
     * @return 任务，不存在返回 null
     */
    TaskInfoRes getByTaskNum(String taskNum);

    /**
     * 分页查询任务
     *
     * @param query 分页查询
     * @return 任务分页
     */
    Page<TaskInfoRes> pageQuery(TaskInfoQuery query);

    /**
     * 判断ID是否存在
     *
     * @param id 主键ID
     * @return 是否存在
     */
    boolean existsById(Long id);

    /**
     * 判断任务编号是否存在
     *
     * @param taskNum 任务编号
     * @return 是否存在
     */
    boolean existsByTaskNum(String taskNum);
}
