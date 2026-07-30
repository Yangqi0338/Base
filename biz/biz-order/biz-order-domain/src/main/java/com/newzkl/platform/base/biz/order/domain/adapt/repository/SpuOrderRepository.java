package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.SpuOrderPageReq;
import com.newzkl.platform.base.biz.order.model.order.res.OrderStateCheckRes;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateCountVO;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;

import java.util.List;
import java.util.Map;

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
     * 按查询条件不分页查询 SPU 订单列表
     *
     * <p>对等旧 {@code SpuOrderDAO.listPkByQuery} 的用法 (导出明细时先取全部命中订单)。
     * 条件与 {@link SpuOrderRepository#pageQuery} 完全一致, 仅去掉分页。<b>无结果条数上限</b> (与旧行为一致),
     * 调用方须自行控制条件范围。</p>
     *
     * @param req 查询条件 (分页字段忽略)
     * @return 命中的 SPU 订单列表, 恒非 null
     */
    List<SpuOrder> listByQuery(SpuOrderPageReq req);

    /**
     * 按查询条件分组统计各订单状态的 SPU 订单数
     *
     * <p>对等旧 {@code IOrderRepository#stateCountMap} (旧实现 =
     * {@code SpuOrderDAO.countMapByQuery} + {@code addGroupField("t.order_state")})。
     * 只返回有数据的状态, 状态补零由调用方负责。</p>
     *
     * @param req 查询条件 (分页字段忽略)
     * @return 订单状态 → 订单数
     */
    Map<Integer, Integer> stateCountMap(SpuOrderPageReq req);

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

    /**
     * 按时间粒度分组统计 SPU 订单量与金额
     *
     * <p>迁移自旧 {@code SpuOrderDAO.orderCount} (daos/SpuOrderDAO.xml)。只返回有数据的时间桶,
     * 补齐连续桶由 {@code GroupCountUtils#complete} 负责。</p>
     *
     * @param timeQuery 时间分组查询
     * @return 分组统计列表 (按时间桶升序)
     */
    List<GroupCountRes> orderGroupCount(TimeQuery timeQuery);

    /**
     * 统计全量 SPU 订单数
     *
     * <p>迁移自旧 {@code SpuOrderDAO.countByQuery} 传空 {@code SpuOrderQuery} 的调用。</p>
     *
     * @return SPU 订单总数
     */
    Integer countAll();

    /**
     * 汇总全量 SPU 订单应付总额
     *
     * <p>迁移自旧 {@code SpuOrderDAO.sumAmountByQuery} 传空 {@code SpuOrderQuery} 的调用。
     * 旧库字段 {@code total_amount} 在中台拆分为多个金额字段, 取语义最接近的
     * {@code order_payable_amount} (订单应付总额)。</p>
     *
     * @return 订单金额合计 (单位: 分)
     */
    Integer sumOrderPayableAmountAll();
}