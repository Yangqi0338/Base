package com.newzkl.platform.base.biz.account.action.task;

import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.common.core.redis.aspect.DistributedLock;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 已注销用户回收任务
 *
 * <p>按小时扫描已注销(DESTROY)且超过24h宽限期的用户账号, 物理删除以释放 username 唯一索引, 使其可重新注册。
 * 逻辑删除保留数据会占用唯一索引, 故此处走物理删除; 至多1小时回收偏差可接受。多实例部署以分布式锁保证单实例执行</p>
 *
 * @author KC
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CanceledMemberRecycleTask {

    private final UserClientDomain userClientDomain;

    @XxlJob("canceledMemberRecycle")
    @DistributedLock(key = "'canceledMemberRecycle'")
    public void recycle() {
        log.info("已注销用户回收任务开始执行");
        int deleted = userClientDomain.recycleCanceledMember();
        log.info("已注销用户回收任务执行完成, 删除行数: {}", deleted);
    }
}
