package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.application.service.AccountService;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.AdminDisableAccountReq;
import com.newzkl.platform.base.biz.account.model.req.AdminRegisterIdentityReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.vo.MemberAccountVO;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link AccountAdminController} 入参与出参契约测试。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("中台用户管理控制器")
class AccountAdminControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private UserClientDomain userClientDomain;

    @InjectMocks
    private AccountAdminController accountAdminController;

    @Test
    @DisplayName("分页: 透传查询条件, 原样返回 MyBatis-Plus 分页")
    void pageShouldDelegate() {
        AccountQuery query = new AccountQuery();
        Page<MemberAccountVO> page = new Page<>();
        page.setTotal(9);
        when(accountService.pageAccount(query)).thenReturn(page);

        assertEquals(9, accountAdminController.page(query).getData().getTotal());
    }

    @Test
    @DisplayName("添加会员: 复用平台建会员领域能力, 出参不回传主键")
    void addShouldDelegateToMemberCreation() {
        AdminRegisterIdentityReq req = new AdminRegisterIdentityReq();
        req.setPhone("13800000000");
        when(userClientDomain.adminCreateMember(req)).thenReturn(new IdentityRegisterRes(null, 123L));

        assertNull(accountAdminController.add(req).getData(), "旧接口不回传主键");
        verify(userClientDomain).adminCreateMember(req);
    }

    @Test
    @DisplayName("启用禁用: 透传入参给应用服务")
    void disableShouldDelegate() {
        AdminDisableAccountReq req = new AdminDisableAccountReq();
        req.setId(88L);

        accountAdminController.disableMember(req);

        verify(accountService).disableAccount(req);
    }

    @Test
    @DisplayName("批量导入: 透传文件并返回结构化导入结果")
    void importMemberShouldDelegate() {
        MultipartFile file = new MockMultipartFile("file", "member.xlsx", null, new byte[]{1, 2});
        EasyExcelErrorVO result = new EasyExcelErrorVO(null, 2, 1, 1, List.of());
        when(userClientDomain.adminImportAccount(file)).thenReturn(result);

        assertSame(result, accountAdminController.importMember(file).getData());
    }

    @Test
    @DisplayName("端点集合锁定: 仅 4 个端点, 未迁的能力不得悄悄补上")
    void shouldExposeFourEndpointsOnly() {
        List<String> methods = Arrays.stream(AccountAdminController.class.getDeclaredMethods())
                .filter(it -> !it.isSynthetic())
                .map(Method::getName)
                .sorted()
                .toList();

        assertEquals(List.of("add", "disableMember", "importMember", "page"), methods);
        assertTrue(methods.size() == 4);
    }
}
