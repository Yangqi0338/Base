package com.newzkl.platform.base.biz.user.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.TaskConfigRepository;
import com.newzkl.platform.base.biz.user.domain.service.TaskConfigDomain;
import com.newzkl.platform.base.biz.user.model.task.config.query.TaskConfigQuery;
import com.newzkl.platform.base.biz.user.model.task.config.req.TaskConfigOperateReq;
import com.newzkl.platform.base.biz.user.model.task.config.res.TaskConfigRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 营销任务类型领域服务实现
 *
 * <p>迁移自旧 {@code TaskConfigDomainServiceImpl}。旧实体自带 {@code isValidTypeAndGroup} 充血校验，
 * 中台贫血模型下改为本层显式校验。</p>
 *
 * @author KC
 */
@Slf4j
@Service("taskConfigDomainImpl")
@RequiredArgsConstructor
public class TaskConfigDomainImpl implements TaskConfigDomain {

    private final TaskConfigRepository taskConfigRepository;

    /**
     * 校验任务类型与分组合法性
     *
     * @param taskType  任务类型编码
     * @param taskGroup 任务分组编码
     */
    private void validateTypeAndGroup(Integer taskType, Integer taskGroup) {
        if (taskType == null || taskType <= 0 || taskGroup == null || taskGroup <= 0) {
            throw new PlatformException(BaseErrorCode.PARAM, "任务类型/分组不合法");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskConfigRes create(TaskConfigOperateReq req) {
        validateTypeAndGroup(req.getTaskType(), req.getTaskGroup());
        if (req.getId() != null) {
            throw new PlatformException(BaseErrorCode.PARAM, "新增时ID必须为空");
        }
        if (taskConfigRepository.getByTypeAndGroup(req.getTaskType(), req.getTaskGroup()) != null) {
            throw new PlatformException(BaseErrorCode.EXIST_DATA, "任务类型+分组");
        }

        TaskConfigRes res = TransferUtils.transfer(req, TaskConfigRes::new, (source, target) ->
                target.setIsEnabled(source.getIsEnabled() == null ? 1 : source.getIsEnabled()));
        TaskConfigRes saved = taskConfigRepository.save(res);
        log.info("新增营销任务类型成功，ID：{}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskConfigRes update(TaskConfigOperateReq req) {
        if (req.getId() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "修改时ID不能为空");
        }
        validateTypeAndGroup(req.getTaskType(), req.getTaskGroup());
        if (!taskConfigRepository.existsById(req.getId())) {
            throw new PlatformException(BaseErrorCode.NODATA, "任务类型");
        }

        TaskConfigRes existByTypeGroup = taskConfigRepository.getByTypeAndGroup(req.getTaskType(), req.getTaskGroup());
        if (existByTypeGroup != null && !Objects.equals(existByTypeGroup.getId(), req.getId())) {
            throw new PlatformException(BaseErrorCode.EXIST_DATA, "任务类型+分组");
        }

        TaskConfigRes updated = taskConfigRepository.updateById(TransferUtils.transfer(req, TaskConfigRes::new));
        log.info("修改营销任务类型成功，ID：{}", req.getId());
        return updated;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (id == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "删除时ID不能为空");
        }
        if (!taskConfigRepository.existsById(id)) {
            throw new PlatformException(BaseErrorCode.NODATA, "任务类型");
        }
        taskConfigRepository.deleteById(id);
        log.info("删除营销任务类型成功，ID：{}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskConfigRes toggleEnable(Long id, Integer isEnabled) {
        if (id == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "状态切换时ID不能为空");
        }
        if (isEnabled == null || (isEnabled != 0 && isEnabled != 1)) {
            throw new PlatformException(BaseErrorCode.PARAM, "状态值非法，仅支持0/1");
        }
        if (!taskConfigRepository.existsById(id)) {
            throw new PlatformException(BaseErrorCode.NODATA, "任务类型");
        }
        return taskConfigRepository.updateEnableStatus(id, isEnabled);
    }

    @Override
    public TaskConfigRes getById(Long id) {
        TaskConfigRes res = taskConfigRepository.getById(id);
        if (res == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "任务类型");
        }
        return res;
    }

    @Override
    public Page<TaskConfigRes> pageQuery(TaskConfigQuery query) {
        return taskConfigRepository.pageQuery(query);
    }
}
