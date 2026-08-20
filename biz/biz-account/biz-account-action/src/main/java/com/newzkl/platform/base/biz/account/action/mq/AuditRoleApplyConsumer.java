package com.newzkl.platform.base.biz.account.action.mq;

import com.alibaba.fastjson2.JSON;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.ddd.facade.AuditEvent;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 角色申请审批完成消费者
 *
 * <p>消费 plugin-audit 角色申请审批终态事件: 通过则据企业信息激活供应商入驻, 驳回则记失败原因</p>
 *
 * <p>迁移自 new-scm AuditRoleApplyConsumer + SupplierRolePolicy.roleApplyAuditEvent 的供应商分支,
 * 精简项(实名认证/角色计数/短信通知)因 Base 无对应基建留桩待接线(deferred)</p>
 *
 * @author KC
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.AUDIT_ROLE_APPLY_MESSAGE + "-consumer", tag = MQ.Tag.AUDIT_ROLE_APPLY_MESSAGE)
public class AuditRoleApplyConsumer extends AbstractMessageMQPushConsumer<AuditEvent> {

    @Autowired
    private SupplierClientDomain supplierClientDomain;
    @Autowired
    private AccountDomain accountDomain;

    /**
     * 处理角色申请审批完成事件
     *
     * @param message 审批事件
     * @param extMap 消息扩展参数
     */
    @Override
    public void remoteProcess(AuditEvent message, Map<String, Object> extMap) {
        Long accountId = message.getAccountId();
        log.info("角色申请审批完成消费, 账号ID: {}, 状态: {}", accountId, message.getState());

        if (AuditEnum.State.SUCCESS.getCode().equals(message.getState())) {
            String companyInfo = JSON.parseObject(message.getData()).getString("companyInfo");
            AccountVO accountVO = accountDomain.account(AccountEnum.Client.ADMIN, accountId);
            supplierClientDomain.auditPass(accountVO, companyInfo);
            // deferred: 实名认证 nameAuthSuccess / 角色计数 addCount / 成功短信 —— Base 无对应基建, 待接线
            log.warn("角色申请审核通过: 实名认证/角色计数/成功短信 待接线(deferred), 账号ID: {}", accountId);
        } else if (AuditEnum.State.FAIL.getCode().equals(message.getState())) {
            supplierClientDomain.auditFail(accountId, message.getLastRefuseReason());
            // deferred: 失败短信 —— 待接线
            log.warn("角色申请审核驳回: 失败短信 待接线(deferred), 账号ID: {}", accountId);
        } else {
            log.warn("角色申请审批事件状态未处理: state={}, 账号ID: {}", message.getState(), accountId);
        }
    }
}
