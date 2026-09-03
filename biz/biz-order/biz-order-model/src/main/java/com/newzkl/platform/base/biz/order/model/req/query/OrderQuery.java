package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
* 订单(SpuOrder 层折叠后, 合并原 SpuOrderQuery 字段, 语义 order 级)
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
     * 渠道商ID集合 查询
     */
    private List<Long> channelIdList;
    /**
     * 商户ID
     */
    private Long merchantId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员ID集合
     */
    private List<Long> memberIdList;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 外部订单号集合
     */
    private List<String> outOrderNoList;

    /**
     * 追加外部订单号
     *
     * @param outOrderNo 外部订单号
     */
    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNoList = doWrapperList(this.outOrderNoList, outOrderNo);
    }

    /**
     * 订单状态集合
     */
    private List<OrderEnum.State> orderStateList;

    /**
     * 追加订单状态
     *
     * @param orderState 订单状态
     */
    public void setOrderState(OrderEnum.State orderState) {
        this.orderStateList = doWrapperList(this.orderStateList, orderState);
    }

    // ===== SpuOrder 层折叠合并字段(语义 order 级) =====
    /**
     * SKU_ID集合
     */
    private List<Long> skuIdList;

    /**
     * 追加 SKU_ID
     *
     * @param skuId SKU ID
     */
    public void setSkuId(Long skuId) {
        this.skuIdList = doWrapperList(this.skuIdList, skuId);
    }

    /**
     * 供应商ID集合
     */
    private List<Long> supplierIdList;

    /**
     * 追加供应商ID
     *
     * @param supplierId 供应商ID
     */
    public void setSupplierId(Long supplierId) {
        this.supplierIdList = doWrapperList(this.supplierIdList, supplierId);
    }

    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 交易师ID
     */
    private Long dealerId;
    /**
     * 订单ID集合
     */
    private List<Long> orderIdList;

    /**
     * 追加订单ID
     *
     * @param orderId 订单ID
     */
    public void setOrderId(Long orderId) {
        this.orderIdList = doWrapperList(this.orderIdList, orderId);
    }

    /**
     * 交易单号集合
     */
    private List<String> orderNoList;

    /**
     * 追加交易单号
     *
     * @param orderNo 交易单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNoList = doWrapperList(this.orderNoList, orderNo);
    }

    /**
     * SPU名称
     */
    private String spuName;
    /**
     * SPU id 集合
     */
    private List<Long> spuIdList;

    /**
     * 追加 SPU id
     *
     * @param spuId SPU ID
     */
    public void setSpuId(Long spuId) {
        this.spuIdList = doWrapperList(this.spuIdList, spuId);
    }

    /**
     * 渠道商名称
     */
    private String channelName;
    /**
     * 渠道类型
     * @ext 前端传数字 code, Jackson 经 SpuEnum.ChannelType 的 @JsonValue 反序列化为枚举
     */
    private SpuEnum.ChannelType spuChannelType;
    /**
     * 收货人手机号
     */
    private String shipPhone;
    /**
     * 运营类型
     * @ext 0 机构 1 行业 2 区域; 无对应枚举, 保留 Integer
     */
    private Integer type;
    /**
     * 订单价格区间
     * @ext 二元数组 [最小值, 最大值], 单位分
     */
    private Integer[] memberAmount;
    /**
     * 购买方式
     * @ext 0 激活, 1 自购; 无对应枚举, 保留 String
     */
    private String buyMode;
    /**
     * 支付时间区间
     * @ext 二元数组 [开始, 结束]
     */
    private String[] payTime;
    /**
     * 退款状态
     * @ext 0 未退款 1 退款; 无对应枚举, 保留 Integer
     */
    private Integer refund;
    /**
     * 门店账号
     */
    private String storeAccount;
    /**
     * 结算发送状态
     */
    private CommonEnum.YesOrNo settleSendStatus;

    /**
     * 创建时间早于该值
     * <p>
     * 单侧上界查询用本字段, 勿复用 {@code BizPageQuery.createStartTime}: 后者只 setStart 不 setEnd 时
     * createTime 数组长度为 1, {@code between} 会退化成 first==last 的秒级等值
     */
    private LocalDateTime lessCreateTime;
}
