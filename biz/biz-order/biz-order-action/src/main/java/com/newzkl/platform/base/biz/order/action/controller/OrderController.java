package com.newzkl.platform.base.biz.order.action.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.action.cmd.OrderCmd;
import com.newzkl.platform.base.biz.order.action.excel.RowBackGroundWriteHandler;
import com.newzkl.platform.base.biz.order.application.service.QueryOrderService;
import com.newzkl.platform.base.biz.order.application.service.UpdateOrderService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.OperatorApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SkuOrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SpuOrderRepository;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.order.model.order.dto.Order;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.CancelOrderReq;
import com.newzkl.platform.base.biz.order.model.order.req.ConfirmOrderReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderAddressUpdateReq;
import com.newzkl.platform.base.biz.order.model.order.req.SpuOrderPageReq;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateCountVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderAggVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderItemExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.EasyExcelUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 交易-订单控制器
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.interfaces.controller.OrderController}
 * ({@code /sale/order}, 23 端点)。已落 <b>16 个</b>, 路径与 HTTP 方法逐字保持不变
 * (含旧写法里 {@code /countOrderState} / {@code /countState} 的前导斜杠,
 * 与其余端点无前导斜杠的写法差异一并保留)。</p>
 *
 * <p><b>⚠️ 其中 9 个写端点当前调用即抛 </b> ——
 * {@code UpdateOrderServiceImpl} / {@code QueryOrderServiceImpl} 的对应方法尚未实现
 * (缺订单主聚合 {@code OrderAgg} 状态机、库存释放、退款联动、待结算单生成、支付端口)。
 * 这里先把 URL 契约与入参换算接通, 补 domain 能力后无需改本类。</p>
 *
 * <p><b>已落端点偏离说明:</b></p>
 * <ul>
 *   <li>出参信封 {@code ScmResult} → {@code PlatformResult}; 分页壳 {@code PageInfo} →
 *       MyBatis-Plus {@code Page} (Architecture.md 规定的分页迁移口径)。</li>
 *   <li>{@code spuOrderPage} 入参 {@code SpuOrderQuery} → {@code SpuOrderPageReq};
 *       出参元素 {@code SpuOrderAggVO} → {@code SpuOrderVO} (Base 的 {@code SpuOrderAggVO}
 *       是「SPU + SKU 列表」详情聚合, 不用于列表页; 列表页 SKU 明细旧版亦未下发)。</li>
 *   <li>{@code spuOrderVO} 入参仍为主键 {@code id} (Long), controller 内经
 *       {@link SpuOrderRepository#getById} 换算为 Base 的业务单号 {@code spuOrderNo}
 *       再取 SKU 子单, 前端契约不变。</li>
 *   <li>{@code countOrderState} / {@code countState} 旧版直连 {@code IOrderRepository},
 *       此处改走 {@code QueryOrderService}, 语义一致。</li>
 *   <li>旧 {@code @Limit(FuncCons...)} / {@code @RoleLimit} 权限点不在本层声明,
 *       鉴权切面归入口 starter (鉴权降级, 见 07 号台账)。</li>
 * </ul>
 *
 * <p><b>本轮补齐 3 端点 (真实现, 无桩):</b> {@code exportSpuOrder} /
 * {@code exportSpuOrderItem} (新增 {@code SpuOrderExcelVO} / {@code SpuOrderItemExcelVO} 与
 * {@code QueryOrderService#exportSpuOrder*}, 数据源为本域仓储 + 快照 JSON 解析) 与
 * {@code operatorSpuOrderStateCount} (新增 {@code SpuOrderRepository#stateCountMap} 分组统计)。
 * 三者均为纯查询 + Excel/计数, 不触碰状态机与资金流。</p>
 *
 * <p><b>🔴 未迁 7 端点 (缺前置能力, 非契约问题):</b></p>
 * <ul>
 *   <li><b>缺 C 端下单编排 + 支付端口</b>: {@code memberCreateOrder} /
 *       {@code getOrderCreateRes} / {@code commitMemberPayment} / {@code memberPayment} /
 *       {@code createOrderAgain} (旧 {@code ICommitOrder} 全链, Base 无对等编排;
 *       {@code PayBaseResult} 在 biz-finance-model, biz-order-action 未依赖该模块)。</li>
 *   <li><b>缺 VO 类型</b>: {@code orderPage} 需 {@code OrderAggVO}、{@code orderPayVO} 需
 *       {@code OrderPayVO} —— 两者全仓零命中。</li>
 * </ul>
 *
 * <p><b>⚠️ 源侧 bug 未照抄</b>: 旧 {@code appendSpuOrderQuery} 的渠道商分支写
 * {@code OrderEnum.OrderType.MEMBER.getCode().equals(spuOrderQuery)} —— 拿枚举 code 与
 * <b>整个 query 对象</b>比较, 恒 false, 该分支的 {@code setStoreId} 永不执行。本类按
 * <b>显然的原意</b>修正为与 {@code spuOrderQuery.getOrderType()} 比较, 并在此留档:
 * 修正后渠道商查 C 端订单会额外收敛 storeId, 与旧线上行为不同, <b>需人工确认</b>。</p>
 *
 * @author KC
 */
@RestController("orderController")
@RequestMapping("/sale/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    /**
     * 供应商/运营商可见的订单状态白名单 (逐字沿用旧 {@code appendSpuOrderQuery})
     */
    private static final List<Integer> VISIBLE_STATES = Arrays.asList(
            OrderEnum.State.WAIT_DELIVERY.getCode(),
            OrderEnum.State.WAIT_RECEIVE.getCode(),
            OrderEnum.State.DOWN_RECEIVE.getCode(),
            OrderEnum.State.SUCCESS.getCode(),
            OrderEnum.State.CLOSE.getCode()
    );

    private final QueryOrderService queryOrderService;

    private final UpdateOrderService updateOrderService;

    private final OperatorApi operatorApi;

    private final OrderRepository orderRepository;

    private final SpuOrderRepository spuOrderRepository;

    private final SkuOrderRepository skuOrderRepository;

    /**
     * 分页查询 SPU 订单
     *
     * @param spuOrderPageReq SPU 订单分页查询条件, 按当前登录角色自动收敛可见范围
     * @return SPU 订单视图分页结果; 运营商无可见供应商时返回空页
     */
    @PostMapping("spuOrderPage")
    public PlatformResult<Page<SpuOrderVO>> spuOrderPage(@RequestBody SpuOrderPageReq spuOrderPageReq) {
        if (!appendSpuOrderQuery(spuOrderPageReq)) {
            return PlatformResult.success(new Page<>(spuOrderPageReq.getCurrent(), spuOrderPageReq.getSize()));
        }
        return PlatformResult.success(queryOrderService.spuPage(spuOrderPageReq));
    }

    /**
     * 查询 SPU 订单详情 (含 SKU 子订单列表)
     *
     * @param spuOrderId SPU 订单主键 ID, 沿用旧契约参数名 {@code id}
     * @return SPU 订单详情聚合视图
     * @throws PlatformException 主键不存在时抛出
     */
    @GetMapping("spuOrderVO")
    public PlatformResult<SpuOrderAggVO> spuOrderVO(@RequestParam("id") Long spuOrderId) {
        SpuOrder spuOrder = spuOrderRepository.getById(spuOrderId);
        if (spuOrder == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "SPU订单");
        }
        SpuOrderAggVO aggVO = new SpuOrderAggVO();
        aggVO.setSpuOrderVO(TransferUtils.transfer(spuOrder, SpuOrderVO::new));
        List<SkuOrder> skuOrderList = skuOrderRepository.getBySpuOrderNo(spuOrder.getSpuOrderNo());
        aggVO.setSkuOrderList(skuOrderList == null ? new ArrayList<>() : skuOrderList);
        return PlatformResult.success(aggVO);
    }

    /**
     * 统计当前渠道商各状态订单数量
     *
     * @return 订单状态统计列表
     */
    @GetMapping("/countOrderState")
    public PlatformResult<List<OrderStateCountVO>> countOrderState() {
        return PlatformResult.success(queryOrderService.countOrderStateByChannel(SecurityUtils.getAccountId()));
    }

    /**
     * 统计当前登录账号各状态订单数量 (C 端)
     *
     * @return 订单状态统计列表
     */
    @GetMapping("/countState")
    public PlatformResult<List<OrderStateCountVO>> countState() {
        return PlatformResult.success(queryOrderService.countOrderStateByAccount(SecurityUtils.getAccountId()));
    }

    /**
     * 修改订单收货地址
     *
     * @param cmd 订单主键 ID + 新收货地址主键 ID
     * @return 是否修改成功
     */
    @PostMapping("changeOrderShip")
    public PlatformResult<Boolean> changeOrderShip(@RequestBody @Validated OrderCmd.ChangeShip cmd) {
        OrderAddressUpdateReq req = new OrderAddressUpdateReq();
        req.setOrderNo(orderNoById(cmd.getOrderId()));
        req.setShipId(cmd.getShipId());
        req.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(updateOrderService.changeOrderShip(req));
    }

    /**
     * C 端取消订单
     *
     * @param cmd 订单主键 ID + 取消原因
     * @return 空信封
     */
    @PostMapping("memberCancelOrder")
    public PlatformResult<Void> memberCancelOrder(@RequestBody @Validated OrderCmd.Cancel cmd) {
        updateOrderService.cancelOrder(cancelReq(cmd.getId(), cmd.getCancelReason(), RoleEnum.CompanyRole.MEMBER));
        return PlatformResult.success();
    }

    /**
     * C 端确认收货
     *
     * @param cmd 订单主键 ID
     * @return 空信封
     */
    @PostMapping("memberConfirmOrder")
    public PlatformResult<Void> memberConfirmOrder(@RequestBody @Validated OrderCmd.ID cmd) {
        updateOrderService.confirmOrder(confirmReq(cmd.getId(), RoleEnum.CompanyRole.MEMBER));
        return PlatformResult.success();
    }

    /**
     * 渠道商取消订单
     *
     * @param cmd 订单主键 ID
     * @return 空信封
     */
    @PostMapping("channelCancelOrder")
    public PlatformResult<Void> channelCancelOrder(@RequestBody @Validated OrderCmd.ID cmd) {
        updateOrderService.cancelOrder(cancelReq(cmd.getId(), null, RoleEnum.CompanyRole.CHANNEL));
        return PlatformResult.success();
    }

    /**
     * 渠道商确认收货
     *
     * @param cmd 订单主键 ID
     * @return 空信封
     */
    @PostMapping("channelConfirmOrder")
    public PlatformResult<Void> channelConfirmOrder(@RequestBody @Validated OrderCmd.ID cmd) {
        updateOrderService.confirmOrder(confirmReq(cmd.getId(), RoleEnum.CompanyRole.CHANNEL));
        return PlatformResult.success();
    }

    /**
     * 完成订单
     *
     * <p>旧版按 {@code spuOrderId} + {@code skuOrderId} 完成单条 SKU, Base 的
     * {@code completeOrder} 按主订单号整单完成, 故 {@code skuOrderId} 当前不参与调用。</p>
     *
     * @param cmd SPU 订单主键 ID (+ 契约保留的 SKU 订单主键 ID)
     * @return 空信封
     */
    @PostMapping("completeOrder")
    public PlatformResult<Void> completeOrder(@RequestBody @Validated OrderCmd.CompleteOrder cmd) {
        SpuOrder spuOrder = spuOrderRepository.getById(cmd.getSpuOrderId());
        if (spuOrder == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "SPU订单");
        }
        ConfirmOrderReq req = new ConfirmOrderReq();
        req.setOrderNo(spuOrder.getOrderNo());
        updateOrderService.completeOrder(req);
        return PlatformResult.success();
    }

    /**
     * 渠道商交易单余额支付 (批量)
     *
     * @param cmd 订单主键 ID 列表
     * @return 空信封
     */
    @PostMapping("orderBalancePay")
    public PlatformResult<Void> orderBalancePay(@RequestBody @Validated OrderCmd.IdList cmd) {
        String[] orderNos = cmd.getIdList().stream().map(this::orderNoById).toArray(String[]::new);
        queryOrderService.orderBalancePay(orderNos);
        return PlatformResult.success();
    }

    /**
     * 运营商交易单余额支付
     *
     * @param cmd 订单主键 ID
     * @return 空信封
     */
    @PostMapping("operatorOrderBalancePay")
    public PlatformResult<Void> operatorOrderBalancePay(@RequestBody @Validated OrderCmd.ID cmd) {
        queryOrderService.operatorOrderBalancePay(orderNoById(cmd.getId()));
        return PlatformResult.success();
    }

    /**
     * 交易单直接支付
     *
     * @param cmd 订单主键 ID
     * @return 空信封
     */
    @PostMapping("orderDirectPay")
    public PlatformResult<Void> orderDirectPay(@RequestBody @Validated OrderCmd.ID cmd) {
        queryOrderService.orderDirectPay(orderNoById(cmd.getId()));
        return PlatformResult.success();
    }

    /**
     * SPU 订单导出
     *
     * <p><b>刻意偏离 (安全)</b>: 旧 {@code data()} 忽略 {@code appendSpuOrderQuery} 的返回值,
     * 运营商无可见供应商时不写入 supplierIdList, 导出反而变成<b>全量订单</b>;
     * 此处与 {@code spuOrderPage} 对齐, 无可见范围时导出空表。</p>
     *
     * @param response        HTTP 响应, 直写 xlsx 字节流
     * @param spuOrderPageReq SPU 订单查询条件, 按当前登录角色自动收敛可见范围
     * @throws IOException 响应输出流写出失败时抛出
     */
    @PostMapping("exportSpuOrder")
    public void exportSpuOrder(HttpServletResponse response, @RequestBody SpuOrderPageReq spuOrderPageReq)
            throws IOException {
        List<SpuOrderExcelVO> rows = appendSpuOrderQuery(spuOrderPageReq)
                ? queryOrderService.exportSpuOrder(spuOrderPageReq)
                : new ArrayList<>();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("订单", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), SpuOrderExcelVO.class).sheet("模板").doWrite(rows);
    }

    /**
     * SPU 订单明细导出
     *
     * <p>存在售后的行整行标黄, 与旧版一致 (黄条判定取 SKU 子单售后中数量 &gt; 0)。
     * 无数据时 {@link EasyExcelUtil#export} 不写出文件, 沿用旧行为。</p>
     *
     * <p><b>刻意偏离 (安全)</b>: 同 {@link OrderController#exportSpuOrder}, 运营商无可见供应商时导出空,
     * 不再退化为全量。</p>
     *
     * @param spuOrderPageReq SPU 订单查询条件, 按当前登录角色自动收敛可见范围
     * @throws IOException 响应输出流写出失败时抛出
     */
    @PostMapping("exportSpuOrderItem")
    public void exportSpuOrderItem(@RequestBody SpuOrderPageReq spuOrderPageReq) throws IOException {
        if (!appendSpuOrderQuery(spuOrderPageReq)) {
            return;
        }
        List<SpuOrderItemExcelVO> rows = queryOrderService.exportSpuOrderItem(spuOrderPageReq);
        Set<Integer> highlightRows = new HashSet<>();
        for (int i = 0; i < rows.size(); i++) {
            Integer refundingCount = rows.get(i).getRefundingCount();
            if (refundingCount != null && refundingCount > 0) {
                highlightRows.add(i + 1);
            }
        }
        EasyExcelUtil.export(rows, new RowBackGroundWriteHandler(highlightRows, IndexedColors.YELLOW.index), "订单明细");
    }

    /**
     * 运营商: SPU 订单状态分组统计
     *
     * <p>旧 {@code @RoleLimit({OPERATOR})} 角色限制不在本层声明 (鉴权切面归入口 starter,
     * 见 07 号台账)。可见状态白名单在本层预置, 供应商可见范围收敛在应用服务内完成。</p>
     *
     * @param spuOrderPageReq SPU 订单查询条件
     * @return 订单状态 → 订单数, 白名单内无数据的状态补 0
     */
    @PostMapping("operatorSpuOrderStateCount")
    public PlatformResult<Map<Integer, Integer>> operatorSpuOrderStateCount(
            @RequestBody @Validated SpuOrderPageReq spuOrderPageReq) {
        spuOrderPageReq.setOrderStateList(new ArrayList<>(VISIBLE_STATES));
        return PlatformResult.success(
                queryOrderService.spuOrderStateCountMap(spuOrderPageReq, SecurityUtils.getAccountId()));
    }

    /**
     * 按订单主键 ID 换算交易单号
     *
     * <p>Base 交易域已把业务标识由主键 ID 换成交易单号 {@code orderNo}, 旧契约仍传 ID,
     * 故在 action 层统一换算, 前端契约不变。</p>
     *
     * @param orderId 订单主键 ID
     * @return 交易单号
     * @throws PlatformException 主键不存在时抛出
     */
    private String orderNoById(Long orderId) {
        Order order = orderRepository.getById(orderId);
        if (order == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "订单");
        }
        return order.getOrderNo();
    }

    /**
     * 组装取消订单请求
     *
     * @param orderId      订单主键 ID
     * @param cancelReason 取消原因, 可为 null
     * @param companyRole  发起方角色
     * @return 取消订单请求
     */
    private CancelOrderReq cancelReq(Long orderId, String cancelReason, RoleEnum.CompanyRole companyRole) {
        CancelOrderReq req = new CancelOrderReq();
        req.setOrderNo(orderNoById(orderId));
        req.setCancelReason(cancelReason);
        req.setCompanyRole(companyRole);
        return req;
    }

    /**
     * 组装确认收货请求
     *
     * @param orderId     订单主键 ID
     * @param companyRole 发起方角色
     * @return 确认收货请求
     */
    private ConfirmOrderReq confirmReq(Long orderId, RoleEnum.CompanyRole companyRole) {
        ConfirmOrderReq req = new ConfirmOrderReq();
        req.setOrderNo(orderNoById(orderId));
        req.setCompanyRole(companyRole);
        return req;
    }

    /**
     * 按当前登录角色收敛 SPU 订单查询范围
     *
     * <p>逐字迁自旧 {@code appendSpuOrderQuery}, 唯一差异: 平台角色分支旧版清空
     * {@code spuChannelType}, Base {@code SpuOrderPageReq} 无该字段且平台默认即全量,
     * 故不再需要该分支。运营商分支走 {@code OperatorApi} 出站端口 (biz-order-infrastructure
     * 经 {@code biz-account-facade} 跨域), 与旧 Dubbo {@code IOperatorFacade} 语义一致:
     * 先取运营类型可见供应商, 与请求指定的 supplierId(List) 求交集, 交集为空即无可见数据。</p>
     *
     * @param req SPU 订单分页查询条件, 原地改写
     * @return {@code false} 表示当前角色无任何可见数据, 调用方应直接返回空页
     */
    private boolean appendSpuOrderQuery(SpuOrderPageReq req) {
        Long accountId = SecurityUtils.getAccountId();
        Long roleId = SecurityUtils.getRoleId();
        if (RoleEnum.CompanyRole.CHANNEL.getCode().equals(roleId)) {
            req.setChannelId(accountId);
            if (OrderEnum.OrderType.MEMBER.getCode().equals(req.getOrderType()) && req.getStoreId() == null) {
                req.setStoreId(accountId);
            }
        } else if (RoleEnum.CompanyRole.SUPPLIER.getCode().equals(roleId)) {
            req.setSupplierId(accountId);
            req.setOrderStateList(VISIBLE_STATES);
        } else if (RoleEnum.CompanyRole.MEMBER.getCode().equals(roleId)) {
            req.setMemberId(accountId);
        } else if (RoleEnum.CompanyRole.OPERATOR.getCode().equals(roleId)) {
            List<Long> visibleSupplierIdList = operatorApi.supplierIdListByType(accountId, req.getType());
            List<Long> searchIdList = new ArrayList<>();
            if (req.getSupplierIdList() != null) {
                searchIdList.addAll(req.getSupplierIdList());
            }
            if (req.getSupplierId() != null) {
                searchIdList.add(req.getSupplierId());
            }
            if (!searchIdList.isEmpty()) {
                visibleSupplierIdList = visibleSupplierIdList.stream()
                        .filter(searchIdList::contains)
                        .collect(Collectors.toList());
            }
            if (visibleSupplierIdList.isEmpty()) {
                return false;
            }
            req.setSupplierIdList(visibleSupplierIdList);
            req.setOrderStateList(VISIBLE_STATES);
        }
        return true;
    }
}
