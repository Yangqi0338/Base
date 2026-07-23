package com.newzkl.platform.base.biz.store.domain.store.service.impl;

import cn.hutool.core.lang.Opt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreCategory;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreStyle;
import com.newzkl.platform.base.biz.store.model.store.req.StoreQueryReq;
import com.newzkl.platform.base.biz.store.model.store.req.StoreSaveReq;
import com.newzkl.platform.base.biz.store.model.store.vo.StoreSearchVO;
import com.newzkl.platform.base.biz.store.model.store.vo.StoreVO;
import com.newzkl.platform.base.biz.store.domain.store.repository.IStoreRepository;
import com.newzkl.platform.base.biz.store.domain.store.repository.IStoreStyleRepository;
import com.newzkl.platform.base.biz.store.domain.store.service.IStoreCategoryDomain;
import com.newzkl.platform.base.biz.store.domain.store.service.IStoreDomain;
import com.zkl.scm.web.id.SnowflakeIdAble;
import com.zkl.scm.web.utils.TransferUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 订单
 * @date 2024/1/3115:56
 */
@Service
public class IStoreDomainImpl implements IStoreDomain {

    @Autowired
    private IStoreRepository storeRepository;
    @Autowired
    private IStoreStyleRepository storeStyleRepository;
    @Autowired
    @Lazy
    private IStoreCategoryDomain storeCategoryDomain;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long storeSave(StoreSaveReq storeSaveReq) {
        Store item = TransferUtils.transfer(storeSaveReq, Store::new, (c, v)->{
            if(c.getId() == null){
                v.setId(SnowflakeIdAble.getSnowflakeId());
            }else {
                //主营门店将ID赋值给商户ID
                v.setMerchantId(c.getId());
                v.setChannelId(c.getId());
            }
            v.setCustomNumber(0);
            v.setSelectionNumber(0);
            v.setDealerNumber(0);
            v.setDealerAmount(0);
            v.setLogo("https://zztp.zzxyg88.com/stores/storeLogo.png");
        });
        // 新建默认模板
        StoreStyle storeStyle = storeStyleRepository.copyStyle(null);
        //插入新增的模板id
        item.setStyleCode(storeStyle.getStyleCode());
        return storeRepository.storeSave(item);
    }

    @Override
    public int storeEdit(Long id, StoreSaveReq storeSaveReq) {
        Store item = TransferUtils.transfer(storeSaveReq, Store::new, (c, v)->{
            v.setId(id);
        });
        return storeRepository.storeEdit(item);
    }

    @Override
    public int storeUpdate(Store store) {
        return storeRepository.storeEdit(store);
    }

    @Override
    public Store getStoreByChannelId(Long channelId) {
        return storeRepository.storeByChannelId(channelId);
    }

    @Override
    public List<Store> storeList(List<Long> storeIdList) {
        return storeRepository.storeList(storeIdList);
    }

    @Override
    public Page<StoreSearchVO> storeSearchPage(StoreQueryReq storeQueryReq) {
        return storeRepository.storeSearchPage(storeQueryReq);
    }

    @Override
    public List<Store> getStoreList(Store store) {
        return storeRepository.getStoreList(store);
    }

    @Override
    public int storeDelete(List<Long> storeIdList) {
        return storeRepository.storeDelete(storeIdList);
    }

    @Override
    public Store store(Long storeId) {
        Store store = storeRepository.store(storeId);
        if(store != null) {
            StoreCategory detail = storeCategoryDomain.detail(store.getType());
            store.setTypeName(Opt.ofNullable(detail).map(StoreCategory::getName).orElse(""));
        }
        return store;
    }


    @Override
    public Page<StoreVO> storePage(StoreQueryReq storeQueryReq) {
        return storeRepository.storePage(storeQueryReq);
    }

    @Override
    public void cancelModelShop() {
        storeRepository.cancelModelShop();
    }
}
