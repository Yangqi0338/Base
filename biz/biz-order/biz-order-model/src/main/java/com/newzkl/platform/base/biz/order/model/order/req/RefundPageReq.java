package com.newzkl.platform.base.biz.order.model.order.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
* 售后单
* @author fang
*/
@Data
public class RefundPageReq extends Page<RefundVO> {
    /**
     * 售后单号
     */
    private Long id;
    /**
     * SPU订单号
     */
    private String spuOrderNo;
    /**
     * 商户ID
     */
    private Long merchantId;
    /**
     * 售后单号集合
     */
    private List<Long> idList;
    /**
     * 渠道商ID
     */
    private Long channelId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * (0,"待渠道商审核"),(2,"待供应商审核"),(4,"待提交物流"),(6,"待确认收货"),(7,"待平台介入"),(8,"平台介入中"),(9,"退款中"),(10,"已完成"),(-2,"已拒绝"),(-4,"已关闭"),
     */
    private Integer refundState;

    private List<Integer> refundStateList;
    /**
     * 售后类型 (0 仅退款 1 退货退款)
     */
    private Integer refundType;
    /**
     * 创建开始时间
     */
    private Long createBeginTime;
    /**
     * 创建结束时间
     */
    private Long createEndTime;
    /**
     * 不可见来源订单状态
     */
    private List<Integer> fromOrderStateNot;
    /**
     * 订单类型 (0:渠道商订单 1:c端订单) 查询
     */
    private Integer orderType;
    /**
     * C端ID
     */
    private Long memberId;

    /**
     * C端ID集合
     */
    private List<Long> memberIdList;
    /**
     * 状态变化时间小于
     */
    private LocalDateTime stateTimeLess;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 商品名称
     */
    private String spuName;

}
