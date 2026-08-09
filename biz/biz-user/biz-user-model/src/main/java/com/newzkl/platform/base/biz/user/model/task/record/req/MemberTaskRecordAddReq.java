package com.newzkl.platform.base.biz.user.model.task.record.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 会员任务进度上报请求
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.rpc.model.req.MemberTaskRecordAddReq}（旧在 rpc 模块，
 * 中台无 Dubbo，落 model 模块）。</p>
 *
 * <p>迁移说明：作为 MQ 消息体需序列化，保留 {@code Serializable}。</p>
 *
 * @author KC
 */
@Data
public class MemberTaskRecordAddReq implements Serializable {

    /**
     * 会员ID
     */
    private String memberId;

    /**
     * 任务编号
     */
    private String taskNum;

    /**
     * 会员昵称
     */
    private String memberNickname;

    /**
     * 新增数值
     * @ext 广告数量/订单金额
     */
    private Long num;

    /**
     * 商品ID
     */
    private Long goodsId;
}
