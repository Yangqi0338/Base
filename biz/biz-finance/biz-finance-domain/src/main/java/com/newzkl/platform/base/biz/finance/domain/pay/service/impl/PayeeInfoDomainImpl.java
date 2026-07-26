package com.newzkl.platform.base.biz.finance.domain.pay.service.impl;

import com.newzkl.platform.base.biz.finance.domain.adapt.repository.PayeeInfoRepository;
import com.newzkl.platform.base.biz.finance.domain.pay.service.PayeeInfoDomain;
import com.newzkl.platform.base.biz.finance.model.pay.req.UpdatePayeeInfoReq;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PayeeInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 现金流收款方配置领域服务实现。
 *
 * <p>迁移自 new-scm {@code PayeeInfoServiceImpl}。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class PayeeInfoDomainImpl implements PayeeInfoDomain {

    /**
     * 收款方配置仓储。
     */
    private final PayeeInfoRepository payeeInfoRepository;

    /**
     * 更新收款方配置。
     *
     * @param req 更新请求
     */
    @Override
    public void updatePayeeInfo(UpdatePayeeInfoReq req) {
        payeeInfoRepository.alterPayeeInfo(req);
    }

    /**
     * 按消费类型查询收款方配置。
     *
     * @param consumeType 消费类型
     * @return 收款方配置列表
     */
    @Override
    public List<PayeeInfoVO> queryPayeeInfo(Integer consumeType) {
        return payeeInfoRepository.queryPayeeInfo(consumeType);
    }
}
