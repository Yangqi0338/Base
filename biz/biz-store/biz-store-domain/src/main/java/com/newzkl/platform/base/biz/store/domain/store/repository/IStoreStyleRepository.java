package com.newzkl.platform.base.biz.store.domain.store.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreStyle;
import com.newzkl.platform.base.biz.store.model.store.req.StoreStylePageQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreStyleResponse;

import java.util.List;

/**
 * 门店样式仓储接口
 */
public interface IStoreStyleRepository {
    Page<StoreStyleResponse> storeStylePage(StoreStylePageQuery query);

    void create(StoreStyle storeStyle);

    void update(StoreStyle storeStyle);

    /**
     * 删除默认样式
     */
    void deleteDefaultStyle();

    /**
     * 获取默认样式
     */
    StoreStyle getDefaultStyle();

    /**
     * 复制模版样式
     */
    StoreStyle copyStyle(String styleCode);

    List<StoreStyle> getByStoreStyleList(List<String> storeStyleList);

    StoreStyle getByStyleCode(String storeStyle);

    /**
     * 获取自己的模板
     */
    StoreStyle getOneselfStyle(String storeStyle);

    /**
     * 删除复制的样板店模板
     */
    void deleteCopyStyle(String styleCode);

}