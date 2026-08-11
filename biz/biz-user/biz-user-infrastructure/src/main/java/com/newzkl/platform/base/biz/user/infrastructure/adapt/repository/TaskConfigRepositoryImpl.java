package com.newzkl.platform.base.biz.user.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.TaskConfigRepository;
import com.newzkl.platform.base.biz.user.infrastructure.dao.TaskConfigDAO;
import com.newzkl.platform.base.biz.user.infrastructure.entity.TaskConfigDO;
import com.newzkl.platform.base.biz.user.model.task.config.query.TaskConfigQuery;
import com.newzkl.platform.base.biz.user.model.task.config.res.TaskConfigRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 营销任务类型仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.repository.TaskConfigRepositoryImpl}。
 * 旧实现继承 {@code ServiceImpl} 且用 MapStruct Assembler 转换，中台改为直接注入 DAO +
 * {@code TransferUtils}（{@code BeanUtils}/Assembler 均不再使用）。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class TaskConfigRepositoryImpl implements TaskConfigRepository {

    private final TaskConfigDAO taskConfigDAO;

    @Override
    public TaskConfigRes save(TaskConfigRes res) {
        TaskConfigDO doObj = TransferUtils.transfer(res, TaskConfigDO::new);
        taskConfigDAO.insert(doObj);
        return TransferUtils.transfer(doObj, TaskConfigRes::new);
    }

    @Override
    public TaskConfigRes updateById(TaskConfigRes res) {
        TaskConfigDO doObj = TransferUtils.transfer(res, TaskConfigDO::new);
        taskConfigDAO.updateById(doObj);
        return getById(doObj.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        return taskConfigDAO.deleteById(id) > 0;
    }

    @Override
    public TaskConfigRes getById(Long id) {
        return TransferUtils.transfer(taskConfigDAO.selectById(id), TaskConfigRes::new);
    }

    @Override
    public TaskConfigRes updateEnableStatus(Long id, CommonEnum.YesOrNo isEnabled) {
        TaskConfigDO updateDO = new TaskConfigDO();
        updateDO.setId(id);
        updateDO.setIsEnabled(isEnabled);
        taskConfigDAO.updateById(updateDO);
        return getById(id);
    }

    @Override
    public TaskConfigRes getByTypeAndGroup(Integer taskType, Integer taskGroup) {
        TaskConfigDO doObj = taskConfigDAO.selectOne(new BaseLambdaQueryWrapper<TaskConfigDO>()
                .notNullEq(TaskConfigDO::getTaskType, taskType)
                .notNullEq(TaskConfigDO::getTaskGroup, taskGroup)
                .last("limit 1"));
        return TransferUtils.transfer(doObj, TaskConfigRes::new);
    }

    @Override
    public Page<TaskConfigRes> pageQuery(TaskConfigQuery query) {
        Page<TaskConfigDO> doPage = taskConfigDAO.selectPage(
                RepositorySupport.page(query), taskConfigDAO.getLw(query));
        return TransferUtils.transferPage(doPage, TaskConfigRes.class);
    }

    @Override
    public boolean existsById(Long id) {
        return taskConfigDAO.exists(new BaseLambdaQueryWrapper<TaskConfigDO>()
                .notNullEq(TaskConfigDO::getId, id));
    }

    @Override
    public boolean incrTaskCount(Long id, int delta) {
        return taskConfigDAO.update(new LambdaUpdateWrapper<TaskConfigDO>()
                .setSql("task_count = IFNULL(task_count, 0) + " + delta)
                .eq(TaskConfigDO::getId, id)) > 0;
    }
}
