package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.domain.pay.service.PayeeInfoDomain;
import com.newzkl.platform.base.biz.finance.model.account.req.BatchQueryConfigChannelQuery;
import com.newzkl.platform.base.biz.finance.model.account.res.BatchQueryConfigChannelRes;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.biz.finance.model.pay.req.UpdatePayeeInfoReq;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PayeeInfoVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final PayeeInfoDomain payeeInfoDomain;

    /**
     * 更新收款方配置
     *
     * @param req 收款方更新请求
     * @return 成功结果
     */
    @PostMapping("/alterPayeeInfo")
    public PlatformResult<Boolean> alterPayeeInfo(@RequestBody UpdatePayeeInfoReq req) {
        payeeInfoDomain.updatePayeeInfo(req);
        return PlatformResult.success();
    }

    /**
     * 查询收款方信息
     *
     * @param consumeType 消费类型
     * @return 收款方信息列表
     */
    @PostMapping("/queryPayeeInfo/{consumeType}")
    public PlatformResult<List<PayeeInfoVO>> queryPayeeInfo(@PathVariable Integer consumeType) {
        return PlatformResult.success(payeeInfoDomain.queryPayeeInfo(consumeType));
    }

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
     * 查询渠道商所属运营商是否为杠杆模式
     *
     * @return 大于 0 为杠杆模式
     */
    @PostMapping("/queryChannelUpOperatorLever")
    public PlatformResult<Integer> queryChannelUpOperatorLever() {
        return PlatformResult.success(accountPurseConfigDomain.queryOperatorLever(SecurityUtils.getOperatorId()));
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
