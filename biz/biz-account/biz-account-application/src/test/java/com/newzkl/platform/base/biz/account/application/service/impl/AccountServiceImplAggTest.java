package com.newzkl.platform.base.biz.account.application.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PermissionApi;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.domain.repository.ChannelRepository;
import com.newzkl.platform.base.biz.account.domain.repository.EmpRepository;
import com.newzkl.platform.base.biz.account.domain.repository.MemberRepository;
import com.newzkl.platform.base.biz.account.domain.repository.SupplierRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.res.AccountAggRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 账号聚合分页/详情装配测试
 * <p>
 * 纯 JUnit 5 + Mockito, 不启动 Spring 容器, 不连数据库。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("账号身份聚合")
class AccountServiceImplAggTest {

    private static final Long ACCOUNT_ID = 90001L;

    @Mock
    private AccountDomain accountDomain;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountAssembler accountAssembler;
    @Mock
    private PermissionApi permissionApi;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private EmpRepository empRepository;
    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private ChannelRepository channelRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    /**
     * 构造一个 MEMBER 账号视图 (client=USER, identityList csv 对齐 MemberVO 主键)
     */
    private AccountVO memberAccount() {
        AccountVO accountVO = new AccountVO();
        accountVO.setId(ACCOUNT_ID);
        accountVO.setRealName("张三");
        accountVO.setUsername("zs");
        accountVO.setClient(AccountEnum.Client.USER);
        accountVO.setIdentityList(AccountEnum.Identity.MEMBER.getCode() + "");
        return accountVO;
    }

    @Test
    @DisplayName("空页不回填身份表, 直接返回空 Page")
    void aggPageEmptyPageSkipsBackfill() {
        AccountQuery query = new AccountQuery();
        query.setIdentity(AccountEnum.Identity.MEMBER);
        when(accountRepository.accountPage(any())).thenReturn(new Page<>());

        Page<AccountAggRes> result = accountService.aggPage(query);

        assertTrue(result.getRecords().isEmpty());
        verify(memberRepository, never()).selectMemberByAccountIdList(any());
        verify(permissionApi, never()).findRoleCodeByAccountIdList(any(), any());
    }

    @Test
    @DisplayName("MEMBER 身份: member 槽有值, 其余 3 槽为 null, roleList 为 code 列表")
    void aggPageFillsOnlyMemberSlot() {
        AccountVO accountVO = memberAccount();

        Page<AccountVO> page = new Page<>(1, 10, 1);
        page.setRecords(List.of(accountVO));

        MemberVO memberVO = new MemberVO();
        memberVO.setId(ACCOUNT_ID);
        memberVO.setNickname("小张");

        when(accountRepository.accountPage(any())).thenReturn(page);
        when(memberRepository.selectMemberByAccountIdList(List.of(ACCOUNT_ID)))
                .thenReturn(List.of(memberVO));
        when(permissionApi.findRoleCodeByAccountIdList(AccountEnum.Client.USER, List.of(ACCOUNT_ID)))
                .thenReturn(Map.of(ACCOUNT_ID, List.of("SUPER_ADMIN")));

        AccountQuery query = new AccountQuery();
        query.setIdentity(AccountEnum.Identity.MEMBER);

        AccountAggRes agg = accountService.aggPage(query).getRecords().get(0);

        assertEquals(List.of(AccountEnum.Identity.MEMBER), agg.getIdentityList());
        assertNotNull(agg.getMember());
        assertEquals("小张", agg.getMember().getNickname());
        assertNull(agg.getEmp());
        assertNull(agg.getSupplier());
        assertNull(agg.getChannel());
        assertEquals(List.of("SUPER_ADMIN"), agg.getRoleList());
        // 继承 AccountRes 后账号主体字段平铺
        assertEquals("张三", agg.getRealName());
        verify(empRepository, never()).listByIdList(any());
    }

    @Test
    @DisplayName("身份行缺失 (脏数据) 时槽为 null 但行保留, records.size() 与 total 一致")
    void aggPageKeepsRowWhenIdentityRowMissing() {
        AccountVO accountVO = memberAccount();

        Page<AccountVO> page = new Page<>(1, 10, 1);
        page.setRecords(List.of(accountVO));

        when(accountRepository.accountPage(any())).thenReturn(page);
        when(memberRepository.selectMemberByAccountIdList(List.of(ACCOUNT_ID)))
                .thenReturn(List.of());
        when(permissionApi.findRoleCodeByAccountIdList(any(), any())).thenReturn(Map.of());

        AccountQuery query = new AccountQuery();
        query.setIdentity(AccountEnum.Identity.MEMBER);

        Page<AccountAggRes> result = accountService.aggPage(query);

        assertEquals(1, result.getRecords().size());
        assertEquals(1L, result.getTotal());
        assertNull(result.getRecords().get(0).getMember());
        assertNotNull(result.getRecords().get(0).getRealName());
        assertTrue(result.getRecords().get(0).getRoleList().isEmpty());
    }

    @Test
    @DisplayName("无表身份 (PARTNER) 4 槽全 null, 不打任何身份表")
    void aggPageTablelessIdentityFillsNoSlot() {
        AccountVO accountVO = memberAccount();
        accountVO.setClient(AccountEnum.Client.PARTNER);
        accountVO.setIdentityList(AccountEnum.Identity.PARTNER.getCode() + "");

        Page<AccountVO> page = new Page<>(1, 10, 1);
        page.setRecords(List.of(accountVO));

        when(accountRepository.accountPage(any())).thenReturn(page);
        when(permissionApi.findRoleCodeByAccountIdList(any(), any())).thenReturn(Map.of());

        AccountQuery query = new AccountQuery();
        query.setIdentity(AccountEnum.Identity.PARTNER);

        AccountAggRes agg = accountService.aggPage(query).getRecords().get(0);

        assertNull(agg.getMember());
        assertNull(agg.getEmp());
        assertNull(agg.getSupplier());
        assertNull(agg.getChannel());
        verify(memberRepository, never()).selectMemberByAccountIdList(any());
        verify(empRepository, never()).listByIdList(any());
        verify(supplierRepository, never()).listByIdList(any());
        verify(channelRepository, never()).listByIdList(any());
    }

    @Test
    @DisplayName("aggDetail: 单账号装配, member 槽有值")
    void aggDetailFillsMemberSlot() {
        AccountVO accountVO = memberAccount();

        MemberVO memberVO = new MemberVO();
        memberVO.setId(ACCOUNT_ID);
        memberVO.setNickname("小张");

        when(accountRepository.account(null, ACCOUNT_ID)).thenReturn(accountVO);
        when(memberRepository.selectMemberByAccountIdList(List.of(ACCOUNT_ID)))
                .thenReturn(List.of(memberVO));
        when(permissionApi.findRoleCodeByAccountIdList(AccountEnum.Client.USER, List.of(ACCOUNT_ID)))
                .thenReturn(Map.of(ACCOUNT_ID, List.of("SUPER_ADMIN")));

        AccountAggRes agg = accountService.aggDetail(List.of(AccountEnum.Identity.MEMBER), ACCOUNT_ID);

        assertEquals(List.of(AccountEnum.Identity.MEMBER), agg.getIdentityList());
        assertEquals("小张", agg.getMember().getNickname());
        assertEquals("张三", agg.getRealName());
        assertEquals(List.of("SUPER_ADMIN"), agg.getRoleList());
        assertNull(agg.getEmp());
    }

    @Test
    @DisplayName("aggDetail: 账号不存在抛 PlatformException, 不打身份表")
    void aggDetailThrowsWhenAccountMissing() {
        when(accountRepository.account(null, ACCOUNT_ID)).thenReturn(null);

        assertThrows(PlatformException.class,
                () -> accountService.aggDetail(List.of(AccountEnum.Identity.MEMBER), ACCOUNT_ID));

        verify(memberRepository, never()).selectMemberByAccountIdList(any());
    }
}
