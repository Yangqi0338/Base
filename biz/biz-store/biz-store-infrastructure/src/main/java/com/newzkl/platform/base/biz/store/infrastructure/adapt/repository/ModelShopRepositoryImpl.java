package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.enums.ModeShopOrderType;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.biz.store.model.template.entity.ModelShop;
import com.newzkl.platform.base.biz.store.model.template.req.AuditModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.req.QueryModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopRes;
import com.newzkl.platform.base.biz.store.domain.template.repository.ModelShopRepository;
import com.newzkl.platform.base.biz.store.infrastructure.dao.ModelShopDAO;
import com.newzkl.platform.base.biz.store.infrastructure.dao.ModelShopUseRecordDAO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.ModelShopDO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.ModelShopUseRecordDO;
import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopDataDTO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2024/4/7 16:19
 */
@Slf4j
@Repository
public class ModelShopRepositoryImpl implements ModelShopRepository {

    @Autowired
    private ModelShopDAO modelShopDAO;
    @Autowired
    private ModelShopUseRecordDAO modelShopUseRecordDAO;


    @Override
    public void create(ModelShop modelShop) {
        modelShopDAO.insert(TransferUtils.transfer(modelShop, ModelShopDO::new));
    }

    @Override
    public void update(ModelShop modelShop) {
        BaseLambdaQueryWrapper<ModelShopDO> wrapper = new BaseLambdaQueryWrapper<ModelShopDO>();
        if (modelShop.getId() != null) {
            wrapper.eq(ModelShopDO::getId, modelShop.getId());
        } else {
            wrapper.eq(ModelShopDO::getStyleCode, modelShop.getStyleCode());
        }
        modelShopDAO.update(TransferUtils.transfer(modelShop, ModelShopDO::new), wrapper);
    }

    @Override
    public void auditModelShop(AuditModelShopReq req) {
        ModelShopDO modelShopDO = new ModelShopDO();
        modelShopDO.setStyleCode(req.getStyleCode());
        modelShopDO.setAuditState(req.getAuditState());
        modelShopDO.setAuditInfo(req.getAuditInfo());
        modelShopDAO.update(TransferUtils.transfer(req, ModelShopDO::new), new BaseLambdaQueryWrapper<ModelShopDO>()
                .notEmptyEq(ModelShopDO::getStyleCode, req.getStyleCode())
        );
    }

    @Override
    public Page<ModelShopRes> queryModelShopPage(QueryModelShopReq req) {
        BaseLambdaQueryWrapper<ModelShopDO> wrapper = modelShopDAO.buildQueryWrapper(TransferUtils.transfer(req, ModelShopDO::new));
        wrapper.between(req.getCreateTimeL() != null && req.getCreateTimeR() != null, ModelShopDO::getCreateTime, req.getCreateTimeL(), req.getCreateTimeR());
        wrapper.orderByDesc(ModelShopDO::getId);
        return TransferUtils.transferPage(modelShopDAO.selectPage(com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport.page(req), wrapper), ModelShopRes::new);
    }

    @Override
    public ModelShop queryByModelShop(ModelShop modelShop) {
        return TransferUtils.transfer(modelShopDAO.selectOne(modelShopDAO.buildQueryWrapper(TransferUtils.transfer(modelShop, ModelShopDO::new))), ModelShop::new);
    }

    @Override
    public List<ModelShop> queryByModelShopList(ModelShop modelShop) {
        return TransferUtils.transfers(modelShopDAO.selectList(modelShopDAO.buildQueryWrapper(TransferUtils.transfer(modelShop, ModelShopDO::new))), ModelShop::new);
    }

    @Override
    public ModelShop queryByStyleCode(String styleCode) {
        return TransferUtils.transfer(
                modelShopDAO.selectOne(new BaseLambdaQueryWrapper<ModelShopDO>()
                        .eq(ModelShopDO::getStyleCode, styleCode)),
                ModelShop::new
        );
    }

    /**
     * 新增使用门店数
     */
    @Override
    public void updateUseStoreNum(String styleCode, Integer num) {
        modelShopDAO.update(new LambdaUpdateWrapper<ModelShopDO>()
                .setSql("use_store_num = use_store_num + (" + num + ")")
                .eq(ModelShopDO::getStyleCode, styleCode));
    }

    @Override
    public void updateModelShopData(ModelShopDataDTO dto) {
        LambdaUpdateWrapper<ModelShopDO> wrapper = new LambdaUpdateWrapper<>();
        if (ObjectUtil.equals(ModeShopOrderType.PAY.name(), dto.getType())) {
            wrapper.setSql("total_pay_amount = total_pay_amount + " + dto.getAmount())
                    .setSql("total_pay_num = total_pay_num + 1");
        }else {
            wrapper.setSql("total_order_amount = total_order_amount + " + dto.getAmount())
                    .setSql("total_order_num = total_order_num + 1");
        }
        modelShopDAO.update(wrapper.eq(ModelShopDO::getId, dto.getModelShopId()));
    }

    @Override
    public void deleteModelShop(Long id) {
        modelShopDAO.update(new LambdaUpdateWrapper<ModelShopDO>().eq(ModelShopDO::getId, id).set(ModelShopDO::getIsDelete, 1));
    }

    @Override
    public void updateTotalUseStoreNum(Long storeId, Long modelShopId) {
        boolean exists = modelShopUseRecordDAO.exists(new BaseLambdaQueryWrapper<ModelShopUseRecordDO>().eq(ModelShopUseRecordDO::getModelShopId, modelShopId).eq(ModelShopUseRecordDO::getStoreId, storeId));
        // 如果未使用过该门店，+1
        if(!exists){
            modelShopDAO.update(new LambdaUpdateWrapper<ModelShopDO>()
                    .setSql("total_use_store_num = total_use_store_num + 1")
                    .eq(ModelShopDO::getId, modelShopId));
        }
    }
}
