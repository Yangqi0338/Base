package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.biz.store.domain.adapt.api.DistributionApi;
import com.newzkl.platform.base.biz.store.domain.adapt.api.DistributionRandomInfo;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaUpdateWrapper;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.query.StoreQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreSearchRes;
import com.newzkl.platform.base.biz.store.model.store.res.StoreRes;
import com.newzkl.platform.base.biz.store.domain.store.repository.StoreRepository;
import com.newzkl.platform.base.biz.store.infrastructure.dao.StoreDAO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreDO;
import com.newzkl.platform.base.biz.store.domain.adapt.api.UserFollowApi;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* 门店
* @author fang
*/
@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {

    private final StoreDAO storeDAO;
    private final DistributionApi distributionApi;
    private final UserFollowApi userFollowApi;
    @Override
    public Long storeSave(Store store) {
        StoreDO storeDO = TransferUtils.transfer(store, StoreDO::new);
        storeDAO.insert(storeDO);
        return storeDO.getId();
    }
    @Override
    public int storeEdit(Store store) {
        // 不能修改门店类型
        store.setType(null);
        return storeDAO.update(TransferUtils.transfer(store, StoreDO::new), new BaseLambdaQueryWrapper<StoreDO>()
                .notEmptyEq(StoreDO::getId, store.getId())
                .notEmptyEq(StoreDO::getChannelId, store.getChannelId())
        );
    }

    @Override
    public int storeDelete(List<Long> storeIdList) {
        return storeDAO.deleteByIds(storeIdList);
    }

    @Override
    public Store store(Long storeId) {
        return TransferUtils.transfer(storeDAO.selectById(storeId), Store::new);
    }

    @Override
    public Map<String, Integer> countStoreByStyleCodes(List<String> styleCodes) {
        if (CollUtil.isEmpty(styleCodes)) {
            return Map.of();
        }
        List<StoreDO> storeDOList = storeDAO.selectList(
                new LambdaQueryWrapper<StoreDO>().in(StoreDO::getStyleCode, styleCodes));
        return storeDOList.stream().collect(Collectors.groupingBy(
                StoreDO::getStyleCode, Collectors.summingInt(x -> 1)));
    }

    @Override
    public List<Store> storeList(List<Long> storeIdList) {
        return TransferUtils.transfers(storeDAO.selectByIds(storeIdList), Store::new);
    }

    @Override
    public Store storeByChannelId(Long channelId) {
        return TransferUtils.transfer(storeDAO.selectOne(new LambdaQueryWrapper<StoreDO>().eq(StoreDO::getChannelId, channelId)), Store::new);
    }

    @Override
    public Page<StoreRes> storePage(StoreQuery storeQueryReq) {
        BaseLambdaQueryWrapper<StoreDO> wrapper = storeDAO.buildQueryWrapper(TransferUtils.transfer(storeQueryReq, StoreDO::new));
        wrapper.notEmptyIn(StoreDO::getId, storeQueryReq.getIdList());
        return TransferUtils.transferPage(storeDAO.selectPage(com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport.page(storeQueryReq), wrapper), StoreRes::new,(source, target) -> {

            target.setFanNumber(userFollowApi.countFollower(source.getChannelId()));
        });
    }

    @Override
    public void cancelModelShop() {
        storeDAO.update(new BaseLambdaUpdateWrapper<StoreDO>().set(StoreDO::getModelShopId, null).eq(StoreDO::getId, SecurityUtils.getAccountId()));
    }

    @Override
    public Page<StoreSearchRes> storeSearchPage(StoreQuery storeQueryReq) {
        Page<StoreSearchRes> page = TransferUtils.transferPage(this.storePage(storeQueryReq), StoreSearchRes::new);
        List<StoreSearchRes> records = page.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            List<Long> collect = records.stream().map(StoreSearchRes::getId).collect(Collectors.toList());
            Map<Long, List<DistributionRandomInfo>> longListMap = distributionApi.queryRandomDistributionByStoreIdList(collect, 3);
            records.forEach(v -> {
                List<DistributionRandomInfo> voList = longListMap.get(v.getId());
                List<StoreSearchRes.GoodsVO> goodsVOList = TransferUtils.transfers(voList, StoreSearchRes.GoodsVO::new, (source, target) -> {
                    target.setDistributionId(source.getId());
                });
                v.setGoodsVOList(goodsVOList);
            });
        }
        return page;
    }

    @Override
    public List<Store> getStoreList(Store store) {
        BaseLambdaQueryWrapper<StoreDO> wrapper = new BaseLambdaQueryWrapper<StoreDO>()
                .notEmptyEq(StoreDO::getId, store.getId())
                .notEmptyEq(StoreDO::getName, store.getName())
                .notEmptyEq(StoreDO::getChannelId, store.getChannelId())
                .notEmptyEq(StoreDO::getModelShopId, store.getModelShopId())
//                .notEmptyEq(StoreDO::getCategoryId, store.getType())
                .notEmptyEq(StoreDO::getIsModelShop, store.getIsModelShop())
                .notEmptyEq(StoreDO::getStyleCode, store.getStyleCode());
        return TransferUtils.transfers(storeDAO.selectList(wrapper), Store::new);
    }
}
