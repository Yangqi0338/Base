package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;


import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 获取渠道类型列表响应类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetChannelTypeListRes extends HuiDingHuoBaseRes<List<HuiDingHuoGetChannelTypeListRes.ChannelType>> {

    /**
     * 渠道（发货方式）信息类
     */
    @Data
    public static class ChannelType {
        /**
         * 渠道码（必填）
         */
        private SpuEnum.ChannelType spuChannelType;

        /**
         * 渠道名称（必填）
         */
        private String channelTypeName;

        /**
         * 发货时间（必填，如"1-3"）
         */
        private String deliverDays;

        /**
         * 发货时间单位（必填，如"工作日"）
         */
        private String deliverDaysUnit;

        /**
         * 到货时间（必填，如"3-7"）
         */
        private String arrivalDays;

        /**
         * 到货时间单位（必填，如"工作日"）
         */
        private String arrivalDaysUnit;

        /**
         * 图标地址（必填）
         */
        private String iconUrl;

        /**
         * 是否需要校验身份证（必填，0：否，1：是）
         */
        private Integer needVerifyIdentity;
    }
}