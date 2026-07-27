package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.action.cmd.CountCmd;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.ChannelQuery;
import com.newzkl.platform.base.biz.account.model.req.DealerQuery;
import com.newzkl.platform.base.biz.account.model.req.SelectorQuery;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.biz.account.model.res.AccountInfo;
import com.newzkl.platform.base.biz.account.model.res.AccountOutRes;
import com.newzkl.platform.base.biz.account.model.res.SupplierRes;
import com.newzkl.platform.base.biz.account.model.res.UserCountRes;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.biz.account.model.vo.DealerVO;
import com.newzkl.platform.base.biz.account.model.vo.NameAuthVO;
import com.newzkl.platform.base.biz.account.model.vo.SelectorSupplierVO;
import com.newzkl.platform.base.biz.account.model.vo.SelectorVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 用户-旧前端适配控制器 (防腐层)。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.AdapterController}, 路径 {@code /user}
 * 与 11 个端点的 HTTP 方法保持不变。本层<b>不含新业务</b>, 全部是对已迁领域 / 应用能力的转调
 * 与旧出参形态的组装。</p>
 *
 * <p>迁移要点:</p>
 * <ul>
 *   <li>旧控制器注入的 {@code IDictFacade dictFacade} 经全文核对<b>零调用点</b>, 故不迁移,
 *       也不为其建出站端口。</li>
 *   <li>旧 {@code IUserQueryService.accountOutVO(Long)} 单参重载在中台不存在 (改为按端 + ID 查询),
 *       故本层先按 ID 取账号所属端再调 {@code accountOutVO(client, id)}。</li>
 *   <li>旧 {@code IUserQueryService.supplierPage / channelVO} 在中台不存在同名方法, 分别改调
 *       {@link SupplierClientDomain#supplierPage} 与 {@link ChannelClientDomain#channel}。</li>
 *   <li>旧 {@code ChannelQuery.upDealerId} 字段在中台合并为 {@code invitedId} (上级 ID), 语义一致。</li>
 *   <li>分页出参一律为 MyBatis-Plus {@link Page} (旧为 PageHelper {@code PageInfo});
 *       原本就返回列表的端点仍返回列表。</li>
 * </ul>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class AdapterController {

    private final ChannelController channelController;
    private final AccountService accountService;
    private final UserQueryService userQueryService;
    private final AccountDomain accountDomain;
    private final ChannelClientDomain channelClientDomain;
    private final SupplierClientDomain supplierClientDomain;

    /**
     * 渠道商详情 (平台侧)。
     *
     * <p>与旧实现一致: 直接委托给 {@link ChannelController#channel(Long)}。</p>
     *
     * @param id 渠道商账号 ID
     * @return 渠道商详情
     */
    @GetMapping("/channel/channelForAdmin")
    public PlatformResult<ChannelVO> channelForAdmin(@RequestParam("id") Long id) {
        return channelController.channel(id);
    }

    /**
     * 甄选师提交实名认证信息。
     *
     * @param nameAuthVO 实名认证信息
     * @return 成功结果
     */
    @PostMapping("/selector/submitNameAuthInfo")
    public PlatformResult<Void> submitNameAuthInfo(@RequestBody NameAuthVO nameAuthVO) {
        accountService.submitNameAuthInfo(nameAuthVO);
        return PlatformResult.success();
    }

    /**
     * 甄选师查看供应商分页。
     *
     * <p>保留旧语义: 强制以当前登录账号作为供应商邀请人过滤条件 (分页数据范围, 故留在本层)。</p>
     *
     * @param supplierQuery 供应商查询
     * @return 供应商分页
     */
    @PostMapping("/selector/selectorSupplierPageVO")
    public PlatformResult<Page<SelectorSupplierVO>> selectorSupplierPageVO(@RequestBody SupplierQuery supplierQuery) {
        supplierQuery.setInviteId(SecurityUtils.getAccountId());
        return PlatformResult.success(userQueryService.selectorSupplierVO(supplierQuery));
    }

    /**
     * 用户分组统计。
     *
     * <p>保留旧语义: 仅复制 6 个人数字段 (丢弃统计对象上可能已有的分组数据), 再挂入按时间维度的分组统计。
     * 旧实现在人数统计为 null 时会空指针, 本实现补空守卫返回全空的统计对象。</p>
     *
     * @param timeQuery 时间维度查询
     * @return 用户统计 (含分组)
     */
    @PostMapping("/count/groupCount")
    public PlatformResult<UserCountRes> indexCount(@RequestBody TimeQuery timeQuery) {
        UserCountRes source = userQueryService.userCount();
        UserCountRes result = new UserCountRes();
        if (source != null) {
            result.setAccountCount(source.getAccountCount());
            result.setChannelCount(source.getChannelCount());
            result.setSupplierCount(source.getSupplierCount());
            result.setSelectorCount(source.getSelectorCount());
            result.setDealerCount(source.getDealerCount());
            result.setOperatorCount(source.getOperatorCount());
        }
        result.setGroupCountRes(userQueryService.groupCount(timeQuery));
        return PlatformResult.success(result);
    }

    /**
     * 用户数量统计。
     *
     * @return 用户统计
     */
    @PostMapping("/count/userCount")
    public PlatformResult<UserCountRes> userCount() {
        return PlatformResult.success(userQueryService.userCount());
    }

    /**
     * 身份详情 (按角色分派)。
     *
     * <p>保留旧语义: 仅支持 供应商 / 交易师 / 渠道商 / 甄选师 四种角色, 其余角色抛参数异常。</p>
     *
     * @param userAccount 角色 + 账号入参
     * @return 对应身份详情
     */
    @PostMapping("/count/userAccount")
    public PlatformResult<?> userAccount(@RequestBody CountCmd.UserAccount userAccount) {
        Long roleId = userAccount.getRoleId();
        Long accountId = userAccount.getAccountId();
        if (RoleEnum.CompanyRole.SUPPLIER.getCode().equals(roleId)) {
            return PlatformResult.success(userQueryService.supplierVO(accountId));
        } else if (RoleEnum.CompanyRole.DEALER.getCode().equals(roleId)) {
            return PlatformResult.success(userQueryService.dealerVO(accountId));
        } else if (RoleEnum.CompanyRole.CHANNEL.getCode().equals(roleId)) {
            return PlatformResult.success(channelClientDomain.channel(accountId));
        } else if (RoleEnum.CompanyRole.SELECTOR.getCode().equals(roleId)) {
            return PlatformResult.success(userQueryService.selectorVO(accountId));
        } else {
            throw new PlatformException(BaseErrorCode.PARAM, "角色");
        }
    }

    /**
     * 供应商列表 (按邀请人)。
     *
     * <p>保留旧语义: 返回列表而非分页对象。旧出参元素为供应商领域视图, 中台分页出参为对外出参对象,
     * 字段一一对应。</p>
     *
     * @param supplierPage 邀请人入参
     * @return 供应商列表, 无数据返回空集合
     */
    @PostMapping("/count/supplierPage")
    public PlatformResult<List<SupplierRes>> supplierPage(@RequestBody CountCmd.SupplierPage supplierPage) {
        SupplierQuery supplierQuery = new SupplierQuery();
        supplierQuery.setInviteId(supplierPage.getInviteId());
        return PlatformResult.success(records(supplierClientDomain.supplierPage(supplierQuery)));
    }

    /**
     * 渠道商列表 (按上级)。
     *
     * <p>旧字段 {@code upDealerId} (上级交易师 ID) 在中台合并为 {@code invitedId} (上级 ID)。</p>
     *
     * @param channelPage 上级入参
     * @return 渠道商列表, 无数据返回空集合
     */
    @PostMapping("/count/channelPage")
    public PlatformResult<List<ChannelVO>> channelPage(@RequestBody CountCmd.ChannelPage channelPage) {
        ChannelQuery channelQuery = new ChannelQuery();
        channelQuery.setInvitedId(channelPage.getInviteId());
        return PlatformResult.success(records(channelClientDomain.channelPageList(channelQuery)));
    }

    /**
     * 甄选师列表 (按上级甄选师)。
     *
     * @param selectorPage 上级甄选师入参
     * @return 甄选师列表, 无数据返回空集合
     */
    @PostMapping("/count/selectorPage")
    public PlatformResult<List<SelectorVO>> selectorPage(@RequestBody CountCmd.SelectorPage selectorPage) {
        SelectorQuery selectorQuery = new SelectorQuery();
        selectorQuery.setInviteId(selectorPage.getInviteId());
        return PlatformResult.success(records(userQueryService.selectorPage(selectorQuery)));
    }

    /**
     * 交易师列表 (按所属运营商)。
     *
     * @param dealerPage 运营商入参
     * @return 交易师列表, 无数据返回空集合
     */
    @PostMapping("/count/dealerPage")
    public PlatformResult<List<DealerVO>> dealerPage(@RequestBody CountCmd.DealerPage dealerPage) {
        DealerQuery dealerQuery = new DealerQuery();
        dealerQuery.setOperatorId(dealerPage.getInviteId());
        return PlatformResult.success(records(userQueryService.dealerPage(dealerQuery)));
    }

    /**
     * 账号详情。
     *
     * <p>旧单参 {@code accountOutVO(Long)} 在中台改为按端 + ID 查询, 故先取账号所属端。</p>
     *
     * @param id 账号 ID 入参
     * @return 账号对外详情
     */
    @PostMapping("/count/accountDetail")
    public PlatformResult<AccountOutRes> accountDetail(@RequestBody CountCmd.ID id) {
        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setId(id.getAccountId());
        AccountInfo accountInfo = accountDomain.accountInfo(accountQuery);
        if (accountInfo == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "账号");
        }
        return PlatformResult.success(userQueryService.accountOutVO(accountInfo.getClient(), accountInfo.getId()));
    }

    /**
     * 取分页记录, 分页对象或记录为空时返回空集合。
     *
     * <p>中台部分查询能力尚未接线 (返回 null 分页), 本层做空守卫以免适配层直接空指针。</p>
     *
     * @param page 分页对象
     * @param <T>  记录类型
     * @return 记录列表, 无数据返回空集合
     */
    private <T> List<T> records(Page<T> page) {
        if (page == null || page.getRecords() == null) {
            return new ArrayList<>();
        }
        return page.getRecords();
    }
}
