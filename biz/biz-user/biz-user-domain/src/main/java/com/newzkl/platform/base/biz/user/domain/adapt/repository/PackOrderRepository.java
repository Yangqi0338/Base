package com.newzkl.platform.base.biz.user.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.pack.entity.PackOrder;
import com.newzkl.platform.base.biz.user.model.pack.query.PackOrderQuery;
import com.newzkl.platform.base.biz.user.model.pack.res.PackOrderRes;

import java.util.List;

/**
 * 入会礼包订单仓储端口
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.packorder.repository.IPackOrderRepository}。</p>
 *
 * @author KC
 */
public interface PackOrderRepository {

    /**
     * 保存礼包订单（id 为空新增，否则更新）
     *
     * @param packOrder 域内订单实体
     * @return 订单ID
     */
    Long packOrderSave(PackOrder packOrder);

    /**
     * 新增礼包订单
     *
     * @param packOrder 域内订单实体
     * @return 订单ID
     */
    Long packOrderCreate(PackOrder packOrder);

    /**
     * 按ID查询域内订单实体
     *
     * @param packOrderId 订单ID
     * @return 域内订单实体，不存在返回 null
     */
    PackOrder packOrder(Long packOrderId);

    /**
     * 按ID查询订单出参
     *
     * @param packOrderId 订单ID
     * @return 订单出参，不存在返回 null
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
     * 乐观状态跃迁
     *
     * @param idList    订单ID集合
     * @param fromState 期望的当前状态
     * @param toState   目标状态
     * @return 更新条数
     */
    int updateState(List<Long> idList, Integer fromState, Integer toState);

    /**
     * 按条件查询订单ID集合
     *
     * @param query 查询条件
     * @return 订单ID集合
     */
    List<Long> packOrderIdList(PackOrderQuery query);
}
