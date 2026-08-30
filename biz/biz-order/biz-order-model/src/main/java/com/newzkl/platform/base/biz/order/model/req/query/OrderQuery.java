package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
* 订单
* @author fang
*/
@Data
public class OrderQuery extends BizPageQuery {
    /**
     * 渠道类型查询
     * @ext 0 API; 无对应枚举, 保留 Integer
     */
    private Integer channelType;
    /**
     * 订单类型查询
     * @ext 前端传数字 code, Jackson 经 OrderEnum.OrderType 的 @JsonValue 反序列化为枚举
     */
    private OrderEnum.OrderType orderType;
    /**
     * 商品类型查询
     * @ext 0 实物 1 课程 2 服务; 无对应枚举, 保留 Integer
     */
    private Integer goodsType;
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
     * 外部订单号集合
     */
    private List<String> outOrderNoList;

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNoList = doWrapperList(this.outOrderNoList, outOrderNo);
    }

    /**
     * 订单状态集合
     */
    private List<OrderEnum.State> orderStateList;

    public void setOrderState(OrderEnum.State orderState) {
        this.orderStateList = doWrapperList(this.orderStateList, orderState);;
    }

    /**
     * 创建时间早于该值
     * <p>
     * 单侧上界查询用本字段, 勿复用 {@code BizPageQuery.createStartTime}: 后者只 setStart 不 setEnd 时
     * createTime 数组长度为 1, {@code between} 会退化成 first==last 的秒级等值
     */
    private LocalDateTime lessCreateTime;
}
