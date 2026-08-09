package com.newzkl.platform.base.biz.user.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.pack.entity.PackOrder;
import com.newzkl.platform.base.biz.user.model.pack.query.PackOrderQuery;
import com.newzkl.platform.base.biz.user.model.pack.req.PackOrderCommand;
import com.newzkl.platform.base.biz.user.model.pack.req.PackOrderDeliverCommand;
import com.newzkl.platform.base.biz.user.model.pack.res.PackGoodsRes;
import com.newzkl.platform.base.biz.user.model.pack.res.PackOrderRes;
import com.newzkl.platform.base.biz.user.model.relation.res.ShipAddressRes;

import java.util.List;

/**
 * 入会礼包订单领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.packorder.service.IPackOrderDomain}。</p>
 *
 * @author KC
 */
public interface PackOrderDomain {

    /**
     * 组装礼包订单域实体（不落库）
     *
     * @param command       预创建入参
     * @param shipAddressVO 收货信息
     * @param packGoodsList 礼包明细
     * @return 组装后的域实体
     */
    PackOrder packOrderCreate(PackOrderCommand command, ShipAddressRes shipAddressVO, List<PackGoodsRes> packGoodsList);

    /**
     * 落库礼包订单
     *
     * @param packOrder 域实体
     */
    void packOrderCreate(PackOrder packOrder);

    /**
     * 按ID查询订单出参
     *
     * @param packOrderId 订单ID
     * @return 订单出参
     */
    PackOrderRes packOrderVO(Long packOrderId);

    /**
     * 分页查询订单
     *
     * @param query 分页查询
     * @return 订单分页
     */
    Page<PackOrderRes> packOrderVOList(PackOrderQuery query);

    /**
     * 发货
     *
     * @param command 发货入参
     */
    void packOrderDeliver(PackOrderDeliverCommand command);

    /**
     * 支付成功（待支付→待发货）
     *
     * @param orderId 订单ID
     */
    void paySuccess(Long orderId);

    /**
     * 支付超时关单（待支付→已关闭）
     *
     * @param idList 订单ID集合
     */
    void payTimeOut(List<Long> idList);

    /**
     * 按条件查询订单ID集合
     *
     * @param query 查询条件
     * @return 订单ID集合
     */
    List<Long> packOrderIdList(PackOrderQuery query);
}
