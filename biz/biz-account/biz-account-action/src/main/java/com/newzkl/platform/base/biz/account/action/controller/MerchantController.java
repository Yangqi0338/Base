package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.action.cmd.MerchantCmd;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.MerchantDomain;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantQuery;
import com.newzkl.platform.base.biz.account.model.merchant.res.MerchantRes;
import com.newzkl.platform.base.biz.account.model.merchant.vo.WxMpConfigVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户-商户控制器。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.MerchantController}, 路径与 HTTP 方法保持不变。
 * 旧 {@code IRoleService.useStoreCdk} 只是对商户领域同名方法的薄转发, 中台化后控制器直调领域层;
 * 旧 {@code orderPay} 端点依赖未迁的 finance 支付与账号身份判定能力, 本切片未落地 (见迁移报告)。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantDomain merchantDomain;
    private final UserQueryService userQueryService;

    /**
     * 商户修改。
     *
     * @param edit 商户修改入参 (以其 id 为更新目标)
     * @return 成功结果
     */
    @PostMapping("merchantEdit")
    public PlatformResult<Void> merchantEdit(@Validated @RequestBody MerchantCmd.Edit edit) {
        merchantDomain.edit(edit.getId(), edit.getMerchantCommand());
        return PlatformResult.success();
    }

    /**
     * 商户删除。
     *
     * @param merchantIdList 商户 ID 列表
     * @return 成功结果
     */
    @PostMapping("merchantDelete")
    public PlatformResult<Void> merchantDelete(@Validated @RequestBody MerchantCmd.IDList merchantIdList) {
        merchantDomain.delete(merchantIdList.getMerchantIdList());
        return PlatformResult.success();
    }

    /**
     * 当前登录商户详情。
     *
     * @return 商户详情, 无则 null
     */
    @PostMapping("merchant")
    public PlatformResult<MerchantRes> merchant() {
        return PlatformResult.success(userQueryService.merchantVO(SecurityUtils.getAccountId()));
    }

    /**
     * 商户分页。
     *
     * @param merchantQuery 商户查询
     * @return 商户分页
     */
    @PostMapping("merchantPage")
    public PlatformResult<Page<MerchantRes>> merchantPage(@RequestBody MerchantQuery merchantQuery) {
        return PlatformResult.success(userQueryService.merchantPage(merchantQuery));
    }

    /**
     * 使用数字门店开通码。
     *
     * <p>使用者取当前登录态账号, 由领域层回填。</p>
     *
     * @param cdk 开通码入参
     * @return 成功结果
     */
    @PostMapping("useStoreCdk")
    public PlatformResult<Void> useStoreCdk(@Validated @RequestBody MerchantCmd.Cdk cdk) {
        merchantDomain.useStoreCdk(cdk.getCdk());
        return PlatformResult.success();
    }

    /**
     * 获取微信公众号 appId。
     *
     * @param idObj 商户 (渠道商) ID
     * @return appId, 未配置时为 null
     */
    @PostMapping("appId")
    public PlatformResult<String> appId(@RequestBody MerchantCmd.ID idObj) {
        WxMpConfigVO wxMpConfig = userQueryService.wxMpConfig(idObj.getId());
        return PlatformResult.success(wxMpConfig == null ? null : wxMpConfig.getAppId());
    }

    /**
     * 获取微信公众号参数。
     *
     * @param idObj 商户 (渠道商) ID
     * @return 微信公众号配置, 未配置时为 null
     */
    @PostMapping("getWxMpConfigVO")
    public PlatformResult<WxMpConfigVO> getWxMpConfigVO(@RequestBody MerchantCmd.ID idObj) {
        return PlatformResult.success(userQueryService.wxMpConfig(idObj.getId()));
    }

    /**
     * 配置当前登录商户的微信公众号参数。
     *
     * @param editWxMpConfigVO 微信公众号配置入参
     * @return 成功结果
     */
    @PostMapping("setWxMpConfigVO")
    public PlatformResult<Void> setWxMpConfigVO(@RequestBody MerchantCmd.EditWxMpConfigVO editWxMpConfigVO) {
        merchantDomain.wxMpConfigSet(editWxMpConfigVO.getWxMpConfigVO());
        return PlatformResult.success();
    }
}
