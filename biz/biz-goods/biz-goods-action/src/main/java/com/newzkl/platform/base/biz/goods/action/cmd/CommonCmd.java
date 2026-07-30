package com.newzkl.platform.base.biz.goods.action.cmd;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 商品域通用入参命令集合
 *
 * <p>迁移说明: 对应 new-scm 的 {@code IdListObj} / {@code StringObj} 通用请求包装类,
 * Base 通用层未提供对等类型, 故在本域 action 层内聚。</p>
 *
 * @author KC
 */
public class CommonCmd {

    /**
     * 主键列表命令 (对应 new-scm {@code IdListObj})
     */
    @Data
    public static class IdList {
        /**
         * 主键列表
         */
        @NotEmpty(message = "idList不能为空")
        private List<Long> idList;
    }

    /**
     * 字符串命令 (对应 new-scm {@code StringObj})
     */
    @Data
    public static class StringValue {
        /**
         * 字符串值
         */
        private String string;
    }
}
