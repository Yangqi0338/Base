package com.newzkl.platform.base.biz.goods.action.cmd;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 行业入参命令集合
 *
 * @author fang
 */
public class IndustryCmd {

    /**
     * 行业绑定分类命令
     */
    @Data
    public static class BindCategory {
        /**
         * 行业 ID
         */
        @NotNull
        private Long industryId;
        /**
         * 分类 ID
         */
        @NotNull
        private Long categoryId;
        /**
         * 是否绑定
         */
        private Boolean isBind;
    }
}
