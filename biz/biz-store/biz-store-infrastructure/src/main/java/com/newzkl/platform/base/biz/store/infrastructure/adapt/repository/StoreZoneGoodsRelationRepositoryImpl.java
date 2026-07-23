package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreZoneGoodsRelation;
import com.newzkl.platform.base.biz.store.domain.store.repository.StoreZoneGoodsRelationRepository;
import com.newzkl.platform.base.biz.store.infrastructure.dao.StoreZoneGoodsRelationDAO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreZoneGoodsRelationDO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 门店专区商品关系仓储接口
 */
@Repository
public class StoreZoneGoodsRelationRepositoryImpl extends ServiceImpl<StoreZoneGoodsRelationDAO, StoreZoneGoodsRelationDO> implements StoreZoneGoodsRelationRepository {

    @Override
    public void createBatchRelation(List<StoreZoneGoodsRelation> relationList) {
        try {
            boolean result = this.saveBatch(TransferUtils.transfers(relationList, StoreZoneGoodsRelationDO::new));
            if (!result) {
                throw new RuntimeException("批量新增门店专区商品失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("批量新增事务失败", e);
        }
    }

}