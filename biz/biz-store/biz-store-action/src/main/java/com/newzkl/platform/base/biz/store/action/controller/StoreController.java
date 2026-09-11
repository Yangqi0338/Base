package com.newzkl.platform.base.biz.store.action.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.action.cmd.StoreCmd;
import com.newzkl.platform.base.biz.store.application.service.StoreService;
import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelContactReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreOrderPayReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreOrderPayRes;
import com.newzkl.platform.base.common.ddd.facade.ChannelStoreVO;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreDomain;
import com.newzkl.platform.base.biz.store.model.store.query.StoreQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreSearchRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleRes;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 终端-门店控制器
 *
 * <p>迁移自旧 {@code com.zkl.scm.terminal.interfaces.controller.StoreController},
 * 端点路径与 HTTP 方法逐字保留 (含无前导斜杠的 {@code storeEdit}/{@code store}/{@code storeSearchPage})。
 * 旧 {@code storePage} 为 {@code @Deprecated} 死端点, 未迁入。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
@Slf4j
@FuncPermission("门店")
public class StoreController {

    private final StoreDomain storeDomain;

    private final StoreService storeService;

    /**
     * 门店修改
     *
     * @param edit 门店编辑命令
     * @return 成功结果
     */
    @PostMapping("storeEdit")
    @FuncPermission("门店修改")
    public PlatformResult<Void> storeEdit(@Validated @RequestBody StoreCmd.Edit edit) {
        if (edit.getId() == null) {
            edit.setId(SecurityUtils.getAccountId());
        }
        storeDomain.storeEdit(edit.getId(), edit.getStoreSaveReq());
        String contactName = edit.getContactName();
        String contactPhone = edit.getContactPhone();
        if (StrUtil.isNotBlank(contactName) || StrUtil.isNotBlank(contactPhone)) {
            ChannelContactReq req = new ChannelContactReq();
            req.setAccountId(edit.getId());
            req.setName(contactName);
            req.setPhone(contactPhone);
            storeService.updateChannel(req);
        }
        return PlatformResult.success();
    }

    /**
     * 门店详情
     *
     * @param storeId 门店 ID 命令
     * @return 门店详情
     */
    @PostMapping("store")
    public PlatformResult<StoreRes> store(@Validated @RequestBody StoreCmd.ID storeId) {
        if (storeId.getStoreId() == null) {
            storeId.setStoreId(SecurityUtils.getAccountId());
        }
        StoreRes storeRes = storeService.store(storeId.getStoreId());
        if (storeRes != null) {
            ChannelStoreVO channelStoreVO = storeService.channelVO(storeRes.getChannelId());
            storeRes.setContactName(channelStoreVO.getContactsName());
            storeRes.setContactPhone(channelStoreVO.getContactsPhone());
        }
        return PlatformResult.success(storeRes);
    }

    /**
     * 脉脉通商城页面获取门店样式
     *
     * @return 门店样式
     */
    @GetMapping("/getModelShopStyle")
    public PlatformResult<StoreStyleRes> getModelShopStyle() {
        return PlatformResult.success(storeService.getModelShopStyle());
    }

    /**
     * 脉脉通门店分页搜索
     *
     * @param storeQueryReq 查询入参
     * @return 门店搜索分页
     */
    @PostMapping("storeSearchPage")
    public PlatformResult<Page<StoreSearchRes>> storeSearchPage(@RequestBody StoreQuery storeQueryReq) {
        return PlatformResult.success(storeDomain.storeSearchPage(storeQueryReq));
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
    @FuncPermission("门店订单支付")
    public PlatformResult<StoreOrderPayRes> orderPay(@RequestBody @Valid StoreOrderPayReq orderPay) {
        orderPay.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(storeService.orderPay(orderPay));
    }
}
