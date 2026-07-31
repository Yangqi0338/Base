package com.newzkl.platform.base.biz.account.model.vo;


import lombok.Data;

@Data
public class OperatorInviteCountVO {

    /** 邀请ID */
    private Long inviteId;

    /** 邀请数量 */
    private Integer inviteCount;

}
