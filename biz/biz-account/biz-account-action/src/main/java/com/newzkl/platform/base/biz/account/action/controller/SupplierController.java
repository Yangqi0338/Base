package com.newzkl.platform.base.biz.account.action.controller;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.req.SupplierCmd;
import com.newzkl.platform.base.biz.account.model.req.SupplierReq;
import com.newzkl.platform.base.biz.account.model.dto.SupplierDTO;
import com.newzkl.platform.base.biz.account.model.res.SupplierRes;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用户-供应商
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.SupplierController}。
 * 类级路径与方法级路径逐字沿用旧契约, 含旧代码中同一控制器内混用前导斜杠的写法
 * (如 {@code supplierBaseEdit} 与 {@code /periodSet})。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/supplier")
@RequiredArgsConstructor
@FuncPermission("用户-供应商")
public class SupplierController {

    private final SupplierClientDomain supplierClientDomain;
    private final UserQueryService userQueryService;

    /**
     * 供应商修改
     *
     * <p>保留旧鉴权语义: 供应商端强制改写为当前登录账号。
     * 旧 {@code @Limit(code=1022, level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param edit 供应商修改请求
     * @return 空结果
     * @ext 主数据 supplier, 副数据 account(单副, 副数据不再向下关联)。{@code username}
     *      落 account 表, 其余字段落 supplier 表
     */
    @PostMapping("supplierBaseEdit")
    @FuncPermission("供应商修改")
    public PlatformResult<Void> supplierEdit(@Validated @RequestBody SupplierReq edit) {
        if (AccountEnum.Identity.SUPPLIER == SecurityUtils.getIdentity()) {
            edit.setId(SecurityUtils.getAccountId());
        }
        supplierClientDomain.supplierEdit(edit.getId(), edit);
        return PlatformResult.success();
    }

    /**
     * 供应商纯净详情
     *
     * <p>只需供应商自有列时走本端点, 相比 {@code supplier} 少一次 account 查询</p>
     *
     * @param id 供应商账号ID, 不传取当前登录账号
     * @return 供应商纯净视图
     * @ext 主数据 supplier (无副数据)
     */
    @GetMapping("/base")
    public PlatformResult<SupplierDTO> supplierBase(@RequestParam(value = "id", required = false) Long id) {
        return PlatformResult.success(supplierClientDomain.supplierBase(id == null ? SecurityUtils.getAccountId() : id));
    }

    /**
     * 供应商详情
     *
     * @param id 供应商账号ID, 不传取当前登录账号
     * @return 供应商聚合视图
     * @ext 主数据 supplier, 副数据 account(单副, 副数据不再向下关联)。方向与
     *      {@code AccountController.identityDetail}(主 account / 副身份) 相反, 两者不可互相替代
     */
    @GetMapping("supplier")
    public PlatformResult<SupplierRes> supplier(@RequestParam(value = "id", required = false) Long id) {
        return PlatformResult.success(supplierClientDomain.supplierDetail(id == null ? SecurityUtils.getAccountId() : id));
    }

    /**
     * 获取供应商转出限额
     *
     * <p>0 不限; 大于 0 为限额。</p>
     *
     * @return 转出限额
     */
    @GetMapping("limitAmount")
    public PlatformResult<Money> limitAmount() {
        return PlatformResult.success(supplierClientDomain.limitAmount(SecurityUtils.getAccountId()));
    }

    /**
     * 设置供应商账期
     *
     * <p>旧 {@code @Limit(code=1022, level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param periodSet 账期设置入参
     * @return 空结果
     */
    @PostMapping("/periodSet")
    @FuncPermission("设置供应商账期")
    public PlatformResult<Void> periodSet(@Validated @RequestBody SupplierCmd.PeriodSet periodSet) {
        supplierClientDomain.periodSet(periodSet.getId(), periodSet.getPeriodSetConfig());
        return PlatformResult.success();
    }

    /**
     * 供应商账期信息
     *
     * <p>保留旧语义: 未传 ID 时取当前登录账号。</p>
     *
     * @param idListCommand 供应商ID入参
     * @return 账期配置
     */
    @PostMapping("/periodInfo")
    public PlatformResult<SettlementConfigVO> periodInfo(@RequestBody IdCommand idListCommand) {
        Long id = CollUtil.getFirst(idListCommand.getIdList());
        if (id == null) {
            id = SecurityUtils.getAccountId();
        }
        return PlatformResult.success(userQueryService.supplierVO(id).getPeriodSetConfig());
    }

    /**
     * 供应商应付金额设置
     *
     * <p>旧 {@code @Limit(code=1022, level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param set 应付保证金设置入参
     * @return 空结果
     */
    @PostMapping("/shouldPromisePayAmountSet")
    @FuncPermission("供应商应付金额设置")
    public PlatformResult<Void> shouldPromisePayAmountSet(@Validated @RequestBody SupplierCmd.ShouldPromisePayAmountSet set) {
        supplierClientDomain.shouldPromisePayAmountSet(set.getId(), set.getShouldPromisePayAmount(), set.getPromisePayConfig());
        return PlatformResult.success();
    }

    /**
     * 供应商应付金额信息
     *
     * @param idListCommand 供应商ID入参
     * @return 应付保证金金额
     */
    @PostMapping("/shouldPromisePayAmountInfo")
    public PlatformResult<Money> shouldPromisePayAmountInfo(@RequestBody IdCommand idListCommand) {
        Long id = CollUtil.getFirst(idListCommand.getIdList());
        return PlatformResult.success(userQueryService.supplierVO(id).getShouldPromisePayAmount());
    }

    /**
     * 添加供应商行业
     *
     * <p>保留旧语义: 未传 {@code accountId} 时取当前登录账号。
     * 旧 {@code @Limit(code={1022,1024}, level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param addIndustry 行业追加入参
     * @return 空结果
     */
    @PostMapping("/addIndustry")
    @FuncPermission("添加供应商行业")
    public PlatformResult<Void> addIndustry(@Validated @RequestBody SupplierCmd.AddIndustry addIndustry) {
        if (addIndustry.getAccountId() == null) {
            addIndustry.setAccountId(SecurityUtils.getAccountId());
        }
        supplierClientDomain.addIndustry(addIndustry.getAccountId(), addIndustry.getIndustryId());
        return PlatformResult.success();
    }
}
