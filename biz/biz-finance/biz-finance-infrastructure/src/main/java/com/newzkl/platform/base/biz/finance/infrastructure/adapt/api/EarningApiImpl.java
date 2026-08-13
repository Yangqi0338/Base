package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.EarningApi;
import com.newzkl.platform.base.biz.finance.model.event.SkuOrderWaitEarningVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 分润域出站端口实现 (留桩)
 *
 * <p>分润域(scm-finance earning)尚未迁入 Base, 本实现暂留桩仅记日志
 * (deferred, 见 docs/planning/deferred-issues.md), 能力齐后补全实际调用</p>
 *
 * @author KC
 */
@Slf4j
@Service
public class EarningApiImpl implements EarningApi {

    @Override
    public void settleEarning(SkuOrderWaitEarningVO waitEarning) {
        log.warn("settleEarning 待接线(分润域未迁, 能力缺), skuOrderId: {}",
                waitEarning == null ? null : waitEarning.getSkuOrderId());
    }

    @Override
    public void updateRecordRole(Long accountId, Long newRoleId) {
        log.warn("updateRecordRole 待接线(分润域未迁, 能力缺), accountId: {}, newRoleId: {}", accountId, newRoleId);
    }
}
