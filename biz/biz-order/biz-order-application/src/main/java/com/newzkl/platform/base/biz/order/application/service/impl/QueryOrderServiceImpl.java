package com.newzkl.platform.base.biz.order.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.application.service.QueryOrderService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.OperatorApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SkuOrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SpuOrderRepository;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.CommitOrderPreReq;
import com.newzkl.platform.base.biz.order.model.order.req.SpuOrderPageReq;
import com.newzkl.platform.base.biz.order.model.order.res.CreateOrderRes;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateCountVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SkuOrderVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderItemExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderVO;
import com.newzkl.platform.base.biz.order.model.support.api.DistributionDetailVO;
import com.newzkl.platform.base.biz.order.model.support.api.ReceiveAddressOutVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 订单查询应用服务实现 (查询编排)
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.application.service.impl.QueryOrderServiceImpl} /
 * {@code OrderServiceImpl} 的查询与支付入口。查询类方法按旧语义逐条对齐;
 * 支付类方法在中台缺失前置能力, 显式抛出 {@code UnsupportedOperationException} 而非静默返回,
 * 保证 Spring 容器可装配、调用方能立刻暴露缺口。</p>
 *
 * <p><b>domain-gap 清单 (未实现方法 → 缺失能力):</b></p>
 * <ul>
 *   <li>{@link QueryOrderServiceImpl#selectOrderPre} — 缺订单主聚合 {@code OrderAgg} 与预订单编排领域服务
 *       (旧 {@code ICreateOrderDomain#commitOrderPre}), 中台 biz-order 无 {@code OrderDomain}。</li>
 *   <li>{@link QueryOrderServiceImpl#orderBalancePay} — 缺订单主聚合 {@code OrderAgg} 与余额支付端口
 *       (旧 {@code balancePayApi.balancePay} + {@code orderAgg.allPaySuccess}), 中台无支付 adapt-api port。</li>
 *   <li>{@link QueryOrderServiceImpl#operatorOrderBalancePay} — 同上, 另缺运营商代付分支所需的运营商钱包端口。</li>
 *   <li>{@link QueryOrderServiceImpl#orderDirectPay} — 缺订单主聚合与 C 端支付成功回写编排
 *       (旧 {@code doMemberPaySuccess}), 依赖本地消息表 {@code localMessageFacade}, 中台未接入。</li>
 * </ul>
 *
 * <p>偏离说明: 中台 biz-order 尚无订单查询领域服务 (仅有 {@code OrderStateRecordDomain} /
 * {@code OrderDeliveryDomain} / {@code SettleDomain}), 故只读方法直接编排本域仓储端口
 * {@code SpuOrderRepository} / {@code SkuOrderRepository}。仓储端口属领域层契约,
 * 不涉及 DO/DAO, 不破坏分层红线。</p>
 *
 * <p><b>导出与状态分组的快照字段口径</b>: 旧库 {@code spu_order.spu_name} /
 * {@code sku_order.sku_sale_attribute} / {@code sku_order.sku_supplier_price} 三列在 Base
 * 已并入快照 JSON ({@code spu_order.goods_snapshot} / {@code sku_order.sku_snapshot},
 * 结构对等 {@code DistributionDetailVO}, 与 {@code Refund#init} 的解析口径一致);
 * 供货单价由 {@code channelPurchaseAmount ÷ buyNum} 还原。<b>Base 尚无下单编排,
 * 两个快照列目前无写入方</b>, 故导出的商品名/规格值/单价在真实下单链路补齐前会取不到值,
 * 但订单号、金额、状态、收货信息等直存列不受影响。</p>
 *
 * @author KC
 * @since 2026-07-28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QueryOrderServiceImpl implements QueryOrderService {

    /**
     * 金额换算基数: 库内单位「分」→ 导出单位「元」
     */
    private static final BigDecimal AMOUNT_UNIT = new BigDecimal(100);

    private final SpuOrderRepository spuOrderRepository;

    private final SkuOrderRepository skuOrderRepository;

    private final OperatorApi operatorApi;

    /**
     * 查询预订单 (未实现)
     *
     * @param req 预订单查询请求
     * @return 预订单结果
     * @throws UnsupportedOperationException 中台缺订单主聚合与预订单编排领域服务
     */
    @Override
    public CreateOrderRes selectOrderPre(CommitOrderPreReq req) {
        throw new UnsupportedOperationException(
                "TODO[domain-gap]: 缺订单主聚合 OrderAgg 与预订单编排领域服务 (旧 ICreateOrderDomain#commitOrderPre)");
    }

    /**
     * 分页查询 SPU 订单列表
     *
     * @param req 分页查询条件
     * @return SPU 订单视图分页结果
     */
    @Override
    public Page<SpuOrderVO> spuPage(SpuOrderPageReq req) {
        Page<SpuOrder> page = spuOrderRepository.pageQuery(req);
        return TransferUtils.transferPage(page, SpuOrderVO::new);
    }

    /**
     * 查询 SPU 订单详情 (含 SKU 子订单列表)
     *
     * @param spuOrderNo SPU 交易单号
     * @return SPU 订单详情视图
     * @throws PlatformException 交易单号不存在时抛出
     */
    @Override
    public SpuOrderVO getSpuOrderDetail(String spuOrderNo) {
        SpuOrder spuOrder = spuOrderRepository.getBySpuOrderNo(spuOrderNo);
        if (spuOrder == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "SPU订单");
        }
        SpuOrderVO vo = TransferUtils.transfer(spuOrder, SpuOrderVO::new);
        List<SkuOrder> skuOrders = skuOrderRepository.getBySpuOrderNo(spuOrderNo);
        vo.setSkuOrderList(TransferUtils.transfers(skuOrders, SkuOrderVO::new));
        return vo;
    }

    /**
     * 统计指定渠道商的各状态订单数量
     *
     * @param channelId 渠道商 ID
     * @return 订单状态统计列表
     */
    @Override
    public List<OrderStateCountVO> countOrderStateByChannel(Long channelId) {
        return spuOrderRepository.countOrderStateByChannel(channelId);
    }

    /**
     * 统计指定账号的各状态订单数量
     *
     * @param accountId 账号 ID
     * @return 订单状态统计列表
     */
    @Override
    public List<OrderStateCountVO> countOrderStateByAccount(Long accountId) {
        return spuOrderRepository.countOrderStateByAccount(accountId);
    }

    /**
     * 渠道商交易单余额支付 (未实现)
     *
     * @param orderNos 交易单号列表
     * @throws UnsupportedOperationException 中台缺订单主聚合与余额支付端口
     */
    @Override
    public void orderBalancePay(String... orderNos) {
        throw new UnsupportedOperationException(
                "TODO[domain-gap]: 缺订单主聚合 OrderAgg 与余额支付端口 (旧 balancePayApi.balancePay + orderAgg.allPaySuccess)");
    }

    /**
     * 运营商交易单余额支付 (未实现)
     *
     * @param orderNo 交易单号
     * @throws UnsupportedOperationException 中台缺订单主聚合与运营商钱包支付端口
     */
    @Override
    public void operatorOrderBalancePay(String orderNo) {
        throw new UnsupportedOperationException(
                "TODO[domain-gap]: 缺订单主聚合 OrderAgg 与运营商钱包支付端口 (旧 balancePayApi 运营商代付分支)");
    }

    /**
     * 交易单直接支付 (未实现)
     *
     * @param orderNo 交易单号
     * @throws UnsupportedOperationException 中台缺订单主聚合与支付成功回写编排
     */
    @Override
    public void orderDirectPay(String orderNo) {
        throw new UnsupportedOperationException(
                "TODO[domain-gap]: 缺订单主聚合 OrderAgg 与 C 端支付成功回写编排 (旧 doMemberPaySuccess + localMessageFacade)");
    }

    /**
     * 查询 SPU 订单领域模型
     *
     * @param spuOrderNo SPU 交易单号
     * @return SPU 订单领域模型, 不存在返回 null
     */
    @Override
    public SpuOrder selectSpuOrder(String spuOrderNo) {
        return spuOrderRepository.getBySpuOrderNo(spuOrderNo);
    }

    /**
     * 按 SPU 交易单号查询 SKU 子订单列表
     *
     * @param spuOrderNo SPU 交易单号
     * @return SKU 订单领域模型列表
     */
    @Override
    public List<SkuOrder> getBySpuOrderNo(String spuOrderNo) {
        return skuOrderRepository.getBySpuOrderNo(spuOrderNo);
    }

    /**
     * 运营商视角统计各订单状态的 SPU 订单数
     *
     * <p><b>⚠️ 逐字沿用旧 {@code spuOrderStateCountMap} 的供应商范围口径 —— 并集而非交集</b>:
     * 旧实现把「入参指定的 supplierId / supplierIdList」与「运营商可见供应商列表」<b>相加</b>,
     * 而不是求交集; 结果是运营商显式传一个不属于自己的 supplierId 时, 该供应商的订单数也会被计入。
     * 与同类的 {@code spuOrderPage} (交集口径) 不一致。此处保持旧行为不动以免改变线上数据,
     * <b>是否收紧为交集需人工确认</b>。</p>
     *
     * @param req               查询条件, 调用方已预置可见状态集合
     * @param operatorAccountId 当前登录运营商账号 ID
     * @return 订单状态 → 订单数, 入参状态集合内无数据的状态补 0
     */
    @Override
    public Map<Integer, Integer> spuOrderStateCountMap(SpuOrderPageReq req, Long operatorAccountId) {
        List<Integer> stateList = CollUtil.isEmpty(req.getOrderStateList())
                ? allStateCodes()
                : new ArrayList<>(req.getOrderStateList());
        if (req.getOrderState() != null && !stateList.contains(req.getOrderState())) {
            stateList.add(req.getOrderState());
        }
        req.setOrderState(null);
        req.setOrderStateList(stateList);

        // 旧口径: 入参指定供应商 ∪ 运营商可见供应商 (见方法 javadoc 的并集说明)
        Set<Long> searchSupplierIdSet = new LinkedHashSet<>();
        if (req.getSupplierIdList() != null) {
            searchSupplierIdSet.addAll(req.getSupplierIdList());
        }
        if (req.getSupplierId() != null) {
            searchSupplierIdSet.add(req.getSupplierId());
        }
        searchSupplierIdSet.addAll(operatorApi.supplierIdListByType(operatorAccountId, req.getType()));

        Map<Integer, Integer> countMap = new HashMap<>();
        if (!searchSupplierIdSet.isEmpty()) {
            req.setSupplierIdList(new ArrayList<>(searchSupplierIdSet));
            countMap.putAll(spuOrderRepository.stateCountMap(req));
        }
        stateList.forEach(state -> countMap.putIfAbsent(state, 0));
        return countMap;
    }

    /**
     * 装配 SPU 订单导出行
     *
     * @param req 查询条件, 调用方已按登录角色收敛可见范围
     * @return 导出行列表, 恒非 null
     */
    @Override
    public List<SpuOrderExcelVO> exportSpuOrder(SpuOrderPageReq req) {
        Page<SpuOrder> page = spuOrderRepository.pageQuery(req);
        List<SpuOrder> spuOrderList = page.getRecords();
        if (CollUtil.isEmpty(spuOrderList)) {
            return new ArrayList<>();
        }
        Map<String, List<SkuOrder>> skuGroup = skuGroupBySpuOrderNo(spuOrderList);

        List<SpuOrderExcelVO> rows = new ArrayList<>(spuOrderList.size());
        for (SpuOrder spuOrder : spuOrderList) {
            SpuOrderExcelVO row = new SpuOrderExcelVO();
            row.setId(spuOrder.getId() == null ? null : spuOrder.getId().toString());
            row.setOutOrderNo(spuOrder.getOutOrderNo());
            row.setSupplierAmount(toYuan(spuOrder.getChannelPurchaseAmount()));
            row.setFreightAmount(toYuan(spuOrder.getFreightAmount()));
            int skuCount = skuGroup.getOrDefault(spuOrder.getSpuOrderNo(), Collections.emptyList()).stream()
                    .map(SkuOrder::getBuyNum)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .sum();
            row.setCount(String.valueOf(skuCount));
            row.setOrderState(stateInfo(spuOrder.getOrderState()));
            fillShipInfo(spuOrder.getReceiptInfo(), row::setShipName, row::setShipPhone, row::setShipArea);
            row.setSpuName(spuName(spuOrder.getGoodsSnapshot()));
            row.setRemark(spuOrder.getRemark());
            rows.add(row);
        }
        return rows;
    }

    /**
     * 装配 SPU 订单明细 (SKU 粒度) 导出行
     *
     * @param req 查询条件, 调用方已按登录角色收敛可见范围
     * @return 导出行列表, 恒非 null
     */
    @Override
    public List<SpuOrderItemExcelVO> exportSpuOrderItem(SpuOrderPageReq req) {
        List<SpuOrder> spuOrderList = spuOrderRepository.listByQuery(req);
        if (CollUtil.isEmpty(spuOrderList)) {
            return new ArrayList<>();
        }
        Map<String, SpuOrder> spuOrderMap = spuOrderList.stream()
                .filter(spuOrder -> spuOrder.getSpuOrderNo() != null)
                .collect(Collectors.toMap(SpuOrder::getSpuOrderNo, spuOrder -> spuOrder, (left, right) -> left));
        List<SkuOrder> skuOrderList = skuOrderRepository.listBySpuOrderNo(new ArrayList<>(spuOrderMap.keySet()));
        if (CollUtil.isEmpty(skuOrderList)) {
            return new ArrayList<>();
        }

        List<SpuOrderItemExcelVO> rows = new ArrayList<>(skuOrderList.size());
        for (SkuOrder skuOrder : skuOrderList) {
            SpuOrder spuOrder = spuOrderMap.get(skuOrder.getSpuOrderNo());
            if (spuOrder == null) {
                continue;
            }
            SpuOrderItemExcelVO row = new SpuOrderItemExcelVO();
            row.setId(spuOrder.getId() == null ? null : spuOrder.getId().toString());
            row.setOutOrderNo(spuOrder.getOutOrderNo());
            row.setOrderState(stateInfo(skuOrder.getOrderState()));
            Integer refundingCount = skuOrder.getRefundingQuantity() == null ? 0 : skuOrder.getRefundingQuantity();
            row.setRefundingCount(refundingCount);
            row.setRefunding(refundingCount > 0 ? "是" : "否");
            row.setSpuName(spuName(spuOrder.getGoodsSnapshot()));
            row.setAttribute(saleAttributeText(skuOrder.getSkuSnapshot()));
            row.setSkuId(skuOrder.getSkuId() == null ? null : skuOrder.getSkuId().toString());
            row.setPrice(toYuan(unitSupplierPrice(skuOrder)));
            row.setCount(skuOrder.getBuyNum() == null ? null : skuOrder.getBuyNum().toString());
            fillShipInfo(spuOrder.getReceiptInfo(), row::setShipName, row::setShipPhone, row::setShipArea);
            row.setRemark(spuOrder.getRemark());
            rows.add(row);
        }
        return rows;
    }

    /**
     * 批量取 SKU 子单并按 SPU 交易单号分组
     *
     * @param spuOrderList SPU 订单列表
     * @return SPU 交易单号 → SKU 子单列表
     */
    private Map<String, List<SkuOrder>> skuGroupBySpuOrderNo(List<SpuOrder> spuOrderList) {
        List<String> spuOrderNos = spuOrderList.stream()
                .map(SpuOrder::getSpuOrderNo)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (spuOrderNos.isEmpty()) {
            return new HashMap<>();
        }
        List<SkuOrder> skuOrderList = skuOrderRepository.listBySpuOrderNo(spuOrderNos);
        if (CollUtil.isEmpty(skuOrderList)) {
            return new HashMap<>();
        }
        return skuOrderList.stream()
                .filter(skuOrder -> skuOrder.getSpuOrderNo() != null)
                .collect(Collectors.groupingBy(SkuOrder::getSpuOrderNo));
    }

    /**
     * 全部订单状态码
     *
     * @return 订单状态码列表
     */
    private List<Integer> allStateCodes() {
        List<Integer> codes = new ArrayList<>();
        for (OrderEnum.State state : OrderEnum.State.values()) {
            codes.add(state.getCode());
        }
        return codes;
    }

    /**
     * 订单状态码转中文描述
     *
     * @param stateCode 订单状态码
     * @return 状态中文描述, 未收录的状态码返回 null
     */
    private String stateInfo(Integer stateCode) {
        OrderEnum.State state = OrderEnum.State.getByCode(stateCode);
        return state == null ? null : state.getInfo();
    }

    /**
     * 金额「分」转「元」字符串
     *
     * @param cent 金额 (单位: 分), 可为 null
     * @return 两位小数字符串 (向下截断), 入参为 null 时返回 "0.00"
     */
    private String toYuan(Long cent) {
        long value = cent == null ? 0L : cent;
        return new BigDecimal(value).divide(AMOUNT_UNIT).setScale(2, RoundingMode.DOWN).toPlainString();
    }

    /**
     * 由 SKU 子单反推供货单价
     *
     * <p>旧库 {@code sku_order.sku_supplier_price} 存的是单价, Base 的 SKU 子单只留总额
     * {@code channelPurchaseAmount} (渠道商进货总金额) 与数量 {@code buyNum}, 故按
     * 总额 ÷ 数量还原单价; 数量为空或 0 时退化为总额。</p>
     *
     * @param skuOrder SKU 子单
     * @return 供货单价 (单位: 分)
     */
    private Long unitSupplierPrice(SkuOrder skuOrder) {
        Long amount = skuOrder.getChannelPurchaseAmount() == null ? 0L : skuOrder.getChannelPurchaseAmount();
        Integer buyNum = skuOrder.getBuyNum();
        if (buyNum == null || buyNum == 0) {
            return amount;
        }
        return amount / buyNum;
    }

    /**
     * 解析 SPU 商品快照取 SPU 名称
     *
     * @param goodsSnapshot 商品快照 JSON (结构对等 {@code DistributionDetailVO})
     * @return SPU 名称, 快照缺失或不可解析时返回 null
     */
    private String spuName(String goodsSnapshot) {
        DistributionDetailVO detail = parseJson(goodsSnapshot, DistributionDetailVO.class);
        return detail == null ? null : detail.getSpuName();
    }

    /**
     * 解析 SKU 快照取规格值文案
     *
     * <p>旧库读 {@code sku_order.sku_sale_attribute} (销售属性 JSON 数组), Base 把该列并入
     * {@code sku_order.sku_snapshot} 的 {@code saleAttribute} 字段。数组元素取 {@code value}
     * 以分号拼接 —— 注意旧实现循环内是<b>覆盖赋值</b>而非累加, 只留最后一个规格值, 属源侧 bug,
     * 此处按显然的原意改为拼接全部规格值。</p>
     *
     * @param skuSnapshot SKU 快照 JSON (结构对等 {@code DistributionDetailVO})
     * @return 分号拼接的规格值, 无规格时返回 null
     */
    private String saleAttributeText(String skuSnapshot) {
        DistributionDetailVO detail = parseJson(skuSnapshot, DistributionDetailVO.class);
        if (detail == null || !JSONUtil.isTypeJSONArray(detail.getSaleAttribute())) {
            return null;
        }
        JSONArray array = JSONUtil.parseArray(detail.getSaleAttribute());
        List<String> values = new ArrayList<>(array.size());
        for (Object item : array) {
            if (item instanceof JSONObject json) {
                String value = json.getStr("value");
                if (value != null && !value.isEmpty()) {
                    values.add(value);
                }
            }
        }
        return values.isEmpty() ? null : String.join(";", values);
    }

    /**
     * 解析收货信息快照并回填导出行的收货字段
     *
     * @param receiptInfo  收货信息 JSON (结构对等 {@code ReceiveAddressOutVO})
     * @param nameSetter   收货人姓名写入口
     * @param phoneSetter  收货人手机号写入口
     * @param areaSetter   收货地址写入口 (地区 + "," + 详细地址)
     */
    private void fillShipInfo(String receiptInfo, Consumer<String> nameSetter,
                              Consumer<String> phoneSetter,
                              Consumer<String> areaSetter) {
        ReceiveAddressOutVO address = parseJson(receiptInfo, ReceiveAddressOutVO.class);
        if (address == null) {
            return;
        }
        nameSetter.accept(address.getShipName());
        phoneSetter.accept(address.getShipPhone());
        String shipAddress = address.getShipAddress() == null ? "" : address.getShipAddress();
        areaSetter.accept(address.getShipArea() + "," + shipAddress);
    }

    /**
     * 宽松解析 JSON 快照
     *
     * <p>快照是历史落库的自由文本, 解析失败不应中断整份导出, 故失败只记日志并返回 null。</p>
     *
     * @param json  JSON 文本
     * @param clazz 目标类型
     * @param <T>   目标类型
     * @return 解析结果, 文本为空或解析失败时返回 null
     */
    private <T> T parseJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty() || !JSONUtil.isTypeJSON(json)) {
            return null;
        }
        try {
            return JSONUtil.toBean(json, clazz);
        } catch (Exception e) {
            log.warn("导出解析快照失败, 已跳过该字段, clazz={}, json={}", clazz.getSimpleName(), json, e);
            return null;
        }
    }
}
