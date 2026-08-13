package com.newzkl.platform.base.biz.account.model.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 渠道商服务费修改入参
 *
 * <p>原为 {@code OperatorCmd.ServiceFeeConfigEdit}(运营商命令集内部类), 因 OperatorCmd 随运营商
 * 业务删除, 本入参独立成类 —— 业务归属为渠道商服务费修改, 与运营商无关。</p>
 */
@Data
public class ServiceFeeConfigEdit {

    /**
     * 账户ID
     */
    @NotNull
    private Long accountId;
}
