package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.pack.service.PackOrderService;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PayResultDTO;
import com.newzkl.platform.base.biz.account.domain.service.PackGoodsDomain;
import com.newzkl.platform.base.biz.account.domain.service.PackOrderDomain;
import com.newzkl.platform.base.biz.account.model.pack.query.PackGoodsQuery;
import com.newzkl.platform.base.biz.account.model.pack.query.PackOrderQuery;
import com.newzkl.platform.base.biz.account.model.pack.req.PackGoodsCommand;
import com.newzkl.platform.base.biz.account.model.pack.req.PackOrderCommand;
import com.newzkl.platform.base.biz.account.model.pack.req.PackOrderDeliverCommand;
import com.newzkl.platform.base.biz.account.model.pack.req.PackOrderPayReq;
import com.newzkl.platform.base.biz.account.model.pack.res.PackGoodsRes;
import com.newzkl.platform.base.biz.account.model.pack.res.PackOrderPreRes;
import com.newzkl.platform.base.biz.account.model.pack.res.PackOrderRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台-礼包
 *
 * @author KC
 */
@RestController
@RequestMapping("/account/pack")
@Validated
@RequiredArgsConstructor
public class PackController {

    private final PackGoodsDomain packGoodsDomain;
    private final PackOrderService packOrderService;
    private final PackOrderDomain packOrderDomain;

    /**
     * 礼包商品创建
     *
     * @param command 写入入参
     * @return 礼包主键ID
     */
    @PostMapping("/goods/save")
    public PlatformResult<Long> packGoodsSave(@Valid @RequestBody PackGoodsCommand command) {
        return PlatformResult.success(packGoodsDomain.packGoodsSave(command));
    }

    /**
     * 礼包商品删除
     *
     * @param idListCommand ID 列表入参
     * @return 空结果
     */
    @PostMapping("/goods/delete")
    public PlatformResult<Void> packGoodsDelete(@Valid @RequestBody IdCommand idListCommand) {
        packGoodsDomain.packGoodsDelete(idListCommand.getIdList());
        return PlatformResult.success();
    }

    /**
     * 礼包商品修改
     *
     * @param command 写入入参
     * @return 空结果
     */
    @PostMapping("/goods/update")
    public PlatformResult<Void> packGoodsUpdate(@RequestBody PackGoodsCommand command) {
        packGoodsDomain.packGoodsSave(command);
        return PlatformResult.success();
    }

    /**
     * 礼包商品详情
     *
     * @param id 主键ID
     * @return 礼包出参
     */
    @GetMapping("/goods")
    public PlatformResult<PackGoodsRes> packGoods(@RequestParam("id") Long id) {
        return PlatformResult.success(packGoodsDomain.packGoodsVO(id));
    }

    /**
     * 礼包商品分页
     *
     * @param query 分页查询
     * @return 礼包分页
     */
    @PostMapping("/goods/page")
    public PlatformResult<Page<PackGoodsRes>> packGoodsPage(@RequestBody PackGoodsQuery query) {
        return PlatformResult.success(packGoodsDomain.packGoodsVOList(query));
    }

    /**
     * 礼包订单预创建
     *
     * @param command 预创建入参
     * @return 预单出参
     */
    @PostMapping("order/pre")
    public PlatformResult<PackOrderPreRes> packOrderPreSave(@Valid @RequestBody PackOrderCommand command) {
        return PlatformResult.success(packOrderService.packOrderCreate(command));
    }

    /**
     * 礼包订单提交
     *
     * @param idListCommand ID 入参（取首位）
     * @return 订单ID
     */
    @PostMapping("order/submit")
    public PlatformResult<Long> packOrderSubmit(@Valid @RequestBody IdCommand idListCommand) {
        return PlatformResult.success(packOrderService.packOrderSubmit(idListCommand.getId()));
    }

    /**
     * 礼包订单详情
     *
     * @param id 订单ID
     * @return 订单出参
     */
    @GetMapping("order")
    public PlatformResult<PackOrderRes> packOrder(@RequestParam("id") Long id) {
        return PlatformResult.success(packOrderDomain.packOrderVO(id));
    }

    /**
     * 礼包订单分页
     *
     * @param query 分页查询
     * @return 订单分页
     */
    @PostMapping("order/page")
    public PlatformResult<Page<PackOrderRes>> packOrderPage(@RequestBody PackOrderQuery query) {
        AccountEnum.Identity identity = SecurityUtils.getIdentity();
//        if (RoleEnum.CompanyRole.SELECTOR == role) {
//            query.setAccountId(SecurityUtils.getAccountId());
        if (AccountEnum.Identity.PLATFORM != identity) {
            throw new PlatformException(BaseErrorCode.NOT_SERVICE);
        }
        return PlatformResult.success(packOrderDomain.packOrderVOList(query));
    }

    /**
     * 礼包订单发货
     *
     * @param command 发货入参
     * @return 空结果
     */
    @PostMapping("order/deliver")
    public PlatformResult<Void> packOrderDeliver(@Valid @RequestBody PackOrderDeliverCommand command) {
        packOrderDomain.packOrderDeliver(command);
        return PlatformResult.success();
    }

    /**
     * 礼包订单支付
     *
     * @param payReq 支付入参
     * @return 支付结果
     */
    @PostMapping("order/pay")
    public PlatformResult<PayResultDTO> packOrderPay(@Valid @RequestBody PackOrderPayReq payReq) {
        return PlatformResult.success(packOrderService.packOrderPay(payReq));
    }
}
