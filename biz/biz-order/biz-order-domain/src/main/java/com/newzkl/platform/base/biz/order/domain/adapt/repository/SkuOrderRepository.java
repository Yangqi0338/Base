package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.res.OrderStateCheckRes;

import java.util.List;

/**
 * SKU订单仓储接口（DDD领域层）
 * 仅依赖领域模型，完全隔离DO/MyBatisPlus等技术细节
 * @author sijiwang
 */
public interface SkuOrderRepository {

    /**
     * 保存SKU订单
     * @param skuOrder 领域模型
     * @return 保存结果
     */
    boolean save(SkuOrder skuOrder);

    /**
     * 更新SKU订单
     * @param skuOrder 领域模型
     * @return 更新结果
     */
    boolean updateById(SkuOrder skuOrder);

    /**
     * 根据ID查询SKU订单
     * @param id 订单ID
     * @return 领域模型
     */
    SkuOrder getById(Long id);

    /**
     * 根据交易单号查询SKU订单列表
     *
     * @param spuOrderNos 交易单号
     * @return 领域模型列表
     */
    List<SkuOrder> listBySpuOrderNo(List<String> spuOrderNos);

    /**
     * 根据交易单号查询SKU订单列表
     *
     * @param spuOrderNo 交易单号
     * @return 领域模型列表
     */
    List<SkuOrder> getBySpuOrderNo(String spuOrderNo);

    /**
     * 根据交易单号查询SKU订单列表
     *
     * @param orderNo 订单号
     * @return 领域模型列表
     */
    List<SkuOrder> listByOrderNo(String orderNo);

    /**
     * 分页查询SKU订单
     * @param page 分页参数
     * @param skuOrder 查询条件
     * @return 分页结果
     */
    Page<SkuOrder> pageQuery(Page<SkuOrder> page, SkuOrder skuOrder);

    /**
     * 批量保存SKU订单
     * @param skuOrderList 领域模型列表
     * @return 保存结果
     */
    boolean batchSaveSku(List<SkuOrder> skuOrderList);

    /**
     * 批量更新SKU订单
     * @param skuOrderList 领域模型列表
     * @return 更新结果
     */
    boolean batchUpdateSku(List<SkuOrder> skuOrderList);

    /**
     * 根据订单号更新SKU订单状态
     *
     * @param orderNo     订单号
     * @param sourceState 原状态
     * @param toState     目标状态
     * @param closeReason
     * @return 影响行数
     */
    int updateSkuOrderStateByOrderNo(String orderNo, Integer sourceState, Integer toState, String closeReason);

    /**
     * 根据SPU订单号和SKU订单号查询订单ID
     *
     * @param spuOrderNos SPU订单号
     * @param skuOrderNos SKU订单号
     * @return 订单ID列表
     */
    List<String> orderIdBySpuSkuOrderId(List<String> spuOrderNos, List<String> skuOrderNos);

    /**
     * 检查SPU订单状态
     *
     * @param orderNos SPU订单号
     * @return 订单状态列表
     */
    List<OrderStateCheckRes> checkSpuOrderState(List<String> orderNos);
}