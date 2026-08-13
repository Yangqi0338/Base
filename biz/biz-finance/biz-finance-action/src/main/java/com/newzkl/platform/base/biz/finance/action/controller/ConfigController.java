package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.model.account.req.BatchQueryConfigChannelQuery;
import com.newzkl.platform.base.biz.finance.model.account.res.BatchQueryConfigChannelRes;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 配置类控制器
 *
 * <p>迁移自 new-scm {@code interfaces.config.ConfigController}, 类级路径 {@code /config} 逐字保留。</p>
 *
 * <p>未迁端点:</p>
 * <ul>
 *   <li>{@code /getWxPayConfig} — 微信支付通道已整体删除, 该端点随通道去掉</li>
 * </ul>
 *
 * @author KC
 */
@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {

    private final AccountPurseConfigDomain accountPurseConfigDomain;

    /**
     * 批量查询渠道商服务费配置
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
     * 查询供应商配置
     *
     * @return 供应商配置
     */
    @PostMapping("/querySupplierConfig")
    public PlatformResult<ConfigSupplierVO> querySupplierConfig() {
        return PlatformResult.success(accountPurseConfigDomain.querySupplierConfig());
    }

    /**
     * 保存供应商配置
     *
     * @param configSupplierVO 供应商配置
     * @return 成功结果
     */
    @PostMapping("/saveSupplierConfig")
    public PlatformResult<Boolean> saveSupplierConfig(@RequestBody ConfigSupplierVO configSupplierVO) {
        accountPurseConfigDomain.saveSupplierConfig(configSupplierVO);
        return PlatformResult.success();
    }
}
