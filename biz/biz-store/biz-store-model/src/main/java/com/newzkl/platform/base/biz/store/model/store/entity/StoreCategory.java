package com.newzkl.platform.base.biz.store.model.store.entity;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;


/**
 * 门店分类 #store(StoreCategory)DTO类
 *
 * @author kc
 * @since 2026-01-27 11:21:02
 */
@Data
public class StoreCategory extends BaseRes {

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

