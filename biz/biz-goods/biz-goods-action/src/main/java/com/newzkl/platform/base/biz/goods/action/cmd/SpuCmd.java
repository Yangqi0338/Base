package com.newzkl.platform.base.biz.goods.action.cmd;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 商品入参命令集合
 *
 * @author fang
 */
public class SpuCmd {

    /**
     * 商品 ID 命令
     */
    @Data
    public static class Id {
        /**
         * 商品 ID
         */
        @NotNull
        private Long id;
    }

    /**
     * 商品上下架命令
     */
    @Data
    public static class UpCommand {
        /**
         * 上下架状态
         */
        @NotNull
        private Integer enable;
        /**
         * 商品 ID 列表
         */
        @NotEmpty
        private List<Long> spuIdList;
    }
}
