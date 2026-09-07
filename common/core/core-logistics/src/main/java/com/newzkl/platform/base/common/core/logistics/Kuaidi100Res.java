package com.newzkl.platform.base.common.core.logistics;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 快递100 三方响应
 *
 * <p>本类仅承载快递100 原始报文, 不对外暴露给前端。对外统一走 {@link LogisticsTrack}</p>
 */
public class Kuaidi100Res {

    /**
     * 实时轨迹查询响应
     */
    @Data
    public static class QueryRes implements Serializable {

        /**
         * 消息体, 请忽略
         */
        private String message;

        /**
         * 通讯状态, 200 = 正常
         */
        private String status;

        /**
         * 快递单当前状态
         *
         * <p>0 = 在途, 1 = 已揽收, 2 = 疑难, 3 = 已签收, 4 = 退签,
         * 5 = 同城派送中, 6 = 退回, 7 = 转单</p>
         */
        private String state;

        /**
         * 快递单明细状态标记, 暂未实现
         */
        private String condition;

        /**
         * 是否签收标记, 1 = 已签收
         */
        private String ischeck;

        /**
         * 快递公司编码
         */
        private String com;

        /**
         * 快递单号
         */
        private String nu;

        /**
         * 轨迹节点列表, 时间倒序
         */
        private List<Trace> data;
    }

    /**
     * 轨迹节点
     */
    @Data
    public static class Trace implements Serializable {

        /**
         * 时间, 原始格式
         */
        private String time;

        /**
         * 时间, 格式化后
         */
        private String ftime;

        /**
         * 节点描述
         */
        private String context;

        /**
         * 所在地
         */
        private String location;

        /**
         * 节点状态
         */
        private String status;
    }

    /**
     * 单号识别响应
     */
    @Data
    public static class AutoNumRes implements Serializable {

        /**
         * 快递公司编码
         */
        private String comCode;

        /**
         * 单号前缀长度
         */
        private Integer lengthPre;

        /**
         * 单号位数
         */
        private Integer noCount;

        /**
         * 单号前缀
         */
        private String noPre;
    }
}
