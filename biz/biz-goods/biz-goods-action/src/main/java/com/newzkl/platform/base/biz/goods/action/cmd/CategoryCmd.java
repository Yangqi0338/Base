package com.newzkl.platform.base.biz.goods.action.cmd;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 分类入参命令集合
 *
 * @author fang
 */
public class CategoryCmd {

    /**
     * 分类绑定品牌命令
     */
    @Data
    public static class BindBrand {
        /**
         * 分类 ID
         */
        @NotNull
        private Long categoryId;
        /**
         * 品牌 ID
         */
        @NotNull
        private Long brandId;
        /**
         * 是否绑定
         */
        private Boolean isBind;
    }
}
