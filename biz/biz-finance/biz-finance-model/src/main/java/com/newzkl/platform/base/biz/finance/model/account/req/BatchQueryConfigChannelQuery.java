package com.newzkl.platform.base.biz.finance.model.account.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * @author niu
 * @description: 批量查询渠道配置req
 * @date 2024/3/15 17:17
 */
@Data
public class BatchQueryConfigChannelQuery {

    /**
     * id集合
     */
    @NotEmpty(message = "账号ID不能为空")
    private List<Long> accountId;
}
