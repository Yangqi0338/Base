package com.newzkl.platform.base.biz.finance.model.pay.req;

import com.newzkl.platform.base.biz.finance.model.pay.vo.PayeeInfoVO;
import lombok.Data;

import java.util.List;

/**
 * 更新现金流收款方配置请求。
 *
 * <p>迁移自 new-scm {@code domain.pay.model.req.UpdatePayeeInfoReq}。</p>
 *
 * @author KC
 */
@Data
public class UpdatePayeeInfoReq {

    /**
     * 消费类型。
     *
     * <p>1: 甄选师礼包进账; 2: 采购金充值进账; 3: 甄选师转出出账; 4: 运营商转出出账;
     * 5: 供应商转出出账; 6: 交易师转出出账; 7: 分红奖转出出账。</p>
     */
    private Integer consumeType;

    /**
     * 收款方信息列表。
     */
    private List<PayeeInfoVO> payeeInfoList;
}
