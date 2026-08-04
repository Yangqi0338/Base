package com.newzkl.platform.base.biz.course.facade;

/**
 * 课程购买对外契约 (inbound provider)
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.rpc.facade.ICoursePurchaseRecordFacade}。
 * 课程购买按业务概念归属 biz-course(slug10 概念订正: 服务订单/虚拟商品非业务概念, 按概念散归各 biz)。
 * 消费方 = biz-finance 支付回调(支付成功回写订单状态 + 递增实际购买数), 由入口 starter 侧远程 consumer 接线。</p>
 *
 * <p>入参为原始类型, 无需自带 facade model。</p>
 *
 * @author KC
 */
public interface CoursePurchaseFacade {

    /**
     * 课程订单支付成功回调
     *
     * @param orderNo 订单编号
     * @param payNo   第三方支付流水号
     */
    void paySuccess(Long orderNo, String payNo);
}
