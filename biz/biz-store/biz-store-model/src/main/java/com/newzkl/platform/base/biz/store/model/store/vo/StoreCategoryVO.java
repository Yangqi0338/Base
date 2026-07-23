package com.newzkl.platform.base.biz.store.model.store.vo;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;


/**
 * 门店分类 #store(StoreCategory)展示类
 *
 * @author kc
 * @since 2026-01-27 11:21:03
 */
@Data
public class StoreCategoryVO extends BaseVO {

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

