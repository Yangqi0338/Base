package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.action.cmd.CountCmd;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.model.req.ChannelQuery;
import com.newzkl.platform.base.biz.account.model.req.DealerQuery;
import com.newzkl.platform.base.biz.account.model.req.SelectorQuery;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.biz.account.model.res.AccountOutRes;
import com.newzkl.platform.base.biz.account.model.res.UserCountRes;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.biz.account.model.vo.DealerVO;
import com.newzkl.platform.base.biz.account.model.vo.NameAuthVO;
import com.newzkl.platform.base.biz.account.model.vo.SelectorSupplierVO;
import com.newzkl.platform.base.biz.account.model.vo.SelectorVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户-重构适配前端接口
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.AdapterController}。
 * 类级路径沿用旧契约 {@code /user}, 方法级自带二级段 {@code /channel|/selector|/count}, 不做合并改写。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AdapterController {

    private final AccountService accountService;
    private final UserQueryService userQueryService;
    private final ChannelClientDomain channelClientDomain;

    /**
     * 渠道商详情 for admin
     *
     * <p>迁移补充: 旧实现注入 {@code ChannelController} 直接转调其 {@code channel(id)},
     * controller 互调属实现细节, 此处改为直接查询。</p>
     *
     * <p>🔴 <b>勘误(2026-07-30)</b>: 原注「出参结构不变」<b>不成立</b>。经
     * {@code tools/out-dto-diff.mjs} 与旧 VO 机械比对, {@code ChannelVO} 相对旧契约有
     * 改名 1 处({@code realname}→{@code realName})、结构塌缩 1 处
     * ({@code roleId}+{@code roleName}→{@code role} 枚举)、字段缺失 8 个。
     * 用户裁决 <b>后端不做兼容映射, 由前端改</b>。完整清单见 {@code ChannelVO} 类级 javadoc。</p>
     *
     * <p>⚠️ 前端未改前, {@code yys-admin/src/views/user/channelDetails.vue:266} 的
     * {@code result.realname.replace(...)} 会抛 TypeError 导致整页白屏(非静默空值)。</p>
     *
     * @param id 渠道商账号ID
     * @return 渠道商视图, 字段名与旧契约有差异, 见上
     */
    @GetMapping("/channel/channelForAdmin")
    public PlatformResult<ChannelVO> channelForAdmin(@RequestParam("id") Long id) {
        return PlatformResult.success(channelClientDomain.channel(id));
    }

    /**
     * 甄选师提交实名认证信息
     *
     * @param nameAuthVO 实名认证信息
     * @return 空结果
     */
    @PostMapping("/selector/submitNameAuthInfo")
    public PlatformResult<Void> submitNameAuthInfo(@RequestBody NameAuthVO nameAuthVO) {
        accountService.submitNameAuthInfo(nameAuthVO);
        return PlatformResult.success();
    }

    /**
     * 甄选师查看供应商列表
     *
     * @param supplierQuery 供应商查询
     * @return 甄选师视角供应商分页
     */
    @PostMapping("/selector/selectorSupplierPageVO")
    public PlatformResult<Page<SelectorSupplierVO>> selectorSupplierPageVO(@RequestBody SupplierQuery supplierQuery) {
        supplierQuery.setInviteId(SecurityUtils.getAccountId());
        return PlatformResult.success(userQueryService.selectorSupplierVO(supplierQuery));
    }

    /**
     * 分组统计
     *
     * <p>迁移补充: 旧实现用匿名 {@code Function} 逐字段拷贝一份 {@code UserCountRes} 再挂分组数据,
     * 语义等价于取统计结果后补 {@code groupCountRes}, 此处保留语义, 去掉冗余拷贝。</p>
     *
     * @param timeQuery 时间范围查询
     * @return 用户统计结果
     */
    @PostMapping("/count/groupCount")
    public PlatformResult<UserCountRes> indexCount(@RequestBody TimeQuery timeQuery) {
        UserCountRes userCountRes = userQueryService.userCount();
        userCountRes.setGroupCountRes(userQueryService.groupCount(timeQuery));
        return PlatformResult.success(userCountRes);
    }

    /**
     * 数量统计
     *
     * @return 用户统计结果
     */
    @PostMapping("/count/userCount")
    public PlatformResult<UserCountRes> userCount() {
        return PlatformResult.success(userQueryService.userCount());
    }

    /**
     * 角色详情
     *
     * @param userAccount 角色详情入参
     * @return 对应角色的详情视图
     */
    @PostMapping("/count/userAccount")
    public PlatformResult<?> userAccount(@RequestBody CountCmd.UserAccount userAccount) {
        if (RoleEnum.CompanyRole.SUPPLIER.getCode().equals(userAccount.getRoleId())) {
            return PlatformResult.success(userQueryService.supplierVO(userAccount.getAccountId()));
        } else if (RoleEnum.CompanyRole.DEALER.getCode().equals(userAccount.getRoleId())) {
            return PlatformResult.success(userQueryService.dealerVO(userAccount.getAccountId()));
        } else if (RoleEnum.CompanyRole.CHANNEL.getCode().equals(userAccount.getRoleId())) {
            return PlatformResult.success(channelClientDomain.channel(userAccount.getAccountId()));
        } else if (RoleEnum.CompanyRole.SELECTOR.getCode().equals(userAccount.getRoleId())) {
            return PlatformResult.success(userQueryService.selectorVO(userAccount.getAccountId()));
        } else {
            ThrowsException.exception(BaseErrorCode.PARAM);
            return null;
        }
    }

    /**
     * 供应商列表
     *
     * @param supplierPage 供应商列表入参
     * @return 供应商列表
     */
    @PostMapping("/count/supplierPage")
    public PlatformResult<List<SupplierVO>> supplierPage(@RequestBody CountCmd.SupplierPage supplierPage) {
        SupplierQuery supplierQuery = new SupplierQuery();
        supplierQuery.setInviteId(supplierPage.getInviteId());
        return PlatformResult.success(userQueryService.operatorSupplierPage(supplierQuery).getRecords());
    }

    /**
     * 渠道商列表
     *
     * <p>迁移补充: 旧实现按 {@code upDealerId} 过滤, 中台 {@code ChannelQuery} 无该筛选字段,
     * 见迁移报告「数据隔离降级」。</p>
     *
     * @param channelPage 渠道商列表入参
     * @return 渠道商列表
     */
    @PostMapping("/count/channelPage")
    public PlatformResult<List<ChannelVO>> channelPage(@RequestBody CountCmd.ChannelPage channelPage) {
        ChannelQuery channelQuery = new ChannelQuery();
        return PlatformResult.success(userQueryService.channelPage(channelQuery).getRecords());
    }

    /**
     * 甄选师分页
     *
     * @param selectorPage 甄选师列表入参
     * @return 甄选师列表
     */
    @PostMapping("/count/selectorPage")
    public PlatformResult<List<SelectorVO>> selectorPage(@RequestBody CountCmd.SelectorPage selectorPage) {
        SelectorQuery selectorQuery = new SelectorQuery();
        selectorQuery.setInviteId(selectorPage.getInviteId());
        return PlatformResult.success(userQueryService.selectorPage(selectorQuery).getRecords());
    }

    /**
     * 交易师分页
     *
     * @param dealerPage 交易师列表入参
     * @return 交易师列表
     */
    @PostMapping("/count/dealerPage")
    public PlatformResult<List<DealerVO>> dealerPage(@RequestBody CountCmd.DealerPage dealerPage) {
        DealerQuery dealerQuery = new DealerQuery();
        dealerQuery.setOperatorId(dealerPage.getInviteId());
        return PlatformResult.success(userQueryService.dealerPage(dealerQuery).getRecords());
    }

    /**
     * 账号详情
     *
     * @param id 账号ID入参
     * @return 账号外部视图
     */
    @PostMapping("/count/accountDetail")
    public PlatformResult<AccountOutRes> accountDetail(@RequestBody CountCmd.ID id) {
        return PlatformResult.success(userQueryService.accountOutVO(SecurityUtils.getClient(), id.getAccountId()));
    }
}
