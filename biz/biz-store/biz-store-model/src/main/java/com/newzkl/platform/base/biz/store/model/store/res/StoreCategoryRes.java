package com.newzkl.platform.base.biz.store.model.store.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;


/**
 * 门店分类 #store(StoreCategory)展示类
 *
 * @author kc
 * @since 2026-01-27 11:21:03
 */
@Data
public class StoreCategoryRes extends BaseRes {

    /**
     * 门店名称
     */
    private String name;

    /**
     * 图标
     */
    private String logo;

    /**
     * 排序
     */
    private Integer index;

}

