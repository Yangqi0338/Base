package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaUpdateWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.ddd.model.enums.ModeShopOrderType;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopDTO;
import com.newzkl.platform.base.biz.store.model.template.req.AuditModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.req.ModelShopQuery;
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
public class ModelShopRepositoryImpl extends RepositorySupport implements ModelShopRepository {

    @Autowired
    private ModelShopDAO modelShopDAO;
    @Autowired
    private ModelShopUseRecordDAO modelShopUseRecordDAO;


    @Override
    public void create(ModelShopDTO modelShop) {
        modelShopDAO.insert(TransferUtils.transfer(modelShop, ModelShopDO::new));
    }

    @Override
    public void update(ModelShopDTO modelShop) {
        ModelShopQuery query = new ModelShopQuery();
        query.setId(modelShop.getId());
        query.setStyleCode(modelShop.getStyleCode());
        modelShopDAO.update(TransferUtils.transfer(modelShop, ModelShopDO::new), modelShopDAO.getLw(query));
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
    public Page<ModelShopRes> queryModelShopPage(ModelShopQuery query) {
        BaseLambdaQueryWrapper<ModelShopDO> wrapper = modelShopDAO.getLw(query);

        return TransferUtils.transferPage(modelShopDAO.selectPage(com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport.page(query), wrapper), ModelShopRes::new);
    }

    @Override
    public ModelShopDTO queryByModelShop(ModelShopQuery query) {
        return TransferUtils.transfer(modelShopDAO.selectOne(modelShopDAO.getLw(query)), ModelShopDTO::new);
    }

    @Override
    public List<ModelShopDTO> queryByModelShopList(ModelShopQuery query) {
        return TransferUtils.transfers(modelShopDAO.selectList(modelShopDAO.getLw(query)), ModelShopDTO::new);
    }

    @Override
    public ModelShopDTO queryByStyleCode(String styleCode) {
        return TransferUtils.transfer(
                modelShopDAO.selectOne(new BaseLambdaQueryWrapper<ModelShopDO>()
                        .eq(ModelShopDO::getStyleCode, styleCode)),
                ModelShopDTO::new
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
        BaseLambdaUpdateWrapper<ModelShopDO> wrapper = new BaseLambdaUpdateWrapper<>();
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
        modelShopDAO.deleteById(id);
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
