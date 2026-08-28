package com.newzkl.platform.base.biz.store.domain.template.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.BIEnum;
import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopDTO;
import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopOrderRecordDTO;
import com.newzkl.platform.base.biz.store.model.template.query.ModelShopDataQuery;
import com.newzkl.platform.base.biz.store.model.template.query.ModelShopStorePageQuery;
import com.newzkl.platform.base.biz.store.model.template.req.ApplyModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.req.AuditModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.req.ModelShopUpdateReq;
import com.newzkl.platform.base.biz.store.model.template.req.ModelShopQuery;
import com.newzkl.platform.base.biz.store.model.template.res.ModeShopDataSummary;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopDataRes;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopStorePageRes;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopStyleRes;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopRes;
import com.newzkl.platform.base.biz.store.domain.template.repository.ModelShopOrderRecordRepository;
import com.newzkl.platform.base.biz.store.domain.template.repository.ModelShopRepository;
import com.newzkl.platform.base.biz.store.domain.template.repository.ModelShopUseRecordRepository;
import com.newzkl.platform.base.biz.store.domain.template.service.ModelShopDomain;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreStyle;
import com.newzkl.platform.base.biz.store.domain.store.repository.StoreRepository;
import com.newzkl.platform.base.biz.store.domain.store.repository.StoreStyleRepository;
import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopDataDTO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author niu
 * @description:
 * @date 2024/4/7 16:13
 */
@Service
public class ModelShopDomainImpl implements ModelShopDomain {

    @Autowired
    private ModelShopRepository modelShopRepository;
    @Autowired
    private StoreStyleRepository storeStyleRepository;
    @Autowired
    private ModelShopOrderRecordRepository modelShopOrderRecordRepository;
    @Autowired
    private ModelShopUseRecordRepository modelShopUseRecordRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyModelShop(ApplyModelShopReq req) {
        ModelShopQuery query = new ModelShopQuery();
        query.setChannelId(SecurityUtils.getAccountId());
        ModelShopDTO modelShop = modelShopRepository.queryByModelShop(query);
        if (modelShop == null) {
            //复制模板
            StoreStyle storeStyle = storeStyleRepository.copyStyle(null);

            //新增样板店
            modelShop = new ModelShopDTO();
            modelShop.setModelShopName(req.getModelShopName());
            modelShop.setModelDescription(req.getModelDescription());
            modelShop.setChannelId(SecurityUtils.getAccountId());
            modelShop.setStyleCode(storeStyle.getStyleCode());
            modelShop.setCreatorId(SecurityUtils.getAccountId());
            modelShop.setCreatorName(SecurityUtils.getNickName());
            modelShopRepository.create(modelShop);
        } else if (AuditEnum.State.FAIL == modelShop.getAuditState()) {
            modelShop.setId(modelShop.getId());
            modelShop.setAuditState(AuditEnum.State.AUDITING);
            modelShop.setAuditInfo(null);
            modelShopRepository.update(modelShop);
        }
    }

    @Override
    public void auditModelShop(AuditModelShopReq req) {
        modelShopRepository.auditModelShop(req);

        if (AuditEnum.State.SUCCESS == req.getAuditState()) {
            ModelShopDTO modelShop = modelShopRepository.queryByStyleCode(req.getStyleCode());
            Store store = new Store();
            store.setId(modelShop.getChannelId());
            store.setIsModelShop(1);
            store.setModelShopId(modelShop.getId());
            storeRepository.storeEdit(store);
        }
    }

    @Override
    public Page<ModelShopRes> queryModelShopPage(ModelShopQuery req) {
        Page<ModelShopRes> page = modelShopRepository.queryModelShopPage(req);
        //填充模板数据
        List<ModelShopRes> records = page.getRecords();
        List<String> collect = page.getRecords().stream().map(ModelShopRes::getStyleCode).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(collect)) {
            List<StoreStyle> storeStyleList = storeStyleRepository.getByStoreStyleList(collect);
            if (CollectionUtil.isNotEmpty(storeStyleList)) {
                Map<String, StoreStyle> map = storeStyleList.stream().collect(Collectors.toMap(StoreStyle::getStyleCode, item -> item));
                records.forEach(item -> {
                    StoreStyle storeStyle = map.get(item.getStyleCode());
                    if (storeStyle != null) {
                        item.setSourceCode(storeStyle.getSourceCode());
                        item.setSourceName(storeStyle.getSourceName());
                        item.setPreviewImage(storeStyle.getPreviewImage());
                    }
                });
            }
        }
        return page;
    }

    @Override
    public Page<ModelShopStorePageRes> modelShopStorePage(ModelShopStorePageQuery query) {
        if(StrUtil.isBlank(query.getStoreName())){
            Store store = new Store();
            store.setName(query.getStoreName());
            List<Long> storeIdList = storeRepository.getStoreList(store).stream().map(Store::getId).collect(Collectors.toList());
            query.setStoreIdList(storeIdList);
        }

        Page<ModelShopStorePageRes> page = modelShopUseRecordRepository.modelShopStorePage(query);

        // 如果分页结果为空，直接返回
        if (page.getRecords().isEmpty()) {
            return page;
        }


        return page;
    }

    @Override
    public ModelShopDataRes modelShopData(ModelShopDataQuery query) {
        ModelShopQuery modelShop = new ModelShopQuery();
        modelShop.setId(query.getModelShopId());
        // 查询样板店基本信息
        ModelShopDataRes shopDataRes = TransferUtils.transfer(
                modelShopRepository.queryByModelShop(modelShop),
                ModelShopDataRes::new
        );

        if (shopDataRes == null) {
            return null;
        }

        // 根据维度计算时间范围
        Date endTime = new Date();
        DateTime startTime = BIEnum.Dimension.findStartTime(query.getDimension(), endTime);

        if (startTime == null) {
            return shopDataRes;
        }

        LocalDateTime startDateTime = LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault());
        LocalDateTime endDateTime = LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault());

        // 按天统计支付订单数量
        List<ModeShopDataSummary> payOrderSummary = modelShopOrderRecordRepository
                .countPayOrderByDay(query.getModelShopId(), startDateTime, endDateTime);
        shopDataRes.setPayOrderSummary(payOrderSummary);

        // 按天统计支付订单金额
        List<ModeShopDataSummary> payAmountSummary = modelShopOrderRecordRepository
                .sumPayAmountByDay(query.getModelShopId(), startDateTime, endDateTime);
        shopDataRes.setPayAmountSummary(payAmountSummary);

        // 按天统计新增使用门店数量
        List<ModeShopDataSummary> useSummary = modelShopUseRecordRepository
                .countNewStoreByDay(query.getModelShopId(), startDateTime, endDateTime);
        shopDataRes.setUseSummary(useSummary);

        return shopDataRes;
    }

    @Override
    public ModelShopDTO queryByStyleCode(String styleCode) {
        return modelShopRepository.queryByStyleCode(styleCode);
    }

    @Override
    public ModelShopDTO queryById(Long id) {
        ModelShopQuery modelShop = new ModelShopQuery();
        modelShop.setId(id);
        return modelShopRepository.queryByModelShop(modelShop);
    }

    @Override
    public ModelShopDTO queryByChannelId(Long channelId) {
        ModelShopQuery modelShop = new ModelShopQuery();
        modelShop.setChannelId(channelId);
        return modelShopRepository.queryByModelShop(modelShop);
    }

    @Override
    public List<ModelShopStyleRes> queryModelShopList() {
        List<ModelShopStyleRes> modelShopStyleVOList = new ArrayList<>();

        // 添加已上线的样板店
        ModelShopQuery query = new ModelShopQuery();
        query.setAuditState(AuditEnum.State.SUCCESS);
        query.setState(1);
        List<ModelShopDTO> modelShops = modelShopRepository.queryByModelShopList(query);
        if (CollectionUtil.isNotEmpty(modelShops)) {
            modelShopStyleVOList.addAll(TransferUtils.transfers(modelShops, ModelShopStyleRes::new));
        }

        if (modelShopStyleVOList.isEmpty()) {
            return modelShopStyleVOList;
        }

        // 批量查询样式信息
        List<String> styleCodeList = modelShopStyleVOList.stream()
                .map(ModelShopStyleRes::getStyleCode)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        List<StoreStyle> storeStyleList = storeStyleRepository.getByStoreStyleList(styleCodeList);
        Map<String, StoreStyle> styleMap = CollectionUtil.isEmpty(storeStyleList)
                ? Collections.emptyMap()
                : storeStyleList.stream().collect(Collectors.toMap(StoreStyle::getStyleCode, Function.identity(), (a, b) -> a));

        // 查询当前登录用户的门店
        Store store = storeRepository.storeByChannelId(SecurityUtils.getAccountId());
        Long storeModelShopId = (store != null) ? store.getModelShopId() : null;

        // 填充样式数据及使用状态
        modelShopStyleVOList.forEach(item -> {
            StoreStyle storeStyle = styleMap.get(item.getStyleCode());
            if (storeStyle != null) {
                item.setStyleContent(storeStyle.getStyleContent());
                item.setGoodsIdListStr(storeStyle.getGoodsIdListStr());
                item.setPreviewImage(storeStyle.getPreviewImage());
            }
            if (ObjectUtil.equals(item.getId(), storeModelShopId)) {
                item.setInUse(true);
            }
        });

        return modelShopStyleVOList;
    }

    @Override
    public void deleteModelShop(Long id) {
        modelShopRepository.deleteModelShop(id);
    }

    @Override
    public void updateModelShop(ModelShopUpdateReq req) {
        modelShopRepository.update(TransferUtils.transfer(req, ModelShopDTO::new));
    }

    @Override
    public void syncModelShop() {
        Store store = storeRepository.store(SecurityUtils.getAccountId());
        if (store != null && store.getModelShopId() != null) {
            ModelShopQuery modelShopQuery = new ModelShopQuery();
            modelShopQuery.setId(store.getModelShopId());
            ModelShopDTO modelShop = modelShopRepository.queryByModelShop(modelShopQuery);
            // 同步后的新样式
            StoreStyle storeStyle = storeStyleRepository.copyStyle(store.getStyleCode());
            // 删除旧的复制样式
            storeStyleRepository.deleteCopyStyle(modelShop.getStyleCode());
            // 更新样板店的样式
            modelShop.setStyleCode(storeStyle.getStyleCode());
            modelShopRepository.update(modelShop);
        }
    }

    @Override
    public void updateTotalUseStoreNum(Long storeId, Long modelShopId) {
        modelShopRepository.updateTotalUseStoreNum(storeId, modelShopId);
    }

    @Override
    public void updateModelShopData(ModelShopDataDTO dto) {
        Store store = storeRepository.store(dto.getStoreId());
        if (store != null) {
            dto.setModelShopId(store.getModelShopId());
            //修改数据
            modelShopRepository.updateModelShopData(dto);
            //生成记录
            modelShopOrderRecordRepository.create(TransferUtils.transfer(dto, ModelShopOrderRecordDTO::new));
        }
    }
}