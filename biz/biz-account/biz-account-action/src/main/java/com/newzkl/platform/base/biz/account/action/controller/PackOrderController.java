package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.pack.service.PackOrderService;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PayResultDTO;
import com.newzkl.platform.base.biz.account.domain.service.PackOrderDomain;
import com.newzkl.platform.base.biz.account.model.pack.query.PackOrderQuery;
import com.newzkl.platform.base.biz.account.model.pack.req.PackOrderCommand;
import com.newzkl.platform.base.biz.account.model.pack.req.PackOrderDeliverCommand;
import com.newzkl.platform.base.biz.account.model.pack.req.PackOrderPayReq;
import com.newzkl.platform.base.biz.account.model.pack.res.PackOrderPreRes;
import com.newzkl.platform.base.biz.account.model.pack.res.PackOrderRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
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
 * 平台-入会礼包订单
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.interfaces.controller.PackOrderController}。
 * 类级路径与方法级路径逐字沿用旧契约。</p>
 *
 * <p>迁移说明：</p>
 * <ul>
 *   <li>旧构造注入 {@code IPackOrderRepository}（越层，实际未用）本仓移除，只留
 *       {@link PackOrderService}（跨域编排）+ {@link PackOrderDomain}（域读/发货）。</li>
 *   <li>{@code packOrderSubmit} 旧收 {@code IdObj}，本仓换通用 {@code IdListCommand}
 *       （取首位 {@code getId()}）。</li>
 *   <li>{@code packOrderPage} 旧回 {@code PageInfo<PackOrderVO>}，本仓保留 {@code Page} 分页壳直返。
 *       <b>前端契约变</b>（分页入参 current/size → pageNo/pageSize；出参 list → records）。</li>
 *   <li>{@code packOrderDeliver} 旧返空体 {@code ScmResult<Long>}，本仓 {@code PlatformResult<Void>}。</li>
 *   <li>角色分流沿源：{@code SELECTOR} 仅看本账号；{@code PLATFORM} 不限；其余抛
 *       {@code NOT_SERVICE}。{@code SecurityUtils.getRole()} 直返 {@code RoleEnum.CompanyRole}。</li>
 *   <li>{@code TODO[auth-defer]}：旧 {@code @Limit(FuncCons.Admin.pack_order)} 鉴权注解暂缺，
 *       待入口 starter 鉴权基建接入。</li>
 * </ul>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/packOrder")
@Validated
@RequiredArgsConstructor
public class PackOrderController {

    private final PackOrderService packOrderService;

    private final PackOrderDomain packOrderDomain;

    /**
     * 礼包订单预创建
     *
     * @param command 预创建入参
     * @return 预单出参
     */
    @PostMapping("packOrderPreSave")
    public PlatformResult<PackOrderPreRes> packOrderPreSave(@Valid @RequestBody PackOrderCommand command) {
        return PlatformResult.success(packOrderService.packOrderCreate(command));
    }

    /**
     * 礼包订单提交
     *
     * @param idListCommand ID 入参（取首位）
     * @return 订单ID
     */
    @PostMapping("packOrderSubmit")
    public PlatformResult<Long> packOrderSubmit(@Valid @RequestBody IdCommand idListCommand) {
        return PlatformResult.success(packOrderService.packOrderSubmit(idListCommand.getId()));
    }

    /**
     * 礼包订单详情
     *
     * @param id 订单ID
     * @return 订单出参
     */
    @GetMapping("packOrder")
    public PlatformResult<PackOrderRes> packOrder(@RequestParam("id") Long id) {
        return PlatformResult.success(packOrderDomain.packOrderVO(id));
    }

    /**
     * 礼包订单分页
     *
     * @param query 分页查询
     * @return 订单分页
     */
    @PostMapping("packOrderPage")
    public PlatformResult<Page<PackOrderRes>> packOrderPage(@RequestBody PackOrderQuery query) {
        RoleEnum.CompanyRole role = SecurityUtils.getRole();
//        if (RoleEnum.CompanyRole.SELECTOR == role) {
//            query.setAccountId(SecurityUtils.getAccountId());
        if (RoleEnum.CompanyRole.PLATFORM != role) {
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
    @PostMapping("packOrderDeliver")
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
    @PostMapping("packOrderPay")
    public PlatformResult<PayResultDTO> packOrderPay(@Valid @RequestBody PackOrderPayReq payReq) {
        return PlatformResult.success(packOrderService.packOrderPay(payReq));
    }
}
