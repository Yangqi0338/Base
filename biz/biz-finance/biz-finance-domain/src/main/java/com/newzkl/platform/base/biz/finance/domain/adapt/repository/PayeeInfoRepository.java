package com.newzkl.platform.base.biz.finance.domain.adapt.repository;

import com.newzkl.platform.base.biz.finance.model.pay.req.UpdatePayeeInfoReq;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PayeeInfoVO;

import java.util.List;

/**
 * 现金流收款方配置仓储。
 *
 * <p>迁移自 new-scm {@code IPayeeInfoRepository}。</p>
 *
 * @author KC
 */
public interface PayeeInfoRepository {

    /**
     * 更新收款方配置 (先删旧配置再整体重写)。
     *
     * @param req 更新请求
     */
    void alterPayeeInfo(UpdatePayeeInfoReq req);

    /**
     * 按消费类型查询收款方配置。
     *
     * @param consumeType 消费类型
     * @return 收款方配置列表
     */
    List<PayeeInfoVO> queryPayeeInfo(Integer consumeType);
}
