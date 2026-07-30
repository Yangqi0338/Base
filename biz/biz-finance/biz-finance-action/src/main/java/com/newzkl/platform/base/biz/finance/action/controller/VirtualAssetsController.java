package com.newzkl.platform.base.biz.finance.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.virtual.service.VirtualAssetsDomain;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsQuery;
import com.newzkl.platform.base.biz.finance.model.virtual.query.VirtualAssetsRecordQuery;
import com.newzkl.platform.base.biz.finance.model.virtual.res.VirtualAssetsRecordRes;
import com.newzkl.platform.base.biz.finance.model.virtual.res.VirtualAssetsRes;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 虚拟资产控制器
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.finance.interfaces.purse.VirtualAssetsController}。
 * 旧出参为 {@code PageInfo}, 新出参统一降级为 {@code List} (分页壳由响应层承担)。
 * 旧 {@code FinanceEnum.FinanceUser.accountTypeByRoleId(SecurityUtils.getRole())} 在 Base 侧
 * 对应 {@code PurseEnum.FinanceUser.getByRole(SecurityUtils.getRoleId())}。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/virtualAssets")
@RequiredArgsConstructor
public class VirtualAssetsController {

    private final VirtualAssetsDomain virtualAssetsDomain;

    /**
     * 查询虚拟资产
     *
     * <p>账户 id / 账户类型缺省时按当前登录态补齐, 避免越权查看他人资产。</p>
     *
     * @param query 查询入参
     * @return 虚拟资产列表
     */
    @PostMapping("/queryVirtualAssets")
    public PlatformResult<List<VirtualAssetsRes>> queryVirtualAssets(@RequestBody VirtualAssetsQuery query) {
        if (query.getAccountId() == null) {
            query.setAccountId(SecurityUtils.getAccountId());
        }
        if (query.getAccountType() == null) {
            query.setAccountType(PurseEnum.FinanceUser.getByRole(SecurityUtils.getRoleId()));
        }
        return PlatformResult.success(virtualAssetsDomain.queryVirtualAssets(query));
    }

    /**
     * 查询虚拟资产变动记录
     *
     * <p>账户 id / 账户类型缺省时按当前登录态补齐, 避免越权查看他人流水。</p>
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 调用方: yys-admin</p>
     *
     * @param query 查询入参
     * @return 变动记录分页
     */
    @PostMapping("/queryVirtualAssetsRecord")
    public PlatformResult<Page<VirtualAssetsRecordRes>> queryVirtualAssetsRecord(
            @RequestBody VirtualAssetsRecordQuery query) {
        if (query.getAccountId() == null) {
            query.setAccountId(SecurityUtils.getAccountId());
        }
        if (query.getAccountType() == null) {
            query.setAccountType(PurseEnum.FinanceUser.getByRole(SecurityUtils.getRoleId()));
        }
        return PlatformResult.success(virtualAssetsDomain.queryVirtualAssetsRecord(query));
    }
}
