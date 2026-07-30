package com.newzkl.platform.base.biz.finance.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.AccountContributeDomain;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.EarningDomain;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AccountContributeRpcQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AccountContributeRes;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AppEarningRecordRes;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.EarningRecordVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.TotalEarningVO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分润记录控制器
 *
 * <p>迁移自 new-scm {@code interfaces.earning.EarningController}, 类级路径 {@code /earningRecord} 逐字保留。</p>
 *
 * <p>注意: 源 {@code /queryAppEarningRecord} 已在 new-scm 侧标记 {@code @Deprecated}
 * (前端零引用, 2026-07-27 交叉比对), 本类中该端点为既有迁移产物, 未做删除,
 * 是否下线交由 Base 死接口生命周期 (rules/DeadEndpoint.md) 处置。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/earningRecord")
@RequiredArgsConstructor
public class EarningController {

    private final EarningDomain earningDomain;

    private final AccountContributeDomain accountContributeDomain;

    /**
     * 查询分润记录
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 调用方: gys-admin / platform-admin / yys-admin / zhaomu-app</p>
     *
     * @param req 分润记录查询
     * @return 分润记录分页
     */
    @PostMapping("/queryEarningRecord")
    public PlatformResult<Page<EarningRecordVO>> queryEarningRecord(@RequestBody EarningRecordQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
            req.setRoleId(RoleEnum.CompanyRole.getByCode(SecurityUtils.getRoleId()));
        }
        return PlatformResult.success(earningDomain.queryEarningRecord(req));
    }

    /**
     * APP 查询分润记录
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 调用方: zhaomu-app</p>
     *
     * @param req 分润记录查询
     * @return APP 分润记录分页
     */
    @PostMapping("/queryAppEarningRecord")
    public PlatformResult<Page<AppEarningRecordRes>> queryAppEarningRecord(@RequestBody EarningRecordQuery req) {
        if (req.getAccountId() == null) {
            req.setAccountId(SecurityUtils.getAccountId());
        }
        return PlatformResult.success(earningDomain.queryAppEarningRecord(req));
    }

    /**
     * 查询累计分润数据
     *
     * @return 累计分润数据
     */
    @PostMapping("/queryTotalEarning")
    public PlatformResult<TotalEarningVO> queryTotalEarning() {
        return PlatformResult.success(earningDomain.queryTotalEarning());
    }

    /**
     * 查询客户待分润金额
     *
     * <p>源实现按当前登录账号 + 角色查 consumeType=商品 且待结算的分润金额,
     * Base 对应能力为 {@code EarningDomain.queryAccountGoodsWaitEarningAmount}, 查询口径一致。</p>
     *
     * @return 待分润金额
     */
    @PostMapping("/queryAccountWaitEarningAmount")
    public PlatformResult<Integer> queryAccountWaitEarningAmount() {
        EarningRecordQuery req = new EarningRecordQuery();
        req.setAccountId(SecurityUtils.getAccountId());
        req.setRoleId(RoleEnum.CompanyRole.getByCode(SecurityUtils.getRoleId()));
        return PlatformResult.success(earningDomain.queryAccountGoodsWaitEarningAmount(req));
    }

    /**
     * 批量查询客户贡献值
     *
     * @param req 批量贡献值查询
     * @return 客户贡献数据列表
     */
    @PostMapping("/batchQueryAccountContribute")
    public PlatformResult<List<AccountContributeRes>> batchQueryAccountContribute(@RequestBody AccountContributeRpcQuery req) {
        return PlatformResult.success(accountContributeDomain.batchQueryAccountContribute(req));
    }
}
