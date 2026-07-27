package com.newzkl.platform.base.biz.store.action.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.action.cmd.StoreCmd;
import com.newzkl.platform.base.biz.store.application.service.StoreService;
import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelContactReq;
import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelStoreVO;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreDomain;
import com.newzkl.platform.base.biz.store.model.store.req.StoreQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreSearchRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreRes;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门店控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreDomain storeDomain;
    private final StoreService storeService;

    /**
     * 门店编辑, 含渠道联系人同步。
     *
     * @param edit 编辑命令
     * @return 成功结果
     */
    @PostMapping("storeEdit")
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
     * 门店详情, 含渠道联系人回填。
     *
     * @param storeId 门店 ID 命令
     * @return 门店 VO
     */
    @PostMapping("store")
    public PlatformResult<StoreRes> store(@Validated @RequestBody StoreCmd.ID storeId) {
        if (storeId.getStoreId() == null) {
            storeId.setStoreId(SecurityUtils.getAccountId());
        }
        StoreRes storeVO = storeService.store(storeId.getStoreId());
        if (storeVO != null) {
            ChannelStoreVO channelStoreVO = storeService.channelVO(storeVO.getChannelId());
            storeVO.setContactName(channelStoreVO.getContactsName());
            storeVO.setContactPhone(channelStoreVO.getContactsPhone());
        }
        return PlatformResult.success(storeVO);
    }

    /**
     * 门店分页。
     *
     * @param storeQueryReq 查询请求
     * @return 门店分页
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @PostMapping("storePage")
    public PlatformResult<Page<StoreRes>> storePage(@RequestBody StoreQuery storeQueryReq) {
        storeQueryReq.setChannelId(SecurityUtils.getAccountId());
        return PlatformResult.success(storeDomain.storePage(storeQueryReq));
    }

    /**
     * 获取样板店样式。
     *
     * @return 门店样式 VO
     */
    @GetMapping("/getModelShopStyle")
    public PlatformResult<StoreStyleRes> getModelShopStyle() {
        return PlatformResult.success(storeService.getModelShopStyle());
    }

    /**
     * 门店搜索分页。
     *
     * @param storeQueryReq 查询请求
     * @return 搜索分页
     */
    @PostMapping("storeSearchPage")
    public PlatformResult<Page<StoreSearchRes>> storeSearchPage(@RequestBody StoreQuery storeQueryReq) {
        return PlatformResult.success(storeDomain.storeSearchPage(storeQueryReq));
    }
}
