package com.newzkl.platform.base.biz.user.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.TaskConfigRepository;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.TaskInfoRepository;
import com.newzkl.platform.base.biz.user.domain.service.TaskInfoDomain;
import com.newzkl.platform.base.biz.user.model.task.config.res.TaskConfigRes;
import com.newzkl.platform.base.biz.user.model.task.enums.TaskTypeEnum;
import com.newzkl.platform.base.biz.user.model.task.info.query.TaskInfoQuery;
import com.newzkl.platform.base.biz.user.model.task.info.req.TaskInfoOperateReq;
import com.newzkl.platform.base.biz.user.model.task.info.res.TaskInfoRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 营销任务领域服务实现
 *
 * <p>迁移自旧 {@code TaskInfoDomainServiceImpl}。旧实体 {@code TaskEntity} 自带
 * {@code isValidTimeRange}/{@code isValidAdCount} 充血校验，中台贫血模型下改为本层显式校验。</p>
 *
 * <p>迁移说明：</p>
 * <ul>
 *   <li>{@code taskNum} 旧在 {@code TaskInfoAppServiceImpl} 生成，因旧应用层为纯透传已省略，
 *       生成逻辑下沉本层（仍用 {@code BusinessCodeUtil.generate(BusinessType.TASK_CODE)}）。</li>
 *   <li>旧 {@code isValidAdCount} 判 {@code taskType == 2}，与 {@code task_config} 字段注释及
 *       {@code TaskTypeEnum} 定义（1-观看激励广告）矛盾，属旧代码笔误；本实现按枚举取
 *       {@link TaskTypeEnum#AD_WATCH} 判定。</li>
 *   <li>{@code taskCount} 维护由旧「读配置改字段整体 update」改为仓储层原子自增/自减，避免并发覆盖。</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
@Service("taskInfoDomainImpl")
@RequiredArgsConstructor
public class TaskInfoDomainImpl implements TaskInfoDomain {

    private final TaskInfoRepository taskInfoRepository;

    /**
     * 同域任务类型仓储，用于回填任务类型与维护关联任务数
     */
    private final TaskConfigRepository taskConfigRepository;

    /**
     * 加载并校验任务类型配置
     *
     * @param taskId 任务配置ID
     * @return 任务类型配置
     */
    private TaskConfigRes loadConfig(Long taskId) {
        TaskConfigRes config = taskConfigRepository.getById(taskId);
        if (config == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "任务类型");
        }
        return config;
    }

    /**
     * 校验任务时间区间与广告数
     *
     * @param req      操作入参
     * @param taskType 任务类型编码
     */
    private void validateRule(TaskInfoOperateReq req, Integer taskType) {
        if (req.getStartTime() == null || req.getEndTime() == null
                || !req.getEndTime().isAfter(req.getStartTime())) {
            throw new PlatformException(BaseErrorCode.PARAM, "任务结束时间必须晚于开始时间");
        }
        if (Objects.equals(taskType, TaskTypeEnum.AD_WATCH.getCode())
                && (req.getAdCount() == null || req.getAdCount() <= 0)) {
            throw new PlatformException(BaseErrorCode.PARAM, "观看激励广告类型必须填写有效广告数");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskInfoRes create(TaskInfoOperateReq req) {
        if (req.getId() != null) {
            throw new PlatformException(BaseErrorCode.PARAM, "新增时ID必须为空");
        }
        TaskConfigRes config = loadConfig(req.getTaskId());
        validateRule(req, config.getTaskType());

        String taskNum = BusinessCodeUtil.generate(BusinessType.TASK_CODE);
        if (taskInfoRepository.existsByTaskNum(taskNum)) {
            throw new PlatformException(BaseErrorCode.EXIST_DATA, "任务编号");
        }

        TaskInfoRes res = TransferUtils.transfer(req, TaskInfoRes::new, (source, target) -> {
            target.setTaskNum(taskNum);
            target.setTaskType(config.getTaskType());
            target.setTaskTypeName(config.getTaskTypeName());
            target.setIsShow(source.getIsShow() == null ? 1 : source.getIsShow());
            target.setCompleteCount(0);
        });
        TaskInfoRes saved = taskInfoRepository.save(res);
        taskConfigRepository.incrTaskCount(config.getId(), 1);
        log.info("新增营销任务成功，ID：{}，任务编号：{}", saved.getId(), saved.getTaskNum());
        return saved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskInfoRes update(TaskInfoOperateReq req) {
        if (req.getId() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "修改时ID不能为空");
        }
        TaskConfigRes config = loadConfig(req.getTaskId());
        validateRule(req, config.getTaskType());
        if (!taskInfoRepository.existsById(req.getId())) {
            throw new PlatformException(BaseErrorCode.NODATA, "任务");
        }

        TaskInfoRes updated = taskInfoRepository.updateById(
                TransferUtils.transfer(req, TaskInfoRes::new, (source, target) -> {
                    target.setTaskType(config.getTaskType());
                    target.setTaskTypeName(config.getTaskTypeName());
                }));
        log.info("修改营销任务成功，ID：{}", req.getId());
        return updated;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (id == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "删除时ID不能为空");
        }
        TaskInfoRes res = taskInfoRepository.getById(id);
        if (res == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "任务");
        }
        taskInfoRepository.deleteById(id);
        if (res.getTaskId() != null) {
            taskConfigRepository.incrTaskCount(res.getTaskId(), -1);
        }
        log.info("删除营销任务成功，ID：{}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskInfoRes toggleShow(Long id, Integer isShow) {
        if (id == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "状态切换时ID不能为空");
        }
        if (isShow == null || (isShow != 0 && isShow != 1)) {
            throw new PlatformException(BaseErrorCode.PARAM, "显示状态值非法，仅支持0/1");
        }
        if (!taskInfoRepository.existsById(id)) {
            throw new PlatformException(BaseErrorCode.NODATA, "任务");
        }
        return taskInfoRepository.updateShowStatus(id, isShow);
    }

    @Override
    public TaskInfoRes getById(Long id) {
        TaskInfoRes res = taskInfoRepository.getById(id);
        if (res == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "任务");
        }
        return res;
    }

    @Override
    public Page<TaskInfoRes> pageQuery(TaskInfoQuery query) {
        return taskInfoRepository.pageQuery(query);
    }

    @Override
    public TaskInfoRes getByTaskNum(String taskNum) {
        return taskInfoRepository.getByTaskNum(taskNum);
    }
}
