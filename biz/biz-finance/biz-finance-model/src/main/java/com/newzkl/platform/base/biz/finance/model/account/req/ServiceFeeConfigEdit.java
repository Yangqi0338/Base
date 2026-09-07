package com.newzkl.platform.base.biz.finance.model.account.req;

import com.newzkl.platform.base.biz.finance.model.account.vo.ServiceFeeConfigVO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 渠道商服务费修改入参
 *
 * <p>原为 {@code OperatorCmd.ServiceFeeConfigEdit}(运营商命令集内部类), 随运营商业务删除独立成类。
 * 由 account 域迁入: 服务费配置属资金域数据, 入参随端点一并归位 finance。</p>
 *
 * @author KC
 */
@Data
public class ServiceFeeConfigEdit {

    /**
     * 渠道商账号ID
     */
    @NotNull
    private Long accountId;

    /**
     * 服务费配置
     */
    @NotNull
    private ServiceFeeConfigVO serviceFeeConfigVO;
}
