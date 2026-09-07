package com.newzkl.platform.base.common.core.logistics;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 物流轨迹, 一个对象 = 一个包裹
 *
 * <p>打平结构, 屏蔽三方报文差异。多包裹订单对应多元素 List</p>
 *
 * <p>公司与单号字段与发货单 {@code express*} 口径一致, 前端渲染轨迹与渲染发货信息取同名字段</p>
 */
@Data
public class LogisticsTrack implements Serializable {

    /**
     * 快递公司名称
     */
    private String expressCompanyName;

    /**
     * 快递公司编码
     */
    private String expressCompanyCode;

    /**
     * 快递单号
     */
    private String expressNo;

    /**
     * 物流状态码
     *
     * <p>0 = 在途, 1 = 已揽收, 2 = 疑难, 3 = 已签收, 4 = 退签,
     * 5 = 同城派送中, 6 = 退回, 7 = 转单</p>
     */
    private Integer state;

    /**
     * 物流状态文案
     */
    private String stateText;

    /**
     * 是否已签收
     */
    private Boolean signed;

    /**
     * 轨迹节点列表, 时间倒序
     */
    private List<Node> nodes;

    /**
     * 轨迹节点
     */
    @Data
    public static class Node implements Serializable {

        /**
         * 时间
         */
        private String time;

        /**
         * 节点状态
         */
        private String status;

        /**
         * 节点描述
         */
        private String content;

        /**
         * 所在地
         */
        private String location;
    }
}
