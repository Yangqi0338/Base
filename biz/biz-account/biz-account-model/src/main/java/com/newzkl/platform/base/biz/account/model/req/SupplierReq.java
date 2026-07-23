package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import com.newzkl.platform.base.biz.account.model.enums.AuditEnum;
// TODO[cross-domain relation]: import ...ShipAddressVO;
import lombok.Data;

/**
 * 供应商
 *
 * @author fang
 */
@Data
public class SupplierReq extends BaseReq {
    /**
     * 上级甄选师ID
     */
    private Long inviteId;
    /**
     * 账号名称
     */
    private String username;
    /**
     * 收货地址
     */
    // TODO[cross-domain relation]: private ShipAddressVO receiveAddress;
    /**
     * 审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过",4,"终止")
     */
    private AuditEnum.State auditState;
}
