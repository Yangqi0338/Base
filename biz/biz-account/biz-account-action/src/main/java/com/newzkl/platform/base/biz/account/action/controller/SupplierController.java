package com.newzkl.platform.base.biz.account.action.controller;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.req.SupplierCmd;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.biz.account.model.req.SupplierReq;
import com.newzkl.platform.base.biz.account.model.res.SupplierRes;
import com.newzkl.platform.base.biz.account.model.vo.SupplierDescVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     */
    @PostMapping("supplierBaseEdit")
    public PlatformResult<Void> supplierEdit(@Validated @RequestBody SupplierReq edit) {
        if (RoleEnum.CompanyRole.SUPPLIER == SecurityUtils.getRole()) {
            edit.setId(SecurityUtils.getAccountId());
        }
        supplierClientDomain.supplierEdit(edit.getId(), edit);
        return PlatformResult.success();
    }

    /**
     * 供应商删除
     *
     * <p>旧 {@code @Limit(code=1022, level=set)} 未迁移, 见迁移报告「鉴权降级」。</p>
     *
     * @param supplierIdList 供应商账号ID列表
     * @return 空结果
     * @deprecated 前端零引用, 已确认死端点 (2026-07-27 交叉比对); 仅为契约完整性迁入
     */
    @Deprecated
    @PostMapping("supplierDelete")
    public PlatformResult<Void> supplierDelete(@Validated @RequestBody SupplierCmd.IDList supplierIdList) {
        supplierClientDomain.supplierDelete(supplierIdList.getSupplierIdList());
        return PlatformResult.success();
    }

    /**
     * 供应商详情
     *
     * @param id 供应商账号ID
     * @return 供应商视图
     */
    @GetMapping("supplier")
    public PlatformResult<SupplierVO> supplier(@RequestParam("id") Long id) {
        return PlatformResult.success(userQueryService.supplierVO(id));
    }

    /**
     * 获取供应商转出限额
     *
     * <p>0 不限; 大于 0 为限额。</p>
     *
     * @return 转出限额
     */
    @GetMapping("limitAmount")
    public PlatformResult<Integer> limitAmount() {
        return PlatformResult.success(supplierClientDomain.limitAmount(SecurityUtils.getAccountId()));
    }

    /**
     * 供应商分页
     *
     * <p>迁移补充: 旧 {@code IUserQueryService.supplierPage} 中台化后落在
     * {@link SupplierClientDomain#supplierPage}, 记录类型改为出参对象
     * {@code SupplierRes}; 旧实现取了 {@code accountId} 但未使用, 已去掉。</p>
     *
     * @param supplierQuery 供应商查询
     * @return 供应商分页
     */
    @PostMapping("supplierPageVO")
    public PlatformResult<Page<SupplierRes>> supplierPage(@RequestBody SupplierQuery supplierQuery) {
        return PlatformResult.success(supplierClientDomain.supplierPage(supplierQuery));
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
    public PlatformResult<String> periodInfo(@RequestBody IdCommand idListCommand) {
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
    public PlatformResult<Integer> shouldPromisePayAmountInfo(@RequestBody IdCommand idListCommand) {
        Long id = CollUtil.getFirst(idListCommand.getIdList());
        Money amount = userQueryService.supplierVO(id).getShouldPromisePayAmount();
        return PlatformResult.success(amount == null ? null : (int) amount.getCent());
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
    public PlatformResult<Void> addIndustry(@Validated @RequestBody SupplierCmd.AddIndustry addIndustry) {
        if (addIndustry.getAccountId() == null) {
            addIndustry.setAccountId(SecurityUtils.getAccountId());
        }
        supplierClientDomain.addIndustry(addIndustry.getAccountId(), addIndustry.getIndustryId());
        return PlatformResult.success();
    }

    /**
     * 供应商描述信息
     *
     * @param command 供应商ID列表
     * @return 供应商描述列表
     */
    @PostMapping("supplierDescVOList")
    public PlatformResult<List<SupplierDescVO>> supplierDescVOList(@RequestBody SupplierCmd.IDList command) {
        return PlatformResult.success(userQueryService.supplierDescVOList(command.getSupplierIdList()));
    }
}
