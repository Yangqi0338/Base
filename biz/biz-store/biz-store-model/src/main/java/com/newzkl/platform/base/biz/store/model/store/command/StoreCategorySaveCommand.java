package com.newzkl.platform.base.biz.store.model.store.command;

import lombok.Data;


/**
 * 门店分类 #store(StoreCategory)DTO类
 *
 * @author kc
 * @since 2026-01-27 11:21:02
 */
@Data
public class StoreCategorySaveCommand {

    /**
     * 主键
     */
    private Long id;

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

