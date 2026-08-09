package com.newzkl.platform.base.biz.store.domain.store.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreStyle;
import com.newzkl.platform.base.biz.store.model.store.req.StoreStyleCreateReq;
import com.newzkl.platform.base.biz.store.model.store.query.StoreStyleQuery;
import com.newzkl.platform.base.biz.store.model.store.req.StoreStyleUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.req.SupplierTemplateUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleResponse;
import com.newzkl.platform.base.biz.store.model.store.res.SupplierTemplateRes;

import java.util.List;

/**
 * 门店样式
 */
public interface StoreStyleDomain {

    Page<StoreStyleResponse> storeStylePage(StoreStyleQuery req);

    void create(StoreStyleCreateReq req);

    void updateByCode(StoreStyleUpdateReq req);

    List<SupplierTemplateRes> supplierTemplateList(Long accountId);

    void supplierTemplateUpdate(SupplierTemplateUpdateReq req);

    /**
     * 获取自己的模板
     */
    StoreStyle getOneselfStyle(String storeStyle);

    /**
     * 删除复制的样板店模板
     */
    void deleteCopyStyle(String styleCode);

    /**
     * 复制模版样式
     */
    StoreStyle copyStyle(String styleCode);

    StoreStyle getByStyleCode(String styleCode);

}
