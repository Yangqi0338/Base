package com.newzkl.platform.base.biz.finance.model.purse.req;


import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.check.CheckCommand;
import com.newzkl.platform.base.biz.finance.model.enums.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author niu
 * @description: 查询转出申请req
 * @date 2023/12/23 14:21
 */
@Data
public class RollOutApplyQuery extends BizPageQuery {

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 账户类型
     */
    @NotNull(message = "账户类不能为空", groups = {CheckCommand.class})
    private PurseEnum.PurseType purseType;

    /**
     * 申请时间左
     */
    private LocalDateTime applyTimeL;

    /**
     * 申请时间右
     */
    private LocalDateTime applyTimeR;

    /**
     * 审核状态
     */
    private Integer auditState;

    /**
     * 审核状态集合
     */
    private List<AuditEnum.WithdrawSate> auditStateList;

    public void setAuditState(AuditEnum.WithdrawSate auditState) {
        this.auditStateList = BizPageQuery.doWrapperList(auditStateList, auditState);
    }
}
