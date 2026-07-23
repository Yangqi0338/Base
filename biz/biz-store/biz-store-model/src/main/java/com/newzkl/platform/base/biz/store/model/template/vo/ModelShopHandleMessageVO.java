package com.newzkl.platform.base.biz.store.model.template.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 样板店处理消息VO
 * @date 2024/4/8 11:51
 */
@Data
public class ModelShopHandleMessageVO {

    private Long id;

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 渠道商名称
     */
    private String channelName;

    /**
     * 交易师id
     */
    private Long tradersId;

    /**
     * 运营商id
     */
    private Long operatorId;

    /**
     * 交易师消息
     */
    private String tradersMessage;

    /**
     * 运营商消息
     */
    private String operatorMessage;

    /**
     * 处理节点  0：待交易师处理  1：待运营商处理  2：处理结束  3:取消
     */
    private Integer handleNode;

    /**
     * 处理信息内容(目前只有申请样板店,为样板店id)
     */
    private String handleInfo;


    /**
     * 申请时间
     */
    private LocalDateTime createTime;
}
