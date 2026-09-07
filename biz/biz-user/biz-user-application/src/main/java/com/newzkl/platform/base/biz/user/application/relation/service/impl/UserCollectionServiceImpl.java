package com.newzkl.platform.base.biz.user.application.relation.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.application.relation.service.UserCollectionService;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.UserCollectionRepository;
import com.newzkl.platform.base.biz.user.model.relation.req.UncollectedProductReq;
import com.newzkl.platform.base.biz.user.model.relation.req.UserCollectionCreateReq;
import com.newzkl.platform.base.biz.user.model.relation.query.UserCollectionQuery;
import com.newzkl.platform.base.biz.user.model.relation.req.UserCollectionReq;
import com.newzkl.platform.base.biz.user.model.relation.dto.UserCollectionDTO;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户收藏应用服务实现
 *
 * <p>编排"查已删记录 → 复活 or 新建"。跨域缺口见 {@code UserCollectionService} 类注释。</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCollectionServiceImpl implements UserCollectionService {

    private final UserCollectionRepository userCollectionRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCollectionDTO collectProduct(UserCollectionCreateReq req) {
        Long userId = req.getUserId();
        Long storeGoodsId = req.getStoreGoodsId();

        return userCollectionRepository.findWithDeletedByUserIdAndProductId(userId, storeGoodsId)
                .orElseGet(() -> {
                    // TODO[infra-gap] 旧实现先经 IDistributionRpcFacade.selectById 校验铺货并回填
                    //  storeId/spuId/skuId/price，中台无 market 出站端口，改用入参快照。
                    UserCollectionDTO newCollection = TransferUtils.transfer(req, UserCollectionDTO::new, (source, target) -> {
                        target.setIsValid(true);
                        target.setIsDeleted(false);
                        target.setCollectionTime(LocalDateTime.now());
                    });
                    UserCollectionDTO saved = userCollectionRepository.save(newCollection);
                    log.info("用户[{}]新增收藏：铺货ID={}, SPU={}, SKU={}",
                            userId, storeGoodsId, req.getSpuId(), req.getSkuId());
                    return saved;
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uncollectProduct(UncollectedProductReq req) {
        List<Long> idList = new ArrayList<>();
        CollUtil.addAll(idList, req.getIdList());
        if (CollUtil.isEmpty(idList)) {
            UserCollectionQuery query = new UserCollectionQuery();
            query.setUserId(SecurityUtils.getAccountId());
            query.setStoreId(req.getStoreId());
            query.setSpuId(req.getGoodsId());
            query.resetQueryList();
            List<UserCollectionDTO> list = userCollectionRepository.findPageByUserId(query).getRecords();
            list.forEach(userCollection -> idList.add(userCollection.getId()));
        }
        idList.forEach(userCollectionRepository::logicDeleteByUserIdAndProductId);
        return true;
    }

    @Override
    public Boolean uncollectedInvalidProduct() {
        return userCollectionRepository.uncollectedInvalidProduct(SecurityUtils.getAccountId());
    }

    @Override
    public List<UserCollectionDTO> getUserCollections(Long userId) {
        // TODO[infra-gap] 旧实现经 ISpuFacade/IDistributionRpcFacade 富化商品名/主图/售价/销量，
        //  中台无对应出站端口，出参保持收藏时刻快照。
        return userCollectionRepository.findByUserId(userId);
    }

    @Override
    public boolean checkIsCollected(UserCollectionReq req) {
        return userCollectionRepository
                .findWithDeletedByUserIdAndProductId(req.getUserId(), req.getStoreGoodsId())
                .isPresent();
    }

    @Override
    public Page<UserCollectionDTO> getUserCollectionsPage(UserCollectionQuery query) {
        // TODO[infra-gap] 同 getUserCollections，商品实时数据未富化（含 sellNum）。
        return userCollectionRepository.findPageByUserId(query);
    }
}
