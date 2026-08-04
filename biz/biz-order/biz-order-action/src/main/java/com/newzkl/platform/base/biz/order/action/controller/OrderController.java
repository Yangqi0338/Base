package com.newzkl.platform.base.biz.order.action.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.action.cmd.OrderCmd;
import com.newzkl.platform.base.biz.order.application.service.ICommitOrder;
import com.newzkl.platform.base.biz.order.application.service.IOrderService;
import com.newzkl.platform.base.biz.order.application.service.IQueryService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.OperatorApi;
import com.newzkl.platform.base.biz.order.domain.service.IOrderDomain;
import com.newzkl.platform.base.biz.order.model.dto.OrderAgg;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SpuOrderQuery;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.biz.order.model.support.api.PayBaseResult;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.EasyExcelUtil;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 交易-订单
 * @author fang
 */
@RestController
@RequestMapping("/sale/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderDomain orderDomain;

    @Autowired
    private IQueryService queryService;

    @Autowired
    private ICommitOrder commitOrder;

    @Autowired
    private OperatorApi operatorApi;

    /**
     * C端下单
     * @param orderCreate
     * @return
     */
    @PostMapping("memberCreateOrder")
    public PlatformResult<OrderCreateRes> memberCreateOrder(@Validated @RequestBody OrderCmd.OrderCreate orderCreate) {
        MemberOrderCreateCommand memberOrderCreateCommand = Optional.ofNullable(orderCreate.getMemberOrderCreateCommand()).orElse(new MemberOrderCreateCommand());
        memberOrderCreateCommand.setAccountId(SecurityUtils.getAccountId());
        memberOrderCreateCommand.setUserName(SecurityUtils.getUsername());
        memberOrderCreateCommand.setNickName(SecurityUtils.getNickName());
        return PlatformResult.success(commitOrder.createMemberPrePayOrder(orderCreate.getOrderCreateCommand(), memberOrderCreateCommand));
    }

    /**
     * C端消费者 查询预提交订单
     *
     * @param command 支付请求参数
     * @return 支付结果
     */
    @PostMapping("getOrderCreateRes")
    public PlatformResult<OrderCreateRes> getOrderCreateResByRedis(@RequestBody @Validated MemberOrderCreateCommand command) {
        command.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(commitOrder.getOrderCreateResByRedis(command));
    }

    /**
     * C端消费者提交订单
     *
     * @param command 支付请求参数
     * @return 支付结果
     */
    @PostMapping("commitMemberPayment")
    public PlatformResult<OrderAgg> commitMemberPrePayOrder(@RequestBody @Validated CommitMemberOrderCommand command) {
        command.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(commitOrder.commitMemberPrePayOrder(command));
    }


    /**
     * C端消费者支付
     *
     * @param command 支付请求参数
     * @return 支付结果
     */
    @PostMapping("memberPayment")
    public PlatformResult<PayBaseResult> consumerPayment(@RequestBody @Validated PayMemberOrderCommand command) {

        return PlatformResult.success(commitOrder.memberPayOrder(command));
    }

    /**
     * C端订单变更收货地址
     *
     * @param command
     * @return
     */
    @PostMapping("changeOrderShip")
    public PlatformResult<Boolean> changeOrderShip(@RequestBody @Validated OrderShipCommand command) {
        command.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(commitOrder.changeOrderShip(command));
    }

    /**
     * C端取消订单
     * @param idObj SPU订单ID
     * @return
     */
    @PostMapping("memberCancelOrder")
    public PlatformResult<Void> memberCancelOrder(@Validated @RequestBody MemberCancelOrderCommand idObj) {
        orderDomain.memberCancelOrder(idObj.getId(), idObj.getCancelReason());
        return PlatformResult.success();
    }
    /**
     * C端确认收货
     * @param idObj SPU订单ID
     * @return
     */
    @PostMapping("memberConfirmOrder")
    public PlatformResult<Void> memberConfirmOrder(@Validated @RequestBody IdListCommand idObj) {
        orderDomain.receiveSkuOrder(idObj.getId(), null);
        return PlatformResult.success();
    }

    /**
     * C端再来一单
     * @param idObj SPU订单ID
     * @return
     */
    @PostMapping("createOrderAgain")
    public PlatformResult<OrderCreateRes> createOrderAgain(@Validated @RequestBody IdListCommand idObj) {
        return PlatformResult.success(commitOrder.createOrderAgain(idObj.getIdList()));
    }

    /**
     * 渠道商取消交易单
     * @param idObj 交易单ID
     * @return
     */
    @PostMapping("channelCancelOrder")
    public PlatformResult<Void> channelCancelOrder(@Validated @RequestBody IdListCommand idObj) {
        orderDomain.channelCancelOrder(idObj.getId(),"门店主动取消订单" );
        return PlatformResult.success();
    }

    /**
     * SPU订单分页
     * @param spuOrderQuery
     * @return
     */
    @PostMapping("spuOrderPage")
    public PlatformResult<Page<SpuOrderAggVO>> spuOrderPage(@RequestBody SpuOrderQuery spuOrderQuery) {
        boolean b = appendSpuOrderQuery(spuOrderQuery);
        if (!b) {
            return PlatformResult.success(new Page<>());
        }
        Page<SpuOrderAggVO> listOrder = queryService.spuOrderAggVOList(spuOrderQuery);
        return PlatformResult.success(listOrder);
    }

    /**
     * SPU订单详情
     * @param spuOrderId
     * @return
     */
    @GetMapping("spuOrderVO")
    public PlatformResult<SpuOrderAggVO> spuOrderVO(@RequestParam("id") Long spuOrderId) {
        SpuOrderAggVO spuOrderAggVO = queryService.spuOrderAggVO(spuOrderId);
        return PlatformResult.success(spuOrderAggVO);
    }

    /**
     * 交易单余额支付
     *
     * @param idObj
     * @return
     */
    @PostMapping("orderBalancePay")
    public PlatformResult<Void> orderBalancePay(@RequestBody IdListCommand idObj) {
        if (RoleEnum.CompanyRole.CHANNEL == SecurityUtils.getRole()) {
            orderService.orderBalancePay(idObj.getIdList().toArray(new Long[0]));
        }
        return PlatformResult.success();
    }
    /**
     * SPU订单导出
     */
    @PostMapping("exportSpuOrder")
    public void exportSpuOrder(HttpServletResponse response, @RequestBody SpuOrderQuery spuOrderQuery) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("订单", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), SpuOrderExcelVO.class).sheet("模板").doWrite(data(spuOrderQuery));
    }

    /**
     * 运营商: SPU订单状态分组
     */
    @PostMapping("operatorSpuOrderStateCount")
    public PlatformResult<Map<OrderEnum.State, Integer>> spuOrderStateCountMap(@RequestBody @Valid SpuOrderQuery spuOrderQuery) {
        return PlatformResult.success(orderService.spuOrderStateCountMap(spuOrderQuery));
    }
    /**
     * SPU订单明细导出
     */
    @PostMapping("exportSpuOrderItem")
    public void exportSpuOrderItem(@RequestBody SpuOrderQuery spuOrderQuery) throws IOException {
        appendSpuOrderQuery(spuOrderQuery);
        Set<Integer> yellowRowsSet = new HashSet<>();
        RowBackGroundWriteHandler handler = new RowBackGroundWriteHandler(yellowRowsSet, IndexedColors.YELLOW.index);
        List<SpuOrderItemExcelVO> data = orderDomain.querySpuOrderItemExcelVO(spuOrderQuery);
        for (int i = 0; i < data.size(); i++) {
            SpuOrderItemExcelVO spuOrderItemExcelVO = data.get(i);
            if(spuOrderItemExcelVO.getRefundingCount() > 0){
                yellowRowsSet.add(i + 1);
            }
        }
        EasyExcelUtil.export(data, handler, "订单明细");
    }

    private boolean appendSpuOrderQuery(SpuOrderQuery spuOrderQuery) {
        Long accountId = SecurityUtils.getAccountId();
        RoleEnum.CompanyRole role = SecurityUtils.getRole();
        if (RoleEnum.CompanyRole.CHANNEL == role) {
            spuOrderQuery.setChannelId(accountId);
            if(OrderEnum.OrderType.MEMBER == spuOrderQuery.getOrderType() && spuOrderQuery.getStoreId() == null){
                spuOrderQuery.setStoreId(accountId);
            }
        } else if (RoleEnum.CompanyRole.PLATFORM == role) {
            spuOrderQuery.setSpuChannelType(null);
        } else if (RoleEnum.CompanyRole.SUPPLIER == role) {
            spuOrderQuery.setSupplierId(accountId);
            spuOrderQuery.setOrderStateList(Arrays.asList(
                    OrderEnum.State.WAIT_DELIVERY,
                    OrderEnum.State.WAIT_RECEIVE,
                    OrderEnum.State.DOWN_RECEIVE,
                    OrderEnum.State.SUCCESS,
                    OrderEnum.State.CLOSE
            ));
        } else if (RoleEnum.CompanyRole.MEMBER == role) {
            spuOrderQuery.setMemberId(accountId);
        } else if (RoleEnum.CompanyRole.OPERATOR == role) {
            Integer type = spuOrderQuery.getType();
            List<Long> supplierIdList = operatorApi.supplierIdListByType(accountId, type);
            List<Long> searchSupplierIdList = Opt.ofNullable(spuOrderQuery.getSupplierIdList()).orElse(new ArrayList<>());
            Collection<Long> idList = CollUtil.addAll(searchSupplierIdList, spuOrderQuery.getSupplierIdList());
            if (CollUtil.isNotEmpty(idList)) {
                supplierIdList = supplierIdList.stream().filter(idList::contains).collect(Collectors.toList());
            }
            if (CollUtil.isEmpty(supplierIdList)) {
                return false;
            }
            spuOrderQuery.setSupplierIdList(supplierIdList);
            spuOrderQuery.setOrderStateList(Arrays.asList(
                    OrderEnum.State.WAIT_DELIVERY,
                    OrderEnum.State.WAIT_RECEIVE,
                    OrderEnum.State.DOWN_RECEIVE,
                    OrderEnum.State.SUCCESS,
                    OrderEnum.State.CLOSE
            ));
        }

        return true;
    }

    private List<SpuOrderExcelVO> data(SpuOrderQuery spuOrderQuery) {
        appendSpuOrderQuery(spuOrderQuery);
        Page<SpuOrderAggVO> listOrder = queryService.spuOrderAggVOList(spuOrderQuery);
        List<SpuOrderAggVO> list = listOrder.getRecords();
        List<SpuOrderExcelVO> excelDate = new ArrayList<>();
        for (SpuOrderAggVO spuOrderAggVO : list) {
            SpuOrderExcelVO spuOrderExcelVO = new SpuOrderExcelVO();
            spuOrderExcelVO.setId(spuOrderAggVO.getSpuOrderVO().getId().toString());
            spuOrderExcelVO.setOutOrderNo(spuOrderAggVO.getSpuOrderVO().getOutOrderNo());
            BigDecimal supplierAmount = new BigDecimal(spuOrderAggVO.getSpuOrderVO().getSupplierAmount()).divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
            spuOrderExcelVO.setSupplierAmount(supplierAmount.toPlainString());
            BigDecimal freightAmount = new BigDecimal(spuOrderAggVO.getSpuOrderVO().getFreightAmount()).divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
            spuOrderExcelVO.setFreightAmount(freightAmount.toPlainString());
            Integer skuCount = 0;
            if(spuOrderAggVO.getSkuOrderList() != null){
                for (SkuOrderVO skuOrderVO : spuOrderAggVO.getSkuOrderList()) {
                    skuCount = skuCount + skuOrderVO.getCount();
                }
            }
            spuOrderExcelVO.setCount(String.valueOf(skuCount));
            spuOrderExcelVO.setOrderState(spuOrderAggVO.getSpuOrderVO().getOrderState().getValue());
            ShipVO shipVO = JSON.parseObject(spuOrderAggVO.getSpuOrderVO().getShipVO(), ShipVO.class);
            spuOrderExcelVO.setShipName(shipVO.getShipName());
            spuOrderExcelVO.setShipPhone(shipVO.getShipPhone());
            String shipAddress = shipVO.getShipAddress() == null ? "":shipVO.getShipAddress();
            spuOrderExcelVO.setShipArea(shipVO.getShipArea() + "," + shipAddress);
            spuOrderExcelVO.setSpuName(spuOrderAggVO.getSpuOrderVO().getSpuName());
            spuOrderExcelVO.setRemark(spuOrderAggVO.getSpuOrderVO().getRemark());
            excelDate.add(spuOrderExcelVO);
        }
        return excelDate;
    }

    /**
     * 统计渠道商所有订单状态数量
     * @return 订单状态统计列表
     */
    @GetMapping("/countOrderState")
    public PlatformResult<List<OrderStateCountVO>> countOrderState() {
        // 获取当前渠道商ID
        SpuOrderQuery orderQuery = new SpuOrderQuery();
        orderQuery.setChannelId(SecurityUtils.getAccountId());
        List<OrderStateCountVO> stateCountList = orderService.countOrderState(orderQuery);
        return PlatformResult.success(stateCountList);
    }

    /**
     * 统计消费者所有订单状态数量
     * @return 订单状态统计列表
     */
    @GetMapping("/countState")
    public PlatformResult<List<OrderStateCountVO>> countState() {
        SpuOrderQuery orderQuery = new SpuOrderQuery();
        orderQuery.setMemberId(SecurityUtils.getAccountId());
        List<OrderStateCountVO> stateCountList = orderService.countOrderState(orderQuery);
        return PlatformResult.success(stateCountList);
    }
}
