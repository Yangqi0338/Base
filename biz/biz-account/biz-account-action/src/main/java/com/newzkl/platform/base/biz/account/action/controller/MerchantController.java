package com.newzkl.platform.base.biz.account.action.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.MerchantService;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreOrderPayRes;
import com.newzkl.platform.base.biz.account.domain.service.MerchantDomain;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantCmd;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantQuery;
import com.newzkl.platform.base.biz.account.model.merchant.res.MerchantRes;
import com.newzkl.platform.base.biz.account.model.merchant.vo.WxMpConfigVO;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户-商户
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.MerchantController}。
 * 类级路径与方法级路径逐字沿用旧契约; 旧控制器 9 个端点中 8 个已在 2026-07-27 交叉比对中
 * 确认前端零引用, 仅 {@code orderPay} 在用, 但为契约完整性全部迁入并保留 {@code @Deprecated}。</p>
 *
 * <p>迁移差异: 旧 {@code merchant} / {@code merchantPage} 出参为
 * {@code MerchantVO} / {@code PageInfo<MerchantVO>}, 中台统一为出参对象
 * {@code MerchantRes} 与 MyBatis-Plus {@code Page}。</p>
 *
 * <p>infra-gap 清单:</p>
 * <ul>
 *   <li>{@code POST /merchant/orderPay}: 下单支付依赖 biz-finance 出站端口, 详见
 *       {@code StoreOrderPayApiDefaultImpl}</li>
 * </ul>
 *
 * @author KC
 */
@RestController
@RequestMapping("/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantDomain merchantDomain;
    private final MerchantService merchantService;

    /**
     * 商户修改
     *
     * @param edit 商户修改入参
     * @return 空结果
     * @deprecated 前端零引用, 已确认死端点 (2026-07-27 交叉比对); 仅为契约完整性迁入
     */
    @Deprecated
    @PostMapping("merchantEdit")
    public PlatformResult<Void> merchantEdit(@Validated @RequestBody MerchantCmd.Edit edit) {
        merchantDomain.edit(edit.getId(), edit.getMerchantCommand());
        return PlatformResult.success();
    }

    /**
     * 商户删除
     *
     * @param merchantIdList 商户账号ID列表
     * @return 空结果
     * @deprecated 前端零引用, 已确认死端点 (2026-07-27 交叉比对); 仅为契约完整性迁入
     */
    @Deprecated
    @PostMapping("merchantDelete")
    public PlatformResult<Void> merchantDelete(@Validated @RequestBody MerchantCmd.IDList merchantIdList) {
        merchantDomain.delete(merchantIdList.getMerchantIdList());
        return PlatformResult.success();
    }

    /**
     * 当前登录商户详情
     *
     * @return 商户详情
     * @deprecated 前端零引用, 已确认死端点 (2026-07-27 交叉比对); 仅为契约完整性迁入
     */
    @Deprecated
    @PostMapping("merchant")
    public PlatformResult<MerchantRes> merchant() {
        return PlatformResult.success(merchantDomain.currentMerchant());
    }

    /**
     * 商户分页
     *
     * @param merchantQuery 商户查询
     * @return 商户分页
     * @deprecated 前端零引用, 已确认死端点 (2026-07-27 交叉比对); 仅为契约完整性迁入
     */
    @Deprecated
    @PostMapping("merchantPage")
    public PlatformResult<Page<MerchantRes>> merchantPage(@RequestBody MerchantQuery merchantQuery) {
        return PlatformResult.success(merchantDomain.pageList(merchantQuery));
    }

    /**
     * 数智门店订单支付
     *
     * <p>迁移差异: 旧出参为 {@code PayBaseResult} 接口 (仅暴露 tradeNo / thirdTradeNo),
     * 中台化后为避免账户域反依赖资金域模型, 改为端口侧等价出参 {@code StoreOrderPayRes}。</p>
     *
     * @param orderPay 订单支付入参
     * @return 支付结果
     */
    @PostMapping("orderPay")
    public PlatformResult<StoreOrderPayRes> orderPay(@RequestBody @Valid MerchantCmd.OrderPay orderPay) {
        return PlatformResult.success(merchantService.orderPay(orderPay, SecurityUtils.getAccountId()));
    }

    /**
     * 获取微信公众号 appId
     *
     * <p>迁移差异: 旧入参为 {@code IdObj}, 中台等价入参为 {@code IdListCommand}, 取首个 ID。</p>
     *
     * @param idListCommand 渠道商ID入参
     * @return 公众号 appId
     * @deprecated 前端零引用, 已确认死端点 (2026-07-27 交叉比对); 仅为契约完整性迁入
     */
    @Deprecated
    @PostMapping("appId")
    public PlatformResult<String> appId(@RequestBody IdListCommand idListCommand) {
        WxMpConfigVO wxMpConfig = merchantDomain.wxMpConfig(CollUtil.getFirst(idListCommand.getIdList()));
        return PlatformResult.success(wxMpConfig == null ? null : wxMpConfig.getAppId());
    }

    /**
     * 获取微信公众号参数
     *
     * @param idListCommand 渠道商ID入参
     * @return 微信公众号配置
     * @deprecated 前端零引用, 已确认死端点 (2026-07-27 交叉比对); 仅为契约完整性迁入
     */
    @Deprecated
    @PostMapping("getWxMpConfigVO")
    public PlatformResult<WxMpConfigVO> getWxMpConfigVO(@RequestBody IdListCommand idListCommand) {
        return PlatformResult.success(merchantDomain.wxMpConfig(CollUtil.getFirst(idListCommand.getIdList())));
    }

    /**
     * 配置微信公众号参数
     *
     * <p>保留旧语义: 作用于当前登录商户。</p>
     *
     * @param editWxMpConfigVO 微信公众号配置入参
     * @return 空结果
     * @deprecated 前端零引用, 已确认死端点 (2026-07-27 交叉比对); 仅为契约完整性迁入
     */
    @Deprecated
    @PostMapping("setWxMpConfigVO")
    public PlatformResult<Void> setWxMpConfigVO(@RequestBody MerchantCmd.EditWxMpConfigVO editWxMpConfigVO) {
        merchantDomain.wxMpConfigSet(editWxMpConfigVO.getWxMpConfigVO());
        return PlatformResult.success();
    }
}
