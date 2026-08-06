package com.newzkl.platform.base.biz.finance.model.earnings.res;

import com.newzkl.platform.base.biz.finance.model.earnings.vo.AwardInfoVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.GoodsInfoVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.PickPackInfoVO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 分润信息
 * @date 2023/12/22 16:55
 */
@Data
public class AppEarningRecordRes extends BaseRes {

    /**
     * 消费类型
     */
    private EarningsEnum.ConsumeType consumeType;

    /**
     * 分润金额
     */
    private Money amount;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 贡献人
     */
    private Long contributeAccountId;

    /**
     * 贡献人名称
     */
    private String contributeAccountName;

    /**
     * 角色
     */
    private RoleEnum.CompanyRole role;
    /**
     * 结算状态
     */
    private EarningsEnum.State state;

    /**
     * 关联订单号
     */
    private Long joinOrderNo;

    /**
     * 关联交易单号（语义: 关联的交易流水号，与 joinOrderNo 含义不同）
     */
    private Long joinTradeNo;

    /**
     * 礼包信息
     */
    private PickPackInfoVO packInfoVO;

    /**
     * 商品信息
     */
    private GoodsInfoVO goodsInfo;

    /**
     * 分红信息
     */
    private AwardInfoVO awardInfoVO;

    /**
     * 分润时间
     */
    private LocalDateTime earningTime;

    /**
     * 角色名称
     */
    public String getRoleName() {
        return role == null ? "" : role.getValue();
    }

}
