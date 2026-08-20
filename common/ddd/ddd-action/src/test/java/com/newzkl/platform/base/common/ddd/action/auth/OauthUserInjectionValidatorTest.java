package com.newzkl.platform.base.common.ddd.action.auth;

import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.model.auth.OauthIdentity;
import com.newzkl.platform.base.common.ddd.model.auth.OauthUserId;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.utils.auth.OauthUserInjectionValidator;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 登录用户注入校验器测试
 *
 * <p>校验器直连 {@code SecurityUtils}(读 {@link SecurityContextHolder} ThreadLocal),
 * 故通过填/清 ThreadLocal 模拟登录态, 无需 mock static。</p>
 *
 * @author KC
 */
class OauthUserInjectionValidatorTest {

    private final OauthUserInjectionValidator validator = new OauthUserInjectionValidator();

    /**
     * 每例后清 ThreadLocal, 防串扰
     */
    @AfterEach
    void tearDown() {
        SecurityContextHolder.remove();
    }

    /**
     * 模拟登录: 填账号ID与角色ID到 ThreadLocal
     *
     * @param accountId 账号ID, null 表示未登录
     * @param roleId    角色ID, null 表示无角色
     */
    private void login(Long accountId, Long roleId) {
        if (accountId != null) {
            SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, String.valueOf(accountId));
        }
        if (roleId != null) {
            SecurityContextHolder.set(TokenConstants.DETAILS_IDENTITY, String.valueOf(roleId));
        }
    }

    /**
     * 登录态: userId 字段被回填
     */
    @Test
    void injectsWhenLoggedIn() {
        login(1001L, null);
        SampleReq bean = new SampleReq();
        assertTrue(validator.isValid(bean, null));
        assertEquals(1001L, bean.getUserId());
        assertEquals("1001", bean.getUserIdStr());
    }

    /**
     * 未登录 + required: 判失败, 不回填
     */
    @Test
    void failsWhenRequiredButNotLoggedIn() {
        SampleReq bean = new SampleReq();
        assertFalse(validator.isValid(bean, null));
        assertNull(bean.getUserId());
    }

    /**
     * 未登录 + 非 required: 放行, 不回填
     */
    @Test
    void skipsWhenOptionalAndNotLoggedIn() {
        OptionalReq bean = new OptionalReq();
        assertTrue(validator.isValid(bean, null));
        assertNull(bean.getUserId());
    }

    /**
     * 登录态: 角色按字段类型回填(Long/String/枚举)
     */
    @Test
    void injectsRoleWhenLoggedIn() {
        login(1001L, AccountEnum.Identity.SUPPLIER.getCode());
        RoleReq bean = new RoleReq();
        assertTrue(validator.isValid(bean, null));
        assertEquals(AccountEnum.Identity.SUPPLIER.getCode(), bean.getRoleId());
        assertEquals(String.valueOf(AccountEnum.Identity.SUPPLIER.getCode()), bean.getRoleIdStr());
        assertEquals(AccountEnum.Identity.SUPPLIER, bean.getRole());
    }

    /**
     * 未登录(无角色) + required 角色: 判失败
     */
    @Test
    void failsWhenRoleRequiredButNoRole() {
        RoleReq bean = new RoleReq();
        assertFalse(validator.isValid(bean, null));
        assertNull(bean.getRole());
    }

    /**
     * 必填样例入参
     */
    @Getter
    @Setter
    static class SampleReq {

        /**
         * 用户ID
         */
        @OauthUserId
        private Long userId;

        /**
         * 用户ID字符串
         */
        @OauthUserId
        private String userIdStr;
    }

    /**
     * 非必填样例入参
     */
    @Getter
    @Setter
    static class OptionalReq {

        /**
         * 用户ID
         */
        @OauthUserId(required = false)
        private Long userId;
    }

    /**
     * 角色注入样例入参
     */
    @Getter
    @Setter
    static class RoleReq {

        /**
         * 角色ID
         */
        @OauthIdentity
        private Long roleId;

        /**
         * 角色ID字符串
         */
        @OauthIdentity
        private String roleIdStr;

        /**
         * 角色枚举
         */
        @OauthIdentity
        private AccountEnum.Identity identity;
    }
}
