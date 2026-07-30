package com.newzkl.platform.base.biz.order.action.controller;

import com.alibaba.excel.EasyExcel;
import com.newzkl.platform.base.biz.order.action.cmd.DeliverCmd;
import com.newzkl.platform.base.biz.order.action.vo.DeliverVO;
import com.newzkl.platform.base.biz.order.application.service.OrderDeliveryAppService;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SkuOrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SpuOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.OrderDeliveryDomain;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderDelivery;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryUpdateReq;
import com.newzkl.platform.base.biz.order.model.order.vo.FullDeliverExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SplitDeliverExcelVO;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 交易-发货控制器
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.interfaces.controller.DeliverController},
 * 5 端点路径与 HTTP 方法逐字保持不变 (含旧写法里 {@code /fullDeliver} / {@code /splitDeliver}
 * 的前导斜杠)。偏离说明:</p>
 * <ul>
 *   <li>入参标识逐字沿用旧定义: {@code spuOrderId} 为 SPU 订单**主键** (Long)。
 *       controller 内经 {@code SpuOrderRepository} / {@code SkuOrderRepository} 按主键取出订单,
 *       换算为 Base 发货入参所需的 {@code orderNo} / {@code skuOrderNo}。</li>
 *   <li>{@code orderDeliverInfo} 出参仍按 skuId 分组, 元素为 action 层 {@code DeliverVO} ——
 *       字段名逐字沿用旧 {@code DeliverVO} ({@code expressCompanyName} / {@code expressNo} /
 *       {@code expressMobile}), 由 Base 发货 DTO {@code OrderDelivery} 的
 *       {@code logisticsName} / {@code logisticsNo} / {@code expressPhone} 转换而来。
 *       2026-07-30 修: 此前直接返回 {@code OrderDelivery}, 致 3 个前端物流栏静默空白。</li>
 *   <li>导入结果由旧 {@code ExcelErrorVO} 换为 Base 通用 {@code EasyExcelErrorVO},
 *       行号口径沿用旧值 (数据行 i 记为 i+2)。</li>
 *   <li>旧 {@code @Limit(FuncCons...)} 权限点不在本层声明, 鉴权切面归入口 starter (鉴权降级)。</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/sale/deliver")
@RequiredArgsConstructor
public class DeliverController {

    /**
     * Excel 数据行首行对应的展示行号偏移 (表头 1 行, 行号从 1 起算)
     */
    private static final int EXCEL_ROW_OFFSET = 2;

    private final OrderDeliveryAppService orderDeliveryAppService;

    private final OrderDeliveryDomain orderDeliveryDomain;

    private final SpuOrderRepository spuOrderRepository;

    private final SkuOrderRepository skuOrderRepository;

    /**
     * 发货创建
     *
     * @param deliverCommand 发货创建入参
     * @return 成功结果
     */
    @PostMapping("deliverCreate")
    public PlatformResult<Void> deliverCreate(@Validated @RequestBody DeliverCmd.Deliver deliverCommand) {
        orderDeliveryAppService.deliverOrder(toCreateReq(deliverCommand));
        return PlatformResult.success();
    }

    /**
     * SPU订单发货信息
     *
     * @param deliverInfoReq SPU 订单发货信息查询入参
     * @return 按 skuId 分组的发货单列表, 元素为旧字段名出参 {@code DeliverVO}
     */
    @PostMapping("orderDeliverInfo")
    public PlatformResult<Map<Long, List<DeliverVO>>> orderDeliverInfo(
            @Validated @RequestBody DeliverCmd.OrderDeliverInfoReq deliverInfoReq) {
        SpuOrder spuOrder = spuOrderRepository.getById(deliverInfoReq.getSpuOrderId());
        Assert.notNull(spuOrder, "SPU订单不存在");
        Map<Long, List<OrderDelivery>> grouped =
                orderDeliveryDomain.deliverInfoBySpuOrderNo(spuOrder.getSpuOrderNo());
        Map<Long, List<DeliverVO>> result = new LinkedHashMap<>(grouped.size());
        grouped.forEach((skuId, deliveries) ->
                result.put(skuId, deliveries.stream().map(DeliverVO::of).collect(Collectors.toList())));
        return PlatformResult.success(result);
    }

    /**
     * 修改物流单号
     *
     * @param deliverCodeCommand 物流单号修改入参
     * @return 成功结果
     */
    @PostMapping("deliverEdit")
    public PlatformResult<Void> deliverEdit(@Validated @RequestBody DeliverCmd.DeliverCode deliverCodeCommand) {
        OrderDeliveryUpdateReq req = new OrderDeliveryUpdateReq();
        req.setId(deliverCodeCommand.getId());
        req.setLogisticsName(deliverCodeCommand.getExpressCompanyName());
        req.setLogisticsNo(deliverCodeCommand.getExpressNo());
        req.setExpressPhone(deliverCodeCommand.getExpressMobile());
        orderDeliveryDomain.updateDelivery(req);
        return PlatformResult.success();
    }

    /**
     * 整单发货 (Excel 导入)
     *
     * @param file 导入文件
     * @return 导入结果 (含逐行错误)
     */
    @PostMapping(value = "/fullDeliver")
    public PlatformResult<EasyExcelErrorVO> fullDeliver(MultipartFile file) {
        List<FullDeliverExcelVO> lst = readSheet(file, FullDeliverExcelVO.class);
        return PlatformResult.success(importDeliver(lst, item -> {
            DeliverCmd.Deliver deliver = new DeliverCmd.Deliver();
            deliver.setSpuOrderId(Long.valueOf(item.getSpuOrderId()));
            deliver.setExpressCompanyName(item.getExpressCompanyName());
            deliver.setExpressNo(item.getExpressNo());
            deliver.setDeliverItemCommandList(null);
            return deliver;
        }));
    }

    /**
     * 拆单发货 (Excel 导入)
     *
     * @param file 导入文件
     * @return 导入结果 (含逐行错误)
     */
    @PostMapping(value = "/splitDeliver")
    public PlatformResult<EasyExcelErrorVO> splitDeliver(MultipartFile file) {
        List<SplitDeliverExcelVO> lst = readSheet(file, SplitDeliverExcelVO.class);
        return PlatformResult.success(importDeliver(lst, item -> {
            DeliverCmd.Deliver deliver = new DeliverCmd.Deliver();
            deliver.setSpuOrderId(Long.valueOf(item.getSpuOrderId()));
            deliver.setExpressCompanyName(item.getExpressCompanyName());
            deliver.setExpressNo(item.getExpressNo());
            DeliverCmd.DeliverItem deliverItem = new DeliverCmd.DeliverItem();
            deliverItem.setSkuId(Long.valueOf(item.getSkuId()));
            deliver.setDeliverItemCommandList(Collections.singletonList(deliverItem));
            return deliver;
        }));
    }

    /**
     * 读取导入文件首个 sheet (表头 1 行), 沿用旧同步读取行为
     *
     * @param file  导入文件
     * @param clazz 行对象类型
     * @param <T>   行对象类型
     * @return 行对象列表
     */
    private <T> List<T> readSheet(MultipartFile file, Class<T> clazz) {
        try (InputStream inputStream = file.getInputStream()) {
            return EasyExcel.read(inputStream)
                    .head(clazz)
                    .sheet(0)
                    .headRowNumber(1)
                    .doReadSync();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * 逐行发货并累加异常行, 单行失败不中断整批 (沿用旧导入语义)
     *
     * @param rows      Excel 行列表
     * @param converter 行对象转发货入参
     * @param <T>       行对象类型
     * @return 导入结果
     */
    private <T> EasyExcelErrorVO importDeliver(List<T> rows, Function<T, DeliverCmd.Deliver> converter) {
        List<EasyExcelErrorVO.ErrorLineVO> errorLines = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            String line = String.valueOf(i + EXCEL_ROW_OFFSET);
            try {
                orderDeliveryAppService.deliverOrder(toCreateReq(converter.apply(rows.get(i))));
            } catch (PlatformException e) {
                errorLines.add(new EasyExcelErrorVO.ErrorLineVO(line, e.getMessage()));
            } catch (NumberFormatException e) {
                errorLines.add(new EasyExcelErrorVO.ErrorLineVO(line, "存在格式错误, 需为数字"));
            } catch (Exception e) {
                log.warn("发货导入第{}行失败", line, e);
                errorLines.add(new EasyExcelErrorVO.ErrorLineVO(line, e.getMessage()));
            }
        }
        int total = rows.size();
        int errorCount = errorLines.size();
        return new EasyExcelErrorVO(null, total, total - errorCount, errorCount, errorLines);
    }

    /**
     * 旧发货入参换算为 Base 发货创建入参
     *
     * <p>旧契约以 SPU 订单**主键** + skuId 表达发货范围, Base 发货入参以主订单号 + SKU 订单号表达,
     * 故此处按主键取出该 SPU 订单, 再取主订单号与 SPU 交易单号, 并按 skuId 反查 SKU 订单号。</p>
     *
     * @param deliverCommand 旧发货入参
     * @return Base 发货创建入参
     */
    private OrderDeliveryCreateReq toCreateReq(DeliverCmd.Deliver deliverCommand) {
        SpuOrder spuOrder = spuOrderRepository.getById(deliverCommand.getSpuOrderId());
        Assert.notNull(spuOrder, "SPU订单不存在");
        String spuOrderNo = spuOrder.getSpuOrderNo();

        OrderDeliveryCreateReq req = new OrderDeliveryCreateReq();
        req.setOrderNo(spuOrder.getOrderNo());
        req.setLogisticsName(deliverCommand.getExpressCompanyName());
        req.setLogisticsNo(deliverCommand.getExpressNo());
        req.setExpressPhone(deliverCommand.getExpressMobile());
        req.setOperatorId(SecurityUtils.getAccountId());
        Long roleId = SecurityUtils.getRoleId();
        req.setOperatorRole(roleId == null ? null : roleId.intValue());
        req.setItems(toItemReqList(spuOrderNo, deliverCommand.getDeliverItemCommandList()));
        return req;
    }

    /**
     * 发货明细换算: skuId 反查 SKU 订单号。明细为空表示整单发货, 直接返回 null 交由领域层补全。
     *
     * @param spuOrderNo SPU 交易单号
     * @param itemList   旧发货明细
     * @return Base 发货明细列表, 整单发货时为 null
     */
    private List<OrderDeliveryCreateReq.DeliveryItemReq> toItemReqList(String spuOrderNo,
                                                                      List<DeliverCmd.DeliverItem> itemList) {
        if (itemList == null || itemList.isEmpty()) {
            return null;
        }
        Map<Long, SkuOrder> skuMap = skuOrderRepository.getBySpuOrderNo(spuOrderNo).stream()
                .collect(Collectors.toMap(SkuOrder::getSkuId, sku -> sku, (a, b) -> a));
        List<OrderDeliveryCreateReq.DeliveryItemReq> reqList = new ArrayList<>(itemList.size());
        for (DeliverCmd.DeliverItem item : itemList) {
            SkuOrder skuOrder = skuMap.get(item.getSkuId());
            Assert.notNull(skuOrder, "SKU不存在");
            OrderDeliveryCreateReq.DeliveryItemReq itemReq = new OrderDeliveryCreateReq.DeliveryItemReq();
            itemReq.setSkuOrderNo(skuOrder.getSkuOrderNo());
            itemReq.setDeliveryQuantity(item.getCount() == null
                    ? skuOrder.getBuyNum() - skuOrder.getDeliveryQuantity()
                    : item.getCount());
            reqList.add(itemReq);
        }
        return reqList;
    }
}
