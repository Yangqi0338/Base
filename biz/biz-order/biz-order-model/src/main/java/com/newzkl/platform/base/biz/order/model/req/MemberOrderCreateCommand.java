package com.newzkl.platform.base.biz.order.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author sijiwang
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MemberOrderCreateCommand {
    /**
     * 门店ID
     */
    private Long storeId;

    private String storeName;

    private String storeAccount;

    /**
     * 门店头像
     */
    private String storeHead;

    /**
     * 账号id(account.id)
     */
    private Long accountId;

    private String userName;

    private String nickName;

    private String userAccount;

    /**
     * 商户ID
     */
    private Long merchantId;

    /**
     * 无参构造方法
     */
    public MemberOrderCreateCommand() {
    }

    /**
     * @param storeId 门店ID
     */
    public MemberOrderCreateCommand(Long storeId,Long accountId) {
        this.storeId = storeId;
        this.accountId = accountId;
    }
}