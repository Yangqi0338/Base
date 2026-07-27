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
import com.newzkl.platform.base.biz.account.model.vo.SelectorVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link AdapterController} 转调契约测试。
 *
 * <p>防腐层不含业务, 测试只锁"转调对象 + 入参组装 + 出参形态"。</p>
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("用户适配控制器")
class AdapterControllerTest {

    private static final Long ACCOUNT_ID = 8801L;

    @Mock
    private ChannelController channelController;

    @Mock
    private AccountService accountService;

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private AccountDomain accountDomain;

    @Mock
    private ChannelClientDomain channelClientDomain;

    @Mock
    private SupplierClientDomain supplierClientDomain;

    @InjectMocks
    private AdapterController adapterController;

    @BeforeEach
    void setUpLoginState() {
        SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, ACCOUNT_ID.toString());
    }

    @AfterEach
    void clearLoginState() {
        SecurityContextHolder.remove();
    }

    @Test
    @DisplayName("渠道商详情: 直接委托已迁渠道商控制器")
    void channelForAdminShouldDelegateToChannelController() {
        PlatformResult<ChannelVO> expected = PlatformResult.success(new ChannelVO());
        when(channelController.channel(12L)).thenReturn(expected);

        assertSame(expected, adapterController.channelForAdmin(12L));
    }

    @Test
    @DisplayName("实名认证: 透传认证信息")
    void submitNameAuthInfoShouldDelegate() {
        NameAuthVO nameAuthVO = new NameAuthVO();

        adapterController.submitNameAuthInfo(nameAuthVO);

        verify(accountService).submitNameAuthInfo(nameAuthVO);
    }

    @Test
    @DisplayName("甄选师看供应商: 强制以登录账号为邀请人")
    void selectorSupplierPageShouldForceLoginInviteId() {
        when(userQueryService.selectorSupplierVO(org.mockito.ArgumentMatchers.any())).thenReturn(new Page<>());
        SupplierQuery query = new SupplierQuery();
        query.setInviteId(999L);

        adapterController.selectorSupplierPageVO(query);

        ArgumentCaptor<SupplierQuery> captor = ArgumentCaptor.forClass(SupplierQuery.class);
        verify(userQueryService).selectorSupplierVO(captor.capture());
        assertEquals(ACCOUNT_ID, captor.getValue().getInviteId(), "邀请人应被登录态覆盖");
    }

    @Test
    @DisplayName("分组统计: 只复制 6 个人数字段, 分组数据来自时间维度查询")
    void groupCountShouldCopySixCountsAndAttachGroup() {
        UserCountRes source = new UserCountRes();
        source.setAccountCount(1);
        source.setChannelCount(2);
        source.setSupplierCount(3);
        source.setSelectorCount(4);
        source.setDealerCount(5);
        source.setOperatorCount(6);
        source.setGroupCountRes(List.of(new GroupCountRes()));
        when(userQueryService.userCount()).thenReturn(source);
        List<GroupCountRes> group = List.of(new GroupCountRes(), new GroupCountRes());
        TimeQuery timeQuery = new TimeQuery();
        when(userQueryService.groupCount(timeQuery)).thenReturn(group);

        UserCountRes result = adapterController.indexCount(timeQuery).getData();

        assertEquals(1, result.getAccountCount());
        assertEquals(6, result.getOperatorCount());
        assertEquals(2, result.getGroupCountRes().size(), "分组数据应来自 groupCount 而非源对象");
    }

    @Test
    @DisplayName("分组统计: 人数统计为空时不抛空指针")
    void groupCountShouldTolerateNullUserCount() {
        when(userQueryService.userCount()).thenReturn(null);
        TimeQuery timeQuery = new TimeQuery();
        when(userQueryService.groupCount(timeQuery)).thenReturn(List.of());

        UserCountRes result = adapterController.indexCount(timeQuery).getData();

        assertTrue(result.getGroupCountRes().isEmpty());
    }

    @Test
    @DisplayName("数量统计: 直接透传")
    void userCountShouldDelegate() {
        UserCountRes res = new UserCountRes();
        when(userQueryService.userCount()).thenReturn(res);

        assertSame(res, adapterController.userCount().getData());
    }

    @Test
    @DisplayName("身份详情: 供应商角色走供应商查询")
    void userAccountShouldRouteSupplier() {
        SupplierVO vo = new SupplierVO();
        when(userQueryService.supplierVO(31L)).thenReturn(vo);

        assertSame(vo, adapterController.userAccount(userAccountCmd(1001L, 31L)).getData());
    }

    @Test
    @DisplayName("身份详情: 交易师角色走交易师查询")
    void userAccountShouldRouteDealer() {
        DealerVO vo = new DealerVO();
        when(userQueryService.dealerVO(32L)).thenReturn(vo);

        assertSame(vo, adapterController.userAccount(userAccountCmd(1005L, 32L)).getData());
    }

    @Test
    @DisplayName("身份详情: 渠道商角色走渠道商领域查询")
    void userAccountShouldRouteChannel() {
        ChannelVO vo = new ChannelVO();
        when(channelClientDomain.channel(33L)).thenReturn(vo);

        assertSame(vo, adapterController.userAccount(userAccountCmd(1002L, 33L)).getData());
    }

    @Test
    @DisplayName("身份详情: 甄选师角色走甄选师查询")
    void userAccountShouldRouteSelector() {
        SelectorVO vo = new SelectorVO();
        when(userQueryService.selectorVO(34L)).thenReturn(vo);

        assertSame(vo, adapterController.userAccount(userAccountCmd(1006L, 34L)).getData());
    }

    @Test
    @DisplayName("身份详情: 其余角色抛参数异常 (保留旧语义)")
    void userAccountShouldRejectOtherRole() {
        PlatformException ex = assertThrows(PlatformException.class,
                () -> adapterController.userAccount(userAccountCmd(1000L, 35L)));

        assertEquals("602", ex.getCode());
    }

    @Test
    @DisplayName("供应商列表: 按邀请人查询并降为列表")
    void supplierPageShouldReturnList() {
        Page<SupplierRes> page = new Page<>();
        page.setRecords(List.of(new SupplierRes()));
        when(supplierClientDomain.supplierPage(org.mockito.ArgumentMatchers.any())).thenReturn(page);
        CountCmd.SupplierPage cmd = new CountCmd.SupplierPage();
        cmd.setInviteId(41L);

        assertEquals(1, adapterController.supplierPage(cmd).getData().size());
        ArgumentCaptor<SupplierQuery> captor = ArgumentCaptor.forClass(SupplierQuery.class);
        verify(supplierClientDomain).supplierPage(captor.capture());
        assertEquals(41L, captor.getValue().getInviteId());
    }

    @Test
    @DisplayName("渠道商列表: 旧 upDealerId 映射到上级 ID, 出参为列表")
    void channelPageShouldMapUpDealerId() {
        Page<ChannelVO> page = new Page<>();
        page.setRecords(List.of(new ChannelVO()));
        when(channelClientDomain.channelPageList(org.mockito.ArgumentMatchers.any())).thenReturn(page);
        CountCmd.ChannelPage cmd = new CountCmd.ChannelPage();
        cmd.setInviteId(42L);

        assertEquals(1, adapterController.channelPage(cmd).getData().size());
        ArgumentCaptor<ChannelQuery> captor = ArgumentCaptor.forClass(ChannelQuery.class);
        verify(channelClientDomain).channelPageList(captor.capture());
        assertEquals(42L, captor.getValue().getInvitedId());
    }

    @Test
    @DisplayName("甄选师列表: 按上级甄选师查询, 分页为空时返回空集合")
    void selectorPageShouldTolerateNullPage() {
        when(userQueryService.selectorPage(org.mockito.ArgumentMatchers.any())).thenReturn(null);
        CountCmd.SelectorPage cmd = new CountCmd.SelectorPage();
        cmd.setInviteId(43L);

        assertTrue(adapterController.selectorPage(cmd).getData().isEmpty());
        ArgumentCaptor<SelectorQuery> captor = ArgumentCaptor.forClass(SelectorQuery.class);
        verify(userQueryService).selectorPage(captor.capture());
        assertEquals(43L, captor.getValue().getInviteId());
    }

    @Test
    @DisplayName("交易师列表: 入参 inviteId 落到运营商 ID")
    void dealerPageShouldMapOperatorId() {
        when(userQueryService.dealerPage(org.mockito.ArgumentMatchers.any())).thenReturn(null);
        CountCmd.DealerPage cmd = new CountCmd.DealerPage();
        cmd.setInviteId(44L);

        assertTrue(adapterController.dealerPage(cmd).getData().isEmpty());
        ArgumentCaptor<DealerQuery> captor = ArgumentCaptor.forClass(DealerQuery.class);
        verify(userQueryService).dealerPage(captor.capture());
        assertEquals(44L, captor.getValue().getOperatorId());
    }

    @Test
    @DisplayName("账号详情: 先取账号所属端再查对外详情")
    void accountDetailShouldResolveClientFirst() {
        AccountInfo info = new AccountInfo();
        info.setId(51L);
        info.setClient(CommonEnum.Client.CHANNEL);
        when(accountDomain.accountInfo(org.mockito.ArgumentMatchers.any())).thenReturn(info);
        AccountOutRes res = new AccountOutRes();
        when(userQueryService.accountOutVO(CommonEnum.Client.CHANNEL, 51L)).thenReturn(res);
        CountCmd.ID cmd = new CountCmd.ID();
        cmd.setAccountId(51L);

        assertSame(res, adapterController.accountDetail(cmd).getData());
        ArgumentCaptor<AccountQuery> captor = ArgumentCaptor.forClass(AccountQuery.class);
        verify(accountDomain).accountInfo(captor.capture());
        assertEquals(51L, captor.getValue().getId());
    }

    @Test
    @DisplayName("账号详情: 账号不存在时抛无数据异常")
    void accountDetailShouldRejectAbsentAccount() {
        when(accountDomain.accountInfo(org.mockito.ArgumentMatchers.any())).thenReturn(null);
        CountCmd.ID cmd = new CountCmd.ID();
        cmd.setAccountId(52L);

        assertThrows(PlatformException.class, () -> adapterController.accountDetail(cmd));
    }

    /**
     * 构造角色详情入参。
     *
     * @param roleId    角色 ID
     * @param accountId 账号 ID
     * @return 入参命令
     */
    private CountCmd.UserAccount userAccountCmd(Long roleId, Long accountId) {
        CountCmd.UserAccount cmd = new CountCmd.UserAccount();
        cmd.setRoleId(roleId);
        cmd.setAccountId(accountId);
        return cmd;
    }
}
