package com.newzkl.platform.base.biz.order.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
* 订单
* @author fang
*/
@Data
public class OrderQuery extends PageQuery {

    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
    * 渠道类型 (0:API) 查询
    */
    private Integer channelType;
    /**
    * 订单类型 (0:渠道商订单 1:c端订单) 查询
    */
    private Integer orderType;
    /**
    * 商品类型 (0:实物 1:课程 2:服务) 查询
    */
    private Integer goodsType;
    /**
     * 运营商ID
     */
    private Long operatorId;
    /**
    * 渠道商ID 查询
    */
    private Long channelId;
    /**
     * 商户ID
     */
    private Long merchantId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 昵称
     */
    private Long nickname;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 订单状态
     */
    private OrderEnum.State orderState;
    /**
     * 创建时间小于的时间
     */
    private LocalDateTime lessCreateTime;
    /**
     * 外部订单号
     */
    private String outOrderNo;
    /**
     * 外部订单号集合
     */
    private List<String> outOrderNoList;
    /**
     * 创建开始时间
     */
    private Long createBeginTime;
    /**
     * 创建结束时间
     */
    private Long createEndTime;
    /**
     * 订单状态集合
     */
    private List<Integer> orderStateList;
}
