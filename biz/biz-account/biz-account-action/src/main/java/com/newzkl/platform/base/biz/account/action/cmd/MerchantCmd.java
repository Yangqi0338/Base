package com.newzkl.platform.base.biz.account.action.cmd;

import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantReq;
import com.newzkl.platform.base.biz.account.model.merchant.vo.WxMpConfigVO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商户接口入参命令集。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.MerchantCmd}。
 * 字段名一律沿用旧命名 (如 {@code merchantIdList} / {@code merchantCommand}), 以免改动前端契约;
 * 旧 {@code Cdk} 命令中的 {@code serviceId} / {@code openPhone} 仅 KBB / 三方兑换链路使用,
 * {@code /merchant/useStoreCdk} 未读取, 故不迁移。旧 {@code OrderPay} 命令随 orderPay 端点一并延后。</p>
 *
 * @author KC
 */
public class MerchantCmd {

    /**
     * 单 ID 入参 (旧 {@code IdObj})。
     *
     * @author KC
     */
    @Data
    public static class ID implements Serializable {

        /**
         * 商户 / 渠道商 ID
         */
        @NotNull(message = "id?")
        private Long id;
    }

    /**
     * ID 列表入参。
     *
     * @author KC
     */
    @Data
    public static class IDList implements Serializable {

        /**
         * 商户 ID 列表
         */
        @NotEmpty
        private List<Long> merchantIdList;
    }

    /**
     * 商户修改入参。
     *
     * @author KC
     */
    @Data
    public static class Edit implements Serializable {

        /**
         * 目标商户 ID (以此为更新目标, 而非请求体内的 id)
         */
        private Long id;

        /**
         * 商户字段
         */
        private MerchantReq merchantCommand;
    }

    /**
     * 微信公众号配置入参。
     *
     * @author KC
     */
    @Data
    public static class EditWxMpConfigVO implements Serializable {

        /**
         * 微信公众号配置
         */
        private WxMpConfigVO wxMpConfigVO;
    }

    /**
     * 门店开通码入参。
     *
     * @author KC
     */
    @Data
    public static class Cdk implements Serializable {

        /**
         * 开通码值
         */
        @NotNull(message = "兑换码Code?")
        private String cdk;
    }
}
