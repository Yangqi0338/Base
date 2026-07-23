package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.SpuOrderPageReq;
import com.newzkl.platform.base.biz.order.model.order.res.OrderStateCheckRes;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateCountVO;

import java.util.List;

/**
 * SPU订单仓储接口（DDD领域层）
 * 仅依赖领域模型，完全隔离DO/MyBatisPlus等技术细节
 * @author sijiwang
 */
public interface SpuOrderRepository {

    /**
     * 保存SPU订单
     * @param spuOrder 领域模型
     * @return 保存结果
     */
    boolean save(SpuOrder spuOrder);

    /**
     * 更新SPU订单
     * @param spuOrder 领域模型
     * @return 更新结果
     */
    boolean updateById(SpuOrder spuOrder);

    /**
     * 根据ID查询SPU订单
     * @param id 订单ID
     * @return 领域模型
     */
    SpuOrder getById(Long id);

    /**
     * 根据交易单号查询SPU订单
     * @param spuOrderNo 交易单号
     * @return 领域模型
     */
    SpuOrder getBySpuOrderNo(String spuOrderNo);

    /**
     * 根据交易单号查询SPU订单列表
     * @param spuOrderNo 交易单号
     * @return 领域模型列表
     */
    List<SpuOrder> listBySpuOrderNo(String spuOrderNo);

    /**
     * 根据交易单号查询SPU订单列表
     * @param orderNo 交易单号
     * @return 领域模型列表
     */
    List<SpuOrder> listByOrderNo(String orderNo);

    /**
     * 分页查询SPU订单
     *
     * @param req@return 分页结果
     */
    Page<SpuOrder> pageQuery(SpuOrderPageReq req);

    /**
     * 批量保存SPU订单
     * @param spuOrderList 领域模型列表
     * @return 保存结果
     */
    boolean batchSave(List<SpuOrder> spuOrderList);

    /**
     * 批量更新SPU订单
     * @param spuOrderList 领域模型列表
     * @return 更新结果
     */
    boolean batchUpdateSpu(List<SpuOrder> spuOrderList);

    /**
     * 根据订单号更新SPU订单状态
     * @param orderNo 订单号
     * @param sourceState 原状态
     * @param toState 目标状态
     * @param spuOrderExt 拓展字段
     * @return 影响行数
     */
    int updateSpuOrderStateByOrderNo(String orderNo, Integer sourceState, Integer toState, String spuOrderExt);

    /**
     * 统计指定渠道的订单状态数量
     * @param channelId 渠道ID
     * @return 订单状态统计列表
     */
    List<OrderStateCountVO> countOrderStateByChannel(Long channelId);

    /**
     * 统计指定账户的订单状态数量
     * @param accountId 账户ID
     * @return 订单状态统计列表
     */
    List<OrderStateCountVO> countOrderStateByAccount(Long accountId);

    /**
     * 批量更新订单状态
     * @param orderNos 订单号列表
     * @return 影响行数
     */
    List<OrderStateCheckRes> checkOrderState(List<String> orderNos);
}