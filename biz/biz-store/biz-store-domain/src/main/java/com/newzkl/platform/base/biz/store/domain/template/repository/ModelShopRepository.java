package com.newzkl.platform.base.biz.store.domain.template.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.template.entity.ModelShop;
import com.newzkl.platform.base.biz.store.model.template.req.AuditModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.req.QueryModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopRes;
import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopDataDTO;

import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2024/4/7 16:14
 */
public interface ModelShopRepository {

    /**
     * 新增样板店
     * @return
     */
    void create(ModelShop modelShop);

    /**
     * 修改样板店
     * @return
     */
    void update(ModelShop modelShop);

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
     * 查询样板店
     */
    ModelShop queryByModelShop(ModelShop modelShop);

    /**
     * 查询样板店集合
     */
    List<ModelShop> queryByModelShopList(ModelShop modelShop);

    /**
     * 根据styleCode查询样板店
     * @param styleCode 样式编码
     * @return 样板店信息
     */
    ModelShop queryByStyleCode(String styleCode);

    /**
     * 新增使用门店数
     */
    void updateUseStoreNum(String styleCode, Integer num);

    /**
     * 修改样板店数据
     */
    void updateModelShopData(ModelShopDataDTO dto);

    /**
     * 删除样板店
     */
    void deleteModelShop(Long id);

    /**
     * 修改总使用门店数
     */
    void updateTotalUseStoreNum(Long storeId, Long modelShopId);
}
