package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;


import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 批量查询包裹轨迹响应类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoQueryTrackListRes extends HuiDingHuoBaseRes<List<HuiDingHuoQueryTrackListRes.OrderTrack>> {

    /**
     * 订单物流轨迹信息类
     */
    @Data
    public static class OrderTrack {
        /**
         * 系统订单号或用户自定义订单号（必填）
         */
        private String orderNum;

        /**
         * 物流轨迹列表（必填）
         */
        private List<Track> track;
    }

    /**
     * 包裹物流信息类
     */
    @Data
    public static class Track {
        /**
         * 包裹号（必填）
         */
        private String pkgNo;

        /**
         * 包裹当前物流状态（必填）
         */
        private String status;

        /**
         * 物流状态code（必填）
         */
        private Integer expressCode;

        /**
         * 订单状态（必填）
         */
        private String orderStatus;

        /**
         * 订单状态code（必填）
         */
        private Integer orderStatusCode;

        /**
         * 物流轨迹信息（必填）
         */
        private List<ExpressList> expressList;

        /**
         * 包裹内商品信息（必填）
         */
        private List<Item> itemList;

        /**
         * 变更时间戳（必填）
         */
        private String modifyTime;
    }

    /**
     * 物流轨迹详情类
     */
    @Data
    public static class ExpressList {
        /**
         * 物流公司（可选）
         */
        private String expressCompany;

        /**
         * 物流单号（可选）
         */
        private String expressNum;

        /**
         * 物流详情（必填）
         */
        private List<ExpressDetail> expressDetail;
    }

    /**
     * 物流节点详情类
     */
    @Data
    public static class ExpressDetail {
        /**
         * 时间（可选）
         */
        private String time;

        /**
         * 物流节点（必填）
         */
        private String express;
    }

    /**
     * 包裹内商品信息类
     */
    @Data
    public static class Item {
        /**
         * 渠道名称（必填）
         */
        private String channelName;

        /**
         * 渠道类型（必填）
         */
        private SpuEnum.ChannelType spuChannelType;

        /**
         * 商品id（必填）
         */
        private String itemId;

        /**
         * 商品skuId（必填）
         */
        private String skuId;

        /**
         * 商品数量（必填）
         */
        private Integer number;
    }
}