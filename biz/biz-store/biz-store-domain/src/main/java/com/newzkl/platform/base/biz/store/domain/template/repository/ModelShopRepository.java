package com.newzkl.platform.base.biz.store.domain.template.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopDTO;
import com.newzkl.platform.base.biz.store.model.template.req.AuditModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.req.ModelShopQuery;
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
    void create(ModelShopDTO modelShop);

    /**
     * 修改样板店
     * @return
     */
    void update(ModelShopDTO modelShop);

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
    Page<ModelShopRes> queryModelShopPage(ModelShopQuery query);

    /**
     * 查询样板店
     */
    ModelShopDTO queryByModelShop(ModelShopQuery query);

    /**
     * 查询样板店集合
     */
    List<ModelShopDTO> queryByModelShopList(ModelShopQuery query);

    /**
     * 根据styleCode查询样板店
     * @param styleCode 样式编码
     * @return 样板店信息
     */
    ModelShopDTO queryByStyleCode(String styleCode);

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
