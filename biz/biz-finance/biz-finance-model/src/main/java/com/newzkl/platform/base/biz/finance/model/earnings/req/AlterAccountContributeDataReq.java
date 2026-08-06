package com.newzkl.platform.base.biz.finance.model.earnings.req;


import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author niu
 * @description: 更新客户贡献数据请求对象
 * @date 2024/1/25 11:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlterAccountContributeDataReq implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 贡献对象id
     */
    private Long contributeId;

    /**
     * 更新值
     */
    private Money alterValue;

    /**
     * 1：个人消费  2：分润  3：服务费
     */
    private EarningsEnum.ContributeType alterType;

    /*
     * 构建消费请求
     * */
    public static AlterAccountContributeDataReq buildAmount(Long accountId, PurseEnum.FinanceUser accountType, Money alterValue) {
        return new AlterAccountContributeDataReq(accountId, accountType, null, accountId,
                alterValue, EarningsEnum.ContributeType.AMOUNT);
    }

    /*
     * 构建分润请求
     * */
    public static AlterAccountContributeDataReq buildEarning(Long accountId, Long parentId, PurseEnum.FinanceUser accountType, Long contributeId, Money alterValue) {
        return new AlterAccountContributeDataReq(accountId, accountType, parentId, contributeId,
                alterValue, EarningsEnum.ContributeType.EARNING);
    }

    /*
     * 构建服务费请求
     * */
    public static AlterAccountContributeDataReq buildPlatformServiceAmount(Long accountId, PurseEnum.FinanceUser accountType, Money alterValue) {
        return new AlterAccountContributeDataReq(null, accountType, null, accountId,
                alterValue, EarningsEnum.ContributeType.SERVICE_AMOUNT);
    }
}
