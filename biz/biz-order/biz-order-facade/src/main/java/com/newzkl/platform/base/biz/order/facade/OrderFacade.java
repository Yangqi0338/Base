package com.newzkl.platform.base.biz.order.facade;




import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.api.order.*;
import com.newzkl.platform.base.biz.order.facade.model.hdh.OrderCallbackRequest;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderPayInfoRes;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateRecordRPC;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderStateVO;

import java.util.List;

/**
 * @author niu
 * @description: 订单facade
 * @date 2023/4/28 10:54
 */
public interface OrderFacade {

     /**
      * API-订单创建
      * @param accountId
      * @param orderReq
      * @return
      */
     ApiOrderRes apiSubmitOrder(Long accountId, ApiOrderSubmitReq orderReq);
     /**
      * API-订单列表
      * @param accountId
      * @param spuOrderQuery
      * @return
      */
     Page<ApiOrderVO> apiList(Long accountId, ApiOrderReq spuOrderQuery);
     /**
      * API-订单详情
      * @param accountId
      * @param outOrderNo
      * @return
      */
     ApiOrderAggVO apiDetail(Long accountId, String outOrderNo);
     /**
      * API-订单确认收货
      * @param accountId
      * @param confirmReq
      */
     void apiConfirm(Long accountId, ApiOrderConfirmReq confirmReq);
     /**
      * API-订单运费
      * @param accountId
      * @param orderReq
      * @return
      */
     Long apiFreight(Long accountId, ApiOrderFreightReq orderReq);
     /**
      * API-订单状态
      * @param accountId
      * @param outOrderNoList
      * @return
      */
     List<SpuOrderStateVO> apiOrderState(Long accountId, List<String> outOrderNoList);
     /**
      * SPU订单关系
      *
      * @param orderId
      * @param spuId
      * @return
      */
     SpuOrderRelationVO spuOrderRelation(Long orderId, Long spuId);
     /**
      * C端支付成功
      * @param orderId
      */
     void memberPaySuccess(Long orderId);

     /**
      * 订单渠道商支付
      * @param orderId
      */
     void orderChannelPay(Long orderId);

    /**
     * 批量修改订单状态
     *
     * @param orderIdList
     */
    void orderMemberPay(List<Long> orderIdList);

    /**
     * 超时关闭订单
     */
    void closeOrder(Long orderId);

    /**
     * 保存订单状态记录
     * @param orderStateRecordRPC 订单状态记录RPC传输模型
     */
    void save(OrderStateRecordRPC orderStateRecordRPC);

    /**
     * 会订货订单状态回调处理
     *
     * <p>三方(会订货/HDH)推送订单状态变更, 经本方法完成: 外部订单号解析 → 三方状态码映射我方状态码
     * → 订单状态机校验转换 → (到货态)按外部SKU反查内部SKU执行发货。</p>
     *
     * <p>跨域边界: 订单业务属 biz-order 域, 故回调编排落本 facade。三方渠道插件(plugin-hdh)
     * 仅经本契约转发, 不直连订单 domain。</p>
     *
     * @param callbackRequest 回调请求参数 (含外部订单号 + 三方状态码 + 包裹列表)
     * @return 处理成功返回 {@code true}, 否则 {@code false}
     */
    boolean handleStatusCallback(OrderCallbackRequest callbackRequest);

    OrderPayInfoRes queryPayInfoByOrderId(Long orderId);
}
