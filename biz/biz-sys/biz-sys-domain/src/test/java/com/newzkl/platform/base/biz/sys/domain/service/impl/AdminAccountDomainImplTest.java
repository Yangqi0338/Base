package com.newzkl.platform.base.biz.sys.domain.service.impl;

import com.newzkl.platform.base.biz.sys.domain.adapt.repository.AdminAccountRepository;
import com.newzkl.platform.base.biz.sys.model.adminaccount.req.AdminAccountReq;
import com.newzkl.platform.base.biz.sys.model.adminaccount.req.PasswordUpdateReq;
import com.newzkl.platform.base.biz.sys.model.adminaccount.res.AdminAccountRes;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 平台账号领域服务单元测试 (仓储端口 mock, 不连库)。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class AdminAccountDomainImplTest {

    @Mock
    private AdminAccountRepository adminAccountRepository;

    @InjectMocks
    private AdminAccountDomainImpl adminAccountDomain;

    @Test
    void adminAccountCreate_encodesPasswordAsBCrypt() {
        AdminAccountReq req = new AdminAccountReq();
        req.setUsername("admin");
        req.setPassword("123456");
        when(adminAccountRepository.adminAccountSave(any(AdminAccountReq.class))).thenReturn(1L);

        Long id = adminAccountDomain.adminAccountCreate(req);

        assertThat(id).isEqualTo(1L);
        ArgumentCaptor<AdminAccountReq> captor = ArgumentCaptor.forClass(AdminAccountReq.class);
        verify(adminAccountRepository).adminAccountSave(captor.capture());
        String stored = captor.getValue().getPassword();
        assertThat(stored).isNotEqualTo("123456");
        assertThat(new BCryptPasswordEncoder().matches("123456", stored)).isTrue();
    }

    @Test
    void adminAccountCreate_blankPasswordNotEncoded() {
        AdminAccountReq req = new AdminAccountReq();
        req.setUsername("admin");
        when(adminAccountRepository.adminAccountSave(any(AdminAccountReq.class))).thenReturn(2L);

        adminAccountDomain.adminAccountCreate(req);

        ArgumentCaptor<AdminAccountReq> captor = ArgumentCaptor.forClass(AdminAccountReq.class);
        verify(adminAccountRepository).adminAccountSave(captor.capture());
        assertThat(captor.getValue().getPassword()).isNull();
    }

    @Test
    void passwordVerify_matchReturnsAccount() {
        AdminAccountRes account = new AdminAccountRes();
        account.setId(1L);
        account.setUsername("admin");
        account.setPassword(new BCryptPasswordEncoder().encode("secret"));
        when(adminAccountRepository.adminAccountByUsername("admin")).thenReturn(account);

        AdminAccountRes verified = adminAccountDomain.passwordVerify("admin", "secret");

        assertThat(verified.getId()).isEqualTo(1L);
    }

    @Test
    void passwordVerify_wrongPasswordThrows() {
        AdminAccountRes account = new AdminAccountRes();
        account.setUsername("admin");
        account.setPassword(new BCryptPasswordEncoder().encode("secret"));
        when(adminAccountRepository.adminAccountByUsername("admin")).thenReturn(account);

        assertThatThrownBy(() -> adminAccountDomain.passwordVerify("admin", "wrong"))
                .isInstanceOf(ScmException.class);
    }

    @Test
    void passwordVerify_noAccountThrows() {
        when(adminAccountRepository.adminAccountByUsername("ghost")).thenReturn(null);

        assertThatThrownBy(() -> adminAccountDomain.passwordVerify("ghost", "any"))
                .isInstanceOf(ScmException.class);
    }

    @Test
    void passwordUpdate_verifiesOldThenSavesNewEncoded() {
        AdminAccountRes account = new AdminAccountRes();
        account.setId(5L);
        account.setUsername("admin");
        account.setPassword(new BCryptPasswordEncoder().encode("old"));
        when(adminAccountRepository.adminAccountVO(5L)).thenReturn(account);

        PasswordUpdateReq req = new PasswordUpdateReq();
        req.setAccountId(5L);
        req.setPassword("old");
        req.setNewPassword("new");

        adminAccountDomain.passwordUpdate(req);

        ArgumentCaptor<AdminAccountReq> captor = ArgumentCaptor.forClass(AdminAccountReq.class);
        verify(adminAccountRepository).adminAccountSave(captor.capture());
        AdminAccountReq saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(5L);
        assertThat(new BCryptPasswordEncoder().matches("new", saved.getPassword())).isTrue();
    }
}
