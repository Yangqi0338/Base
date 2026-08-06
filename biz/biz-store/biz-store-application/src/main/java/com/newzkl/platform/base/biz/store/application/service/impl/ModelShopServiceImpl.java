package com.newzkl.platform.base.biz.store.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.biz.store.application.service.ModelShopService;
import com.newzkl.platform.base.biz.store.domain.adapt.api.DistributionApi;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreDomain;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreStyleDomain;
import com.newzkl.platform.base.biz.store.domain.template.service.ModelShopDomain;
import com.newzkl.platform.base.biz.store.domain.template.service.ModelShopUseRecordDomain;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreStyle;
import com.newzkl.platform.base.biz.store.model.template.entity.ModelShop;
import com.newzkl.platform.base.biz.store.model.template.entity.ModelShopUseRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 样板店应用服务实现
 *
 * @author niu
 */
@Service
@RequiredArgsConstructor
public class ModelShopServiceImpl implements ModelShopService {

    private final StoreDomain storeDomain;
    private final StoreStyleDomain storeStyleDomain;
    private final ModelShopDomain modelShopDomain;
    private final ModelShopUseRecordDomain modelShopUseRecordDomain;
    private final DistributionApi distributionApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void useModelShop(String styleCode) {
        // 1. 查询当前门店
        Store store = storeDomain.store(SecurityUtils.getAccountId());
        if (store == null) {
            throw new PlatformException(-1, "门店不存在");
        }

        // 2. 处理旧样板店
        disposeOldModelShop(store);

        // 3. 取消样板店
        if (styleCode == null) {
            handleCancelModelShop(store);
            return;
        }

        // 4. 应用样板店
        handleApplyModelShop(store, styleCode);
    }

    /**
     * 取消样板店
     *
     * @param store 门店
     */
    private void handleCancelModelShop(Store store) {
        storeDomain.cancelModelShop();
        store.setModelShopId(null);

        StoreStyle storeStyle = storeStyleDomain.copyStyle(null);
        store.setStyleCode(storeStyle.getStyleCode());
        storeDomain.storeUpdate(store);
    }

    /**
     * 应用样板店
     *
     * @param store           门店
     * @param targetStyleCode 目标样式编码
     */
    private void handleApplyModelShop(Store store, String targetStyleCode) {
        Set<Long> goodIdList = new HashSet<>();
        // 1. 查询目标样板店
        ModelShop targetModelShop = modelShopDomain.queryByStyleCode(targetStyleCode);
        if (targetModelShop == null) {
            throw new IllegalArgumentException("样板店不存在: " + targetStyleCode);
        }

        // 2. 更新使用门店数
        modelShopDomain.updateUseStoreNum(targetStyleCode, 1);
        modelShopDomain.updateTotalUseStoreNum(store.getId(), targetModelShop.getId());

        // 3. 设置样板店 ID
        store.setModelShopId(targetModelShop.getId());

        // 4. 应用样式
        StoreStyle oneselfStyle = storeStyleDomain.getOneselfStyle(targetStyleCode);
        if (oneselfStyle == null) {
            StoreStyle copiedStyle = storeStyleDomain.copyStyle(targetStyleCode);
            store.setStyleCode(copiedStyle.getStyleCode());
            if (copiedStyle.getGoodsIdListStr() != null) {
                goodIdList = StrUtil.split(copiedStyle.getGoodsIdListStr(), ",").stream()
                        .map(Long::valueOf)
                        .collect(Collectors.toSet());
            }
        } else {
            store.setStyleCode(oneselfStyle.getStyleCode());
            goodIdList = StrUtil.split(oneselfStyle.getGoodsIdListStr(), ",").stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toSet());
        }

        // 5. 更新门店
        storeDomain.storeUpdate(store);

        // 6. 同步商品数据
        if (CollUtil.isNotEmpty(goodIdList)) {
            this.synchronizeGoodsData(targetModelShop.getChannelId(), goodIdList);
        }

        ModelShopUseRecord modelShopUseRecord = new ModelShopUseRecord();
        modelShopUseRecord.setModelShopId(store.getModelShopId());
        modelShopUseRecord.setStoreId(store.getId());
        modelShopUseRecord.setStoreName(store.getName());
        modelShopUseRecord.setModelShopName(targetModelShop.getModelShopName());
        modelShopUseRecordDomain.create(modelShopUseRecord);
    }

    private void disposeOldModelShop(Store store) {
        if (store.getModelShopId() != null && store.getStyleCode() != null) {
            ModelShop modelShop = modelShopDomain.queryById(store.getModelShopId());
            if (modelShop != null) {
                modelShopDomain.updateUseStoreNum(modelShop.getStyleCode(), -1);
            }
            storeStyleDomain.deleteCopyStyle(store.getStyleCode());
        }
    }

    /**
     * 同步商品数据
     *
     * @param targetChannelId 目标渠道 ID
     * @param goodIdList      商品 ID 集合
     */
    private void synchronizeGoodsData(Long targetChannelId, Set<Long> goodIdList) {
        distributionApi.batchCopyDistribution(targetChannelId, SecurityUtils.getAccountId(), goodIdList);
    }
}
