package com.newzkl.platform.base.biz.order.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.action.cmd.OrderCmd;
import com.newzkl.platform.base.biz.order.action.cmd.RefundCmd;
import com.newzkl.platform.base.biz.order.application.service.RefundService;
import com.newzkl.platform.base.biz.order.domain.service.RefundDomain;
import com.newzkl.platform.base.biz.order.model.enums.order.ExpressEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.order.model.order.req.ApplyPlatformReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundReq;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundFreightVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.EasyExcelUtil;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 交易-售后控制器
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.interfaces.controller.RefundController},
 * 15 端点路径与 HTTP 方法逐字保持不变。偏离说明:</p>
 * <ul>
 *   <li>旧 controller 直连 {@code IRefundRepository} (跨层), 新版改注入 {@code RefundDomain}。</li>
 *   <li>分页出参由 {@code PageInfo} 改为 MyBatis-Plus {@code Page} (前端分页壳字段变化)。</li>
 *   <li>{@code SecurityUtils.getRole()} 已不存在, 改用 {@code getRoleId()} 与
 *       {@code RoleEnum.CompanyRole#getCode()} 比较。</li>
 *   <li>旧 {@code @Limit(FuncCons...)} 权限点不在本层声明, 鉴权切面归入口 starter (鉴权降级)。</li>
 *   <li>SPU 订单标识随 Base 交易域改造由主键 ID 改为业务单号 (String)。</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/sale/refund")
@RequiredArgsConstructor
public class RefundController {

    /**
     * 商户角色编码
     *
     * <p>旧 {@code RoleEnum.CompanyRole.MERCHANT(1007L)} 在 Base 通用枚举中尚未收录,
     * 此处以常量保留旧判定值, 保证分页收窄逻辑与旧行为一致。</p>
     */
    private static final Long ROLE_MERCHANT = 1007L;

    private final RefundService refundService;

    private final RefundDomain refundDomain;

    /**
     * C端创建售后
     *
     * @param refundCommand 售后创建入参
     * @return 售后单 ID
     */
    @PostMapping("refundCreate")
    public PlatformResult<Long> refundCreate(@Validated @RequestBody RefundReq refundCommand) {
        refundCommand.setRole(RoleEnum.CompanyRole.MEMBER);
        Long refundId = refundService.refundCreate(refundCommand);
        return PlatformResult.success(refundId);
    }

    /**
     * C端取消售后
     *
     * @param idObj 售后单ID
     * @return 成功结果
     */
    @PostMapping("memberCancelRefund")
    public PlatformResult<Void> memberCancelRefund(@Validated @RequestBody OrderCmd.ID idObj) {
        refundDomain.stopAudit(RoleEnum.CompanyRole.MEMBER, SecurityUtils.getAccountId(), idObj.getId());
        return PlatformResult.success();
    }

    /**
     * 渠道商审核售后单
     *
     * @param audit 审核入参
     * @return 成功结果
     */
    @PostMapping("channelAudit")
    public PlatformResult<Void> channelAudit(@Validated @RequestBody RefundCmd.Audit audit) {
        refundService.channelAudit(audit.getRefundId(), audit.getSpuOrderId(), audit.getExecute(),
                audit.getReason(), false);
        return PlatformResult.success();
    }

    /**
     * 渠道商取消售后
     *
     * @param idObj 售后单ID
     * @return 成功结果
     */
    @PostMapping("channelCancelRefund")
    public PlatformResult<Void> channelCancelRefund(@Validated @RequestBody OrderCmd.ID idObj) {
        refundDomain.stopAudit(RoleEnum.CompanyRole.CHANNEL, SecurityUtils.getAccountId(), idObj.getId());
        return PlatformResult.success();
    }

    /**
     * 渠道商确认收货
     *
     * @param idObj 售后单ID
     * @return 成功结果
     */
    @PostMapping("merchantConfirmRefundFreight")
    public PlatformResult<Void> merchantConfirmRefundFreight(@Validated @RequestBody OrderCmd.ID idObj) {
        refundService.merchantConfirmRefundFreight(idObj.getId());
        return PlatformResult.success();
    }

    /**
     * 渠道商拒绝收货
     *
     * @param idObj 售后单ID
     * @return 成功结果
     */
    @PostMapping("merchantRefuseRefundFreight")
    public PlatformResult<Void> merchantRefuseRefundFreight(@Validated @RequestBody OrderCmd.ID idObj) {
        refundDomain.refuseRefundFreight(idObj.getId());
        return PlatformResult.success();
    }

    /**
     * 供应商审核售后单
     *
     * @param audit 审核入参
     * @return 成功结果
     */
    @PostMapping("supplierAudit")
    public PlatformResult<Void> supplierAudit(@Validated @RequestBody RefundCmd.Audit audit) {
        refundService.supplierAudit(audit.getRefundId(), audit.getExecute(), false);
        return PlatformResult.success();
    }

    /**
     * 提交退货物流
     *
     * @param refundFreightVO 退货物流入参
     * @return 成功结果
     */
    @PostMapping("submitRefundFreight")
    public PlatformResult<Void> submitRefundFreight(@Validated @RequestBody RefundFreightVO refundFreightVO) {
        refundDomain.submitRefundFreight(refundFreightVO);
        return PlatformResult.success();
    }

    /**
     * 供应商确认收货
     *
     * @param idObj 售后单ID
     * @return 成功结果
     */
    @PostMapping("supplierConfirmRefundFreight")
    public PlatformResult<Void> supplierConfirmRefundFreight(@Validated @RequestBody OrderCmd.ID idObj) {
        refundService.supplierConfirmRefundFreight(idObj.getId());
        return PlatformResult.success();
    }

    /**
     * 供应商拒绝收货
     *
     * @param idObj 售后单ID
     * @return 成功结果
     */
    @PostMapping("supplierRefuseRefundFreight")
    public PlatformResult<Void> supplierRefuseRefundFreight(@Validated @RequestBody OrderCmd.ID idObj) {
        refundDomain.refuseRefundFreight(idObj.getId());
        return PlatformResult.success();
    }

    /**
     * 申请平台介入
     *
     * @param applyPlatformCommand 平台介入申请入参
     * @return 成功结果
     */
    @PostMapping("applyPlatform")
    public PlatformResult<Void> applyPlatform(@Validated @RequestBody ApplyPlatformReq applyPlatformCommand) {
        refundDomain.applyPlatform(applyPlatformCommand);
        return PlatformResult.success();
    }

    /**
     * 平台介入处理
     *
     * @param platformExecute 平台介入处理入参
     * @return 成功结果
     */
    @PostMapping("platformExecute")
    public PlatformResult<Void> platformExecute(@Validated @RequestBody RefundCmd.PlatformExecute platformExecute) {
        refundDomain.platformExecute(platformExecute.getRefundId(), platformExecute.getExecute());
        return PlatformResult.success();
    }

    /**
     * 售后单详情
     *
     * @param refundId 售后单 ID
     * @return 售后单视图对象
     */
    @GetMapping("refund")
    public PlatformResult<RefundVO> refund(@RequestParam("id") Long refundId) {
        RefundVO refund = refundDomain.refundVO(refundId);
        return PlatformResult.success(refund);
    }

    /**
     * 所有物流公司
     *
     * @return 物流公司键值列表
     */
    @GetMapping("allExpress")
    public PlatformResult<List<Map<String, String>>> allExpress() {
        List<Map<String, String>> expressTypeMapList = ExpressEnum.ExpressType.listAllWithMap();
        return PlatformResult.success(expressTypeMapList);
    }

    /**
     * 根据 BySpuOrderId 查售后单详情
     *
     * @param spuOrderId SPU 交易单号 (旧契约字段名保留)
     * @return 售后单视图对象
     */
    @GetMapping("refundBySpuOrderId")
    public PlatformResult<RefundVO> refundVoBySpuOrderId(@RequestParam("spuOrderId") String spuOrderId) {
        RefundVO refund = refundDomain.refundVoBySpuOrderId(spuOrderId);
        return PlatformResult.success(refund);
    }

    /**
     * 售后单分页 (按登录角色自动收窄查询范围)
     *
     * @param refundQuery 售后单查询
     * @return 售后单分页
     */
    @PostMapping("refundPage")
    public PlatformResult<Page<RefundVO>> refundPage(@RequestBody RefundPageReq refundQuery) {
        appendRefundQuery(refundQuery);
        Page<RefundVO> listRefund = refundDomain.refundVOList(refundQuery);
        return PlatformResult.success(listRefund);
    }

    /**
     * 售后单导出
     *
     * @param refundQuery 售后单查询
     * @throws IOException 写出流失败
     */
    @PostMapping("exportRefund")
    public void exportRefund(@RequestBody RefundPageReq refundQuery) throws IOException {
        appendRefundQuery(refundQuery);
        List<RefundExcelVO> data = refundDomain.exportRefund(refundQuery);
        EasyExcelUtil.export(data, "售后单");
    }

    /**
     * 按登录角色收窄售后单查询条件 (平台角色不额外收窄, 未知角色直接拒绝)
     *
     * @param refundQuery 售后单查询 (原地写入)
     */
    private static void appendRefundQuery(RefundPageReq refundQuery) {
        Long roleId = SecurityUtils.getRoleId();
        if (RoleEnum.CompanyRole.CHANNEL.getCode().equals(roleId)) {
            refundQuery.setChannelId(SecurityUtils.getAccountId());
        } else if (RoleEnum.CompanyRole.PLATFORM.getCode().equals(roleId)) {
            // 旧逻辑: 平台角色不追加任何过滤条件
        } else if (RoleEnum.CompanyRole.SUPPLIER.getCode().equals(roleId)) {
            refundQuery.setSupplierId(SecurityUtils.getAccountId());
            refundQuery.setFromOrderStateNot(Collections.singletonList(OrderEnum.State.SENDING.getCode()));
        } else if (ROLE_MERCHANT.equals(roleId)) {
            refundQuery.setMerchantId(SecurityUtils.getAccountId());
        } else if (RoleEnum.CompanyRole.MEMBER.getCode().equals(roleId)) {
            refundQuery.setMemberId(SecurityUtils.getAccountId());
        } else {
            ThrowsException.exception(BaseErrorCode.NOT_SERVICE);
        }
    }
}
