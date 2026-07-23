package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreZoneBackgroundImage;
import com.newzkl.platform.base.biz.store.domain.store.repository.StoreZoneBackgroundImageRepository;
import com.newzkl.platform.base.biz.store.infrastructure.dao.StoreZoneBackgroundImageDAO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreZoneBackgroundImageDO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 门店专区背景图仓储实现
 */
@Repository
public class StoreZoneBackgroundImageRepositoryImpl extends ServiceImpl<StoreZoneBackgroundImageDAO, StoreZoneBackgroundImageDO> implements StoreZoneBackgroundImageRepository {

    @Override
    public void createBatchImage(List<StoreZoneBackgroundImage> imageList) {
        try {
            boolean result = this.saveBatch(TransferUtils.transfers(imageList, StoreZoneBackgroundImageDO::new));
            if (!result) {
                throw new RuntimeException("批量新增门店专区背景图失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("批量新增事务失败", e);
        }
    }
}