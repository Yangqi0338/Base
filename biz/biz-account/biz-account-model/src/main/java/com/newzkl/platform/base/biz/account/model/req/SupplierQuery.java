package com.newzkl.platform.base.biz.account.model.req;


import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.biz.account.model.enums.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.SupplierEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 供应商
 *
 * @author fang
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class SupplierQuery extends BizPageQuery {

    /**
     * 状态
     */
    private List<SupplierEnum.State> stateList;
    /**
     * 是否缴纳保证金
     */
    private CommonEnum.YesOrNo promisePayState;
    /**
     * 是否设置账期
     */
    private CommonEnum.YesOrNo periodSetState;
    /**
     * 账号名称
     */
    private String username;
    /**
     * 审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过",4,"终止")
     */
    private AuditEnum.State auditState;
    /**
     * 企业名称
     */
    private String companyName;

    public void setState(SupplierEnum.State state) {
        this.stateList = doWrapperList(this.stateList, state);
    }
    /**
     * 邀请人ID
     */
    private Long inviteId;
    /**
     * 行业ID集合
     */
    private Long industryId;
    /**
     * 企业区域
     */
    private Long companyAreaCode;
}
