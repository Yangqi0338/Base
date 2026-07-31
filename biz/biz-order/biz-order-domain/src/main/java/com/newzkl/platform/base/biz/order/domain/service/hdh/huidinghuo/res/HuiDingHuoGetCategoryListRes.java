package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 获取类目列表响应类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetCategoryListRes extends HuiDingHuoBaseRes<List<HuiDingHuoGetCategoryListRes.Category>> {

    /**
     * 类目信息类（支持递归嵌套子类目）
     */
    @Data
    public static class Category {
        /**
         * 类目id（必填）
         */
        private Long id;

        /**
         * 类目名称（必填）
         */
        private String name;

        /**
         * 类目logo图片地址（可选）
         */
        private String logoUrl;

        /**
         * 子类目信息（可选，递归结构）
         */
        private List<Category> subCategoryList;
    }
}