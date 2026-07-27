package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.model.account.req.BatchQueryConfigChannelQuery;
import com.newzkl.platform.base.biz.finance.model.account.res.BatchQueryConfigChannelRes;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 配置类控制器。
 *
 * @author niu
 */
@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {

    private final AccountPurseConfigDomain accountPurseConfigDomain;

    /**
     * 批量查询渠道商服务费配置。
     *
     * @param req 批量查询请求
     * @return 渠道商配置列表
     */
    @PostMapping("/batchQueryChannelConfig")
    public PlatformResult<List<BatchQueryConfigChannelRes>> batchQueryChannelConfig(@RequestBody BatchQueryConfigChannelQuery req) {
        if (req.getAccountId() != null && req.getAccountId().size() > 0) {
            return PlatformResult.success(accountPurseConfigDomain.batchQueryChannelConfig(req));
        }
        return PlatformResult.success();
    }

    /**
     * 查询供应商配置。
     *
     * @return 供应商配置
     */
    @PostMapping("/querySupplierConfig")
    public PlatformResult<ConfigSupplierVO> querySupplierConfig() {
        return PlatformResult.success(accountPurseConfigDomain.querySupplierConfig());
    }
}
