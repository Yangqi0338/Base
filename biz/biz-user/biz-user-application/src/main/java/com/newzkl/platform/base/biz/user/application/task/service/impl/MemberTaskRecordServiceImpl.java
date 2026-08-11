package com.newzkl.platform.base.biz.user.application.task.service.impl;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.user.application.task.service.MemberTaskRecordService;
import com.newzkl.platform.base.biz.user.domain.service.MemberTaskRecordDomain;
import com.newzkl.platform.base.common.ddd.model.enums.user.TaskStatusEnum;
import com.newzkl.platform.base.common.ddd.model.enums.user.TaskTypeEnum;
import com.newzkl.platform.base.biz.user.model.task.record.query.MemberTaskRecordQuery;
import com.newzkl.platform.base.biz.user.model.task.record.req.MemberTaskRecordAddReq;
import com.newzkl.platform.base.biz.user.model.task.record.res.MemberTaskRecordExportRes;
import com.newzkl.platform.base.biz.user.model.task.record.res.MemberTaskRecordRes;
import com.newzkl.platform.base.common.core.mq.domain.LocalMessageDomain;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.core.mq.model.dto.LocalMessageDTO;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 会员任务进度应用服务实现
 *
 * <p>迁移自旧 {@code MemberTaskRecordAppServiceImpl}。</p>
 *
 * <p>迁移说明：</p>
 * <ul>
 *   <li>枚举描述旧用 try-catch 兜「未知」，中台枚举 {@code getByCode} 返回 null，改为判空兜「未知」。</li>
 *   <li>金额换算旧由实体充血方法 {@code getRedPacketRewardYuan} 完成，中台移入
 *       {@code MemberTaskRecordRes} 的 setter。</li>
 *   <li>事件投递旧走 Dubbo {@code ILocalMessageFacade.sendMessage}，中台无 Dubbo，
 *       改为直接落本地消息表 {@link LocalMessageDomain#create}，由 core-mq 的
 *       {@code LocalMessageJob} 扫描 WAIT 状态发送（可靠消息语义与旧一致）。</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberTaskRecordServiceImpl implements MemberTaskRecordService {

    /**
     * 枚举查不到时的兜底描述
     */
    private static final String UNKNOWN_DESC = "未知";

    private final MemberTaskRecordDomain memberTaskRecordDomain;

    private final LocalMessageDomain localMessageDomain;

    /**
     * 取任务类型描述
     *
     * @param taskType 任务类型编码
     * @return 描述，查不到返回「未知」
     */
    private String taskTypeDesc(Integer taskType) {
        TaskTypeEnum item = TaskTypeEnum.getByCode(taskType);
        return item == null ? UNKNOWN_DESC : item.getDesc();
    }

    /**
     * 取任务状态描述
     *
     * @param taskStatus 任务状态编码
     * @return 描述，查不到返回「未知」
     */
    private String taskStatusDesc(Integer taskStatus) {
        TaskStatusEnum item = TaskStatusEnum.getByCode(taskStatus);
        return item == null ? UNKNOWN_DESC : item.getDesc();
    }

    @Override
    public List<MemberTaskRecordRes> pageQuery(MemberTaskRecordQuery query) {
        List<MemberTaskRecordRes> list = memberTaskRecordDomain.pageQuery(query);
        list.forEach(res -> {
            res.setTaskTypeDesc(taskTypeDesc(res.getTaskType()));
            res.setTaskStatusDesc(taskStatusDesc(res.getTaskStatus()));
        });
        return list;
    }

    @Override
    public List<MemberTaskRecordExportRes> exportAll(MemberTaskRecordQuery query) {
        query.setNonPaged(true);
        List<MemberTaskRecordRes> list = memberTaskRecordDomain.listAll(query);
        if (list.isEmpty()) {
            log.warn("导出会员任务进度：无符合条件的数据");
            return List.of();
        }
        List<MemberTaskRecordExportRes> exportList = TransferUtils.transfers(list,
                MemberTaskRecordExportRes::new, (source, target) -> {
                    target.setTaskTypeDesc(taskTypeDesc(source.getTaskType()));
                    target.setTaskStatusDesc(taskStatusDesc(source.getTaskStatus()));
                    target.setCreateTime(source.getCreateTime());
                });
        log.info("会员任务进度导出数据转换完成，共{}条", exportList.size());
        return exportList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMessage(MemberTaskRecordAddReq req) {
        LocalMessageDTO message = new LocalMessageDTO();
        message.setTag(MQ.Tag.TASK_MEMBER_PROGRESS_EVENT);
        message.setMessageContent(JSONUtil.toJsonStr(req));
        message.setMessageClass(MemberTaskRecordAddReq.class.getCanonicalName());
        message.setSendState(MQEnum.SendState.WAIT);
        Long messageId = localMessageDomain.create(message);
        log.info("会员任务进度事件已落本地消息表，messageId：{}，会员ID：{}，任务编号：{}",
                messageId, req.getMemberId(), req.getTaskNum());
    }
}
