package com.newzkl.platform.base.biz.sys.action.cmd;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * sys 域 controller 入参命令集
 *
 * <p>迁移自旧 {@code com.zkl.scm.model.web.IdObj} 等通用入参壳。字段名沿用旧命名,
 * 以免改动前端契约。</p>
 *
 * @author KC
 */
public class SysCmd {

    /**
     * 单 ID 入参 (旧 {@code IdObj})
     *
     * @author KC
     */
    @Data
    public static class ID implements Serializable {

        /**
         * 主键 ID
         */
        @NotNull(message = "id?")
        private Long id;
    }

    /**
     * 物流轨迹查询入参 (旧 {@code CommonCmd.DeliverQueryReq})
     *
     * <p>字段名逐字沿用旧命名, 前端契约不变</p>
     *
     * @author KC
     */
    @Data
    public static class DeliverQueryReq implements Serializable {

        /**
         * 快递公司类型编码, 不传时由三方自动识别
         */
        private String type;

        /**
         * 快递单号
         */
        private String number;

        /**
         * 收件人手机号
         */
        private String mobile;
    }

    /**
     * 单字符串入参 (旧 {@code com.zkl.scm.model.web.StringObj})
     *
     * <p>字段名 {@code string} 逐字沿用旧命名, 前端契约不变。用于 {@code /ocrIdentify}
     * 传身份证图片 URL。</p>
     *
     * @author KC
     */
    @Data
    public static class StringObj implements Serializable {

        /**
         * 字符串值 (身份证图片 URL)
         */
        private String string;
    }
}
