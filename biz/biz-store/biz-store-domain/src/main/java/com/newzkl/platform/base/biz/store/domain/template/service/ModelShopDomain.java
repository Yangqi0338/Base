package com.newzkl.platform.base.biz.store.domain.template.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.template.entity.ModelShop;
import com.newzkl.platform.base.biz.store.model.template.query.ModelShopDataQuery;
import com.newzkl.platform.base.biz.store.model.template.query.ModelShopStorePageQuery;
import com.newzkl.platform.base.biz.store.model.template.req.ApplyModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.req.AuditModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.req.ModelShopUpdateReq;
import com.newzkl.platform.base.biz.store.model.template.req.QueryModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopDataRes;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopStorePageRes;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopStyleRes;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopRes;
import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopDataDTO;

import java.util.List;

/**
 * @author niu
 * @description: 样板店接口
 * @date 2024/4/7 16:00
 */
public interface ModelShopDomain {

    /**
     * 申请成为样板店
     * @param req
     * @return
     */
    void applyModelShop(ApplyModelShopReq req);

    /**
     * 审核样板店
     * @param req
     */
    void auditModelShop(AuditModelShopReq req);

    /**
     * 查询样板店分页
     * @param req
     * @return
     */
    Page<ModelShopRes> queryModelShopPage(QueryModelShopReq req);

    /**
     * 修改使用门店数
     */
    void updateUseStoreNum(String styleCode, Integer num);

    /**
     * 修改样板店数据
     */
    void updateModelShopData(ModelShopDataDTO dto);

    /**
     * 样板店门店分页
     */
    Page<ModelShopStorePageRes> modelShopStorePage(ModelShopStorePageQuery query);

    /**
     * 样板店数据
     */
    ModelShopDataRes modelShopData(ModelShopDataQuery query);

    /**
     * 根据styleCode查询样板店
     * @param styleCode 样式编码
     * @return 样板店信息
     */
    ModelShop queryByStyleCode(String styleCode);

    /**
     * 根据id查询样板店
     */
    ModelShop queryById(Long id);

    /**
     * 根据渠道id查询样板店
     */
    ModelShop queryByChannelId(Long channelId);

    /**
     * 样板店列表
     * 脉脉通展示
     */
    List<ModelShopStyleRes> queryModelShopList();

    /**
     * 删除样板店
     */
    void deleteModelShop(Long id);

    /**
     * 修改样板店
     */
    void updateModelShop(ModelShopUpdateReq req);

    /**
     * 同步样板店
     */
    void syncModelShop();

    /**
     * 修改总使用门店数
     */
    void updateTotalUseStoreNum(Long storeId, Long modelShopId);
}
