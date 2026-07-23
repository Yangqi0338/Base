package com.newzkl.platform.base.biz.store.domain.store.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkl.scm.auth.api.SecurityUtils;
import com.zkl.scm.module.enums.BusinessType;
import com.zkl.scm.module.util.BusinessCodeUtil;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreZone;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreZoneBackgroundImage;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreZoneGoodsRelation;
import com.newzkl.platform.base.biz.store.model.store.req.StoreZoneCreateReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreZonePageReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreZoneUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreZoneResponse;
import com.newzkl.platform.base.biz.store.domain.store.repository.IStoreZoneBackgroundImageRepository;
import com.newzkl.platform.base.biz.store.domain.store.repository.IStoreZoneGoodsRelationRepository;
import com.newzkl.platform.base.biz.store.domain.store.repository.IStoreZoneRepository;
import com.newzkl.platform.base.biz.store.domain.store.service.IStoreZoneDomain;
import com.zkl.scm.web.utils.TransferUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 门店专区
 */
@Service
public class IStoreZoneDomainImpl implements IStoreZoneDomain {

    @Resource
    private IStoreZoneRepository storeZoneRepository;

    @Resource
    private IStoreZoneBackgroundImageRepository storeZoneBackgroundImageRepository;

    @Resource
    private IStoreZoneGoodsRelationRepository storeZoneGoodsRelationRepository;

    @Override
    public Page<StoreZoneResponse> storeZonePage(StoreZonePageReq req) {
        return storeZoneRepository.storeZonePage(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(StoreZoneCreateReq req) {
        StoreZone storeZone = TransferUtils.transfer(req, StoreZone::new);
        storeZone.setCreateId(SecurityUtils.getAccountId());
        storeZone.setCreateName(SecurityUtils.getNickName());
        storeZone.setZoneCode(BusinessCodeUtil.generate(BusinessType.STORE_SPECIAL_ZONE));
        storeZoneRepository.create(storeZone);

        // 背景图
        List<StoreZoneBackgroundImage> imageList = req.getBackgroundImageList().stream()
                .map(url -> {
                    StoreZoneBackgroundImage image = new StoreZoneBackgroundImage();
                    image.setZoneCode(storeZone.getZoneCode());
                    image.setBackgroundImage(url);
                    return image;
                })
                .collect(Collectors.toList());
        storeZoneBackgroundImageRepository.createBatchImage(imageList);

        // 专区商品关系
        List<StoreZoneGoodsRelation> relationList = TransferUtils.transfers(req.getStoreZoneGoodsList(), StoreZoneGoodsRelation::new, (c, v) -> {
            v.setZoneCode(storeZone.getZoneCode());
        });
        storeZoneGoodsRelationRepository.createBatchRelation(relationList);
    }

    @Override
    public void update(StoreZoneUpdateReq req) {
        storeZoneRepository.update(TransferUtils.transfer(req, StoreZone::new));
    }

    /**
     * 新增商品个数
     */
    @Override
    public void increaseGoodsNum(String storeZoneCode, Integer increaseNum) {
        storeZoneRepository.increaseGoodsNum(storeZoneCode, increaseNum);
    }

    /**
     * 新增订单个数
     */
    @Override
    public void increaseOrderNum(String storeZoneCode, Integer increaseNum) {
        storeZoneRepository.increaseOrderNum(storeZoneCode, increaseNum);
    }
}
