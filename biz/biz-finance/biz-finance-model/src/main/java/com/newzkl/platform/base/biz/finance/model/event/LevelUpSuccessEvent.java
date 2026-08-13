package com.newzkl.platform.base.biz.finance.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 账户升级成功事件
 *
 * <p>迁移自旧 {@code com.zkl.scm.message.rpc.model.user.LevelUpSuccessEvent},
 * 作 {@code LevelUpSuccessConsumer} 消息载体</p>
 *
 * @author KC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelUpSuccessEvent implements Serializable {

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 升级后等级值
     */
    private Integer level;

    /**
     * 升级前角色ID
     */
    private Long oldRoleId;

    /**
     * 升级后角色ID
     */
    private Long newRoleId;
}
