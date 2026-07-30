package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SpuOrderRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.SpuOrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SpuOrderDO;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.SpuOrderPageReq;
import com.newzkl.platform.base.biz.order.model.order.res.OrderStateCheckRes;
import com.newzkl.platform.base.biz.order.model.order.util.GroupCountUtils;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateCountVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SPU订单仓储实现
 * @author sijiwang
 */
@Repository
public class SpuOrderRepositoryImpl extends ServiceImpl<SpuOrderDAO, SpuOrderDO> implements SpuOrderRepository {

    @Override
    public boolean save(SpuOrder spuOrder) {
        Assert.notNull(spuOrder, "保存SPU订单失败：订单对象不能为空");
        SpuOrderDO doObj = TransferUtils.transfer(spuOrder, SpuOrderDO::new);
        doObj.preInsert();
        return saveOrUpdate(doObj);
    }

    @Override
    public boolean updateById(SpuOrder spuOrder) {
        Assert.notNull(spuOrder, "更新SPU订单失败：订单对象不能为空");
        Assert.notNull(spuOrder.getId(), "更新SPU订单失败：订单ID不能为空");
        SpuOrderDO doObj = TransferUtils.transfer(spuOrder, SpuOrderDO::new);
        doObj.preUpdate();
        return updateById(doObj);
    }

    @Override
    public SpuOrder getById(Long id) {
        return Optional.ofNullable(id)
                .map(baseMapper::selectById)
                .map(doObj -> TransferUtils.transfer(doObj, SpuOrder::new))
                .orElse(null);
    }

    @Override
    public SpuOrder getBySpuOrderNo(String spuOrderNo) {
        SpuOrderDO spuOrderDO = baseMapper.selectOne(new LambdaQueryWrapper<SpuOrderDO>()
                .eq(SpuOrderDO::getSpuOrderNo, spuOrderNo));
        return spuOrderDO != null ? TransferUtils.transfer(spuOrderDO, SpuOrder::new) : null;
    }

    @Override
    public List<SpuOrder> listBySpuOrderNo(String spuOrderNo) {
        return Optional.ofNullable(spuOrderNo)
                .filter(Objects::nonNull)
                .filter(str -> !StrUtil.isBlank(str))
                .map(no -> {
                    LambdaQueryWrapper<SpuOrderDO> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(SpuOrderDO::getSpuOrderNo, no)
                            .orderByDesc(SpuOrderDO::getId);
                    return baseMapper.selectList(wrapper).stream()
                            .filter(Objects::nonNull)
                            .map(doObj -> TransferUtils.transfer(doObj, SpuOrder::new))
                            .collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public List<SpuOrder> listByOrderNo(String orderNo) {
        return Optional.ofNullable(orderNo)
                .filter(Objects::nonNull)
                .filter(str -> !StrUtil.isBlank(str))
                .map(no -> {
                    LambdaQueryWrapper<SpuOrderDO> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(SpuOrderDO::getOrderNo, no)
                            .orderByDesc(SpuOrderDO::getId);
                    return baseMapper.selectList(wrapper).stream()
                            .filter(Objects::nonNull)
                            .map(doObj -> TransferUtils.transfer(doObj, SpuOrder::new))
                            .collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public Page<SpuOrder> pageQuery(SpuOrderPageReq req) {
        Assert.notNull(req, "分页查询SPU订单失败：分页对象不能为空");

        // 创建分页对象
        Page<SpuOrderDO> doPage = new Page<>(req.getCurrent(), req.getSize());

        // 构建查询条件
        LambdaQueryWrapper<SpuOrderDO> wrapper = buildQueryWrapper(req);

        // 执行查询
        Page<SpuOrderDO> resultPage = baseMapper.selectPage(doPage, wrapper);

        // 转换返回结果
        return TransferUtils.transferPage(resultPage, SpuOrder::new);
    }

    @Override
    public List<SpuOrder> listByQuery(SpuOrderPageReq req) {
        Assert.notNull(req, "查询SPU订单失败：查询条件不能为空");
        List<SpuOrderDO> doList = baseMapper.selectList(buildQueryWrapper(req));
        return TransferUtils.transfers(doList, SpuOrder::new);
    }

    @Override
    public Map<Integer, Integer> stateCountMap(SpuOrderPageReq req) {
        Assert.notNull(req, "统计SPU订单状态失败：查询条件不能为空");
        // 自定义 SQL 不享受 MyBatis-Plus 逻辑删除注入, 此处显式补 del_flag = 0; 且不能带 order by
        LambdaQueryWrapper<SpuOrderDO> wrapper = new LambdaQueryWrapper<>();
        appendConditions(wrapper, req);
        wrapper.eq(SpuOrderDO::getDelFlag, 0);
        List<Map<String, Object>> rows = baseMapper.stateCountMap(wrapper);
        Map<Integer, Integer> countMap = new HashMap<>(rows.size());
        for (Map<String, Object> row : rows) {
            Integer state = toInteger(row.get("orderState"));
            countMap.put(state, toInteger(row.get("orderCount")));
        }
        return countMap;
    }

    /**
     * 构建查询条件包装器
     *
     * @param req 查询请求参数
     * @return LambdaQueryWrapper
     */
    private LambdaQueryWrapper<SpuOrderDO> buildQueryWrapper(SpuOrderPageReq req) {
        LambdaQueryWrapper<SpuOrderDO> wrapper = new LambdaQueryWrapper<>();
        appendConditions(wrapper, req);
        // 按创建时间降序排列
        wrapper.orderByDesc(SpuOrderDO::getId);
        return wrapper;
    }

    /**
     * 追加 SPU 订单查询条件 (不含排序)
     *
     * <p>从 {@link SpuOrderRepositoryImpl#buildQueryWrapper} 抽出, 供 {@link SpuOrderRepositoryImpl#stateCountMap} 复用同一套条件 ——
     * 分组统计的 SQL 不能带 {@code order by}, 故排序留在 {@link SpuOrderRepositoryImpl#buildQueryWrapper} 内。</p>
     *
     * @param wrapper 条件包装器, 原地追加
     * @param req     查询请求参数
     */
    private void appendConditions(LambdaQueryWrapper<SpuOrderDO> wrapper, SpuOrderPageReq req) {

        // 精确匹配查询条件
        Optional.ofNullable(req.getSpuOrderNo())
                .filter(StringUtils::isNotBlank)
                .ifPresent(spuOrderNo -> wrapper.eq(SpuOrderDO::getSpuOrderNo, spuOrderNo));

        Optional.ofNullable(req.getOrderNo())
                .filter(StringUtils::isNotBlank)
                .ifPresent(orderNo -> wrapper.eq(SpuOrderDO::getOrderNo, orderNo));

        Optional.ofNullable(req.getOutOrderNo())
                .filter(StringUtils::isNotBlank)
                .ifPresent(outOrderNo -> wrapper.eq(SpuOrderDO::getOutOrderNo, outOrderNo));

        Optional.ofNullable(req.getSourceType())
                .ifPresent(sourceType -> wrapper.eq(SpuOrderDO::getSourceType, sourceType));

        Optional.ofNullable(req.getOrderState())
                .ifPresent(orderState -> wrapper.eq(SpuOrderDO::getOrderState, orderState));

        Optional.ofNullable(req.getOrderType())
                .ifPresent(orderType -> wrapper.eq(SpuOrderDO::getOrderType, orderType));

        Optional.ofNullable(req.getChannelId())
                .ifPresent(channelId -> wrapper.eq(SpuOrderDO::getChannelId, channelId));

        Optional.ofNullable(req.getSupplierId())
                .ifPresent(supplierId -> wrapper.eq(SpuOrderDO::getSupplierId, supplierId));

        Optional.ofNullable(req.getStoreId())
                .ifPresent(storeId -> wrapper.eq(SpuOrderDO::getStoreId, storeId));

        // ID集合查询
        Optional.ofNullable(req.getSpuOrderNoList())
                .filter(list -> !list.isEmpty())
                .ifPresent(spuOrderNoList -> wrapper.in(SpuOrderDO::getSpuOrderNo, spuOrderNoList));

        Optional.ofNullable(req.getMemberIdList())
                .filter(list -> !list.isEmpty())
                .ifPresent(memberIdList -> wrapper.in(SpuOrderDO::getUserId, memberIdList));

        Optional.ofNullable(req.getChannelIdList())
                .filter(list -> !list.isEmpty())
                .ifPresent(channelIdList -> wrapper.in(SpuOrderDO::getChannelId, channelIdList));

        Optional.ofNullable(req.getSupplierIdList())
                .filter(list -> !list.isEmpty())
                .ifPresent(supplierIdList -> wrapper.in(SpuOrderDO::getSupplierId, supplierIdList));

        Optional.ofNullable(req.getOrderStateList())
                .filter(list -> !list.isEmpty())
                .ifPresent(orderStateList -> wrapper.in(SpuOrderDO::getOrderState, orderStateList));

        // 时间范围查询
        Optional.ofNullable(req.getCreateBeginTime())
                .filter(StringUtils::isNotBlank)
                .ifPresent(createBeginTime -> wrapper.ge(SpuOrderDO::getCreateTime, createBeginTime));

        Optional.ofNullable(req.getCreateEndTime())
                .filter(StringUtils::isNotBlank)
                .ifPresent(createEndTime -> wrapper.le(SpuOrderDO::getCreateTime, createEndTime));

        Optional.ofNullable(req.getPayBeginTime())
                .filter(StringUtils::isNotBlank)
                .ifPresent(payBeginTime -> wrapper.ge(SpuOrderDO::getPayTime, payBeginTime));

        Optional.ofNullable(req.getPayEndTime())
                .filter(StringUtils::isNotBlank)
                .ifPresent(payEndTime -> wrapper.le(SpuOrderDO::getPayTime, payEndTime));

        // 金额范围查询
        Optional.ofNullable(req.getMemberAmountMin())
                .ifPresent(min -> wrapper.ge(SpuOrderDO::getUserPayAmount, min * 100L));

        Optional.ofNullable(req.getMemberAmountMax())
                .ifPresent(max -> wrapper.le(SpuOrderDO::getUserPayAmount, max * 100L));

        // 退款状态查询
        Optional.ofNullable(req.getRefund())
                .ifPresent(refund -> {
                    if (refund == 0) {
                        // 未退款：isRefunding = 0 或者为null
                        wrapper.and(wrapper1 -> wrapper1.eq(SpuOrderDO::getIsRefunding, 0).or().isNull(SpuOrderDO::getIsRefunding));
                    } else {
                        // 已退款：isRefunding = 1
                        wrapper.eq(SpuOrderDO::getIsRefunding, 1);
                    }
                });
    }

    @Override
    public boolean batchSave(List<SpuOrder> spuOrderList) {
        if (spuOrderList == null || spuOrderList.isEmpty()) {
            return false;
        }
        List<SpuOrderDO> doList = spuOrderList.stream()
                .filter(Objects::nonNull)
                .map(source -> TransferUtils.transfer(source, SpuOrderDO::new))
                .peek(SpuOrderDO::preInsert)
                .collect(Collectors.toList());
        return saveBatch(doList);
    }

    @Override
    public boolean batchUpdateSpu(List<SpuOrder> spuOrderList) {
        if (spuOrderList == null || spuOrderList.isEmpty()) {
            return false;
        }
        List<SpuOrderDO> transfers = TransferUtils.transfers(spuOrderList, SpuOrderDO::new);
        return this.saveOrUpdateBatch(transfers);
    }

    @Override
    public int updateSpuOrderStateByOrderNo(String orderNo, Integer sourceState, Integer toState, String spuOrderExt) {
        return baseMapper.updateSpuOrderStateByOrderNo(orderNo, sourceState, toState, spuOrderExt);
    }

    @Override
    public List<OrderStateCountVO> countOrderStateByChannel(Long channelId) {
        return baseMapper.countOrderStateByChannel(channelId);
    }

    @Override
    public List<OrderStateCountVO> countOrderStateByAccount(Long accountId) {
        return baseMapper.countOrderStateByAccount(accountId);
    }

    @Override
    public List<OrderStateCheckRes> checkOrderState(List<String> orderNos) {
        return baseMapper.checkOrderState(orderNos);
    }

    @Override
    public List<GroupCountRes> orderGroupCount(TimeQuery timeQuery) {
        if (timeQuery == null || timeQuery.getCreateBeginTime() == null || timeQuery.getCreateEndTime() == null) {
            return Collections.emptyList();
        }
        // 旧 SQL 小时分组用 '%h' (12 小时制), 与 Java 侧 "yyyy-MM-dd-HH" 桶键对不上导致分组恒为 0;
        // 此处统一用 '%H' (24 小时制) 修正, 分组键格式与 GroupCountUtils 保持一致
        String dateFormat = GroupCountUtils.isGroupByHour(timeQuery) ? "%Y-%m-%d-%H" : "%Y-%m-%d";
        QueryWrapper<SpuOrderDO> wrapper = new QueryWrapper<>();
        wrapper.select("DATE_FORMAT(create_time, '" + dateFormat + "') AS transDay",
                        "COUNT(*) AS transNum",
                        "IFNULL(SUM(order_payable_amount), 0) AS transAmount")
                .ge("UNIX_TIMESTAMP(create_time) * 1000", timeQuery.getCreateBeginTime())
                .le("UNIX_TIMESTAMP(create_time) * 1000", timeQuery.getCreateEndTime())
                .groupBy("transDay")
                .orderByAsc("transDay");
        List<Map<String, Object>> rows = baseMapper.selectMaps(wrapper);
        List<GroupCountRes> resultList = new ArrayList<>(rows.size());
        for (Map<String, Object> row : rows) {
            GroupCountRes group = new GroupCountRes();
            group.setTransDay(Objects.toString(row.get("transDay"), null));
            group.setTransNum(toInteger(row.get("transNum")));
            group.setTransAmount(toInteger(row.get("transAmount")));
            resultList.add(group);
        }
        return resultList;
    }

    @Override
    public Integer countAll() {
        Long count = baseMapper.selectCount(new QueryWrapper<>());
        return count == null ? 0 : count.intValue();
    }

    @Override
    public Integer sumOrderPayableAmountAll() {
        QueryWrapper<SpuOrderDO> wrapper = new QueryWrapper<>();
        wrapper.select("IFNULL(SUM(order_payable_amount), 0) AS totalAmount");
        List<Map<String, Object>> rows = baseMapper.selectMaps(wrapper);
        if (rows.isEmpty()) {
            return 0;
        }
        return toInteger(rows.get(0).get("totalAmount"));
    }

    /**
     * 聚合列取值转 Integer, 空值归 0
     *
     * @param value 聚合列原始值 (JDBC 可能返回 Long / BigDecimal)
     * @return 整数值, null 归 0
     */
    private Integer toInteger(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.valueOf(value.toString());
    }
}