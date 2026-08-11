package com.newzkl.platform.base.biz.user.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.TaskInfoRepository;
import com.newzkl.platform.base.biz.user.infrastructure.dao.TaskInfoDAO;
import com.newzkl.platform.base.biz.user.infrastructure.entity.TaskInfoDO;
import com.newzkl.platform.base.biz.user.model.task.info.query.TaskInfoQuery;
import com.newzkl.platform.base.biz.user.model.task.info.res.TaskInfoRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 营销任务仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.repository.TaskInfoRepositoryImpl}。
 * 旧实现继承 {@code ServiceImpl} 且用 MapStruct Assembler 转换，中台改为直接注入 DAO +
 * {@code TransferUtils}。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class TaskInfoRepositoryImpl implements TaskInfoRepository {

    private final TaskInfoDAO taskInfoDAO;

    @Override
    public TaskInfoRes save(TaskInfoRes res) {
        TaskInfoDO doObj = TransferUtils.transfer(res, TaskInfoDO::new);
        taskInfoDAO.insert(doObj);
        return TransferUtils.transfer(doObj, TaskInfoRes::new);
    }

    @Override
    public TaskInfoRes updateById(TaskInfoRes res) {
        TaskInfoDO doObj = TransferUtils.transfer(res, TaskInfoDO::new);
        taskInfoDAO.updateById(doObj);
        return getById(doObj.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        return taskInfoDAO.deleteById(id) > 0;
    }

    @Override
    public TaskInfoRes getById(Long id) {
        return TransferUtils.transfer(taskInfoDAO.selectById(id), TaskInfoRes::new);
    }

    @Override
    public TaskInfoRes updateShowStatus(Long id, CommonEnum.YesOrNo isShow) {
        TaskInfoDO updateDO = new TaskInfoDO();
        updateDO.setId(id);
        updateDO.setIsShow(isShow);
        taskInfoDAO.updateById(updateDO);
        return getById(id);
    }

    @Override
    public TaskInfoRes getByTaskNum(String taskNum) {
        TaskInfoDO doObj = taskInfoDAO.selectOne(new BaseLambdaQueryWrapper<TaskInfoDO>()
                .notNullEq(TaskInfoDO::getTaskNum, taskNum)
                .last("limit 1"));
        return TransferUtils.transfer(doObj, TaskInfoRes::new);
    }

    @Override
    public Page<TaskInfoRes> pageQuery(TaskInfoQuery query) {
        Page<TaskInfoDO> doPage = taskInfoDAO.selectPage(
                RepositorySupport.page(query), taskInfoDAO.getLw(query));
        return TransferUtils.transferPage(doPage, TaskInfoRes.class);
    }

    @Override
    public boolean existsById(Long id) {
        return taskInfoDAO.exists(new BaseLambdaQueryWrapper<TaskInfoDO>()
                .notNullEq(TaskInfoDO::getId, id));
    }

    @Override
    public boolean existsByTaskNum(String taskNum) {
        return taskInfoDAO.exists(new BaseLambdaQueryWrapper<TaskInfoDO>()
                .notNullEq(TaskInfoDO::getTaskNum, taskNum));
    }
}
