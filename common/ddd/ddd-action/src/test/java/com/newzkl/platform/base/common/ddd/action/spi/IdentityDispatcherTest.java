package com.newzkl.platform.base.common.ddd.action.spi;

import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.application.spi.IdentityExtension;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@code IdentityDispatcher} 分发三分支单测: 命中 / catch-all 兜底 / 无命中报错
 *
 * @author KC
 */
class IdentityDispatcherTest {

    @AfterEach
    void clear() {
        SecurityContextHolder.remove();
    }

    @Test
    void resolveHitsMatchingImplByRoleId() {
        IdentityDispatcher dispatcher = new IdentityDispatcher();
        dispatcher.register(Greeter.class, (Greeter) () -> "platform", new RoleEnum.CompanyRole[]{RoleEnum.CompanyRole.PLATFORM, RoleEnum.CompanyRole.EMP});
        dispatcher.register(Greeter.class, (Greeter) () -> "channel", new RoleEnum.CompanyRole[]{RoleEnum.CompanyRole.CHANNEL});

        SecurityContextHolder.set(TokenConstants.DETAILS_ROLE, "1002");
        assertEquals("channel", dispatcher.resolve(Greeter.class).hi());
    }

    @Test
    void resolveFallsBackToCatchAllWhenNoConditionMatches() {
        IdentityDispatcher dispatcher = new IdentityDispatcher();
        dispatcher.register(Greeter.class, (Greeter) () -> "platform", new RoleEnum.CompanyRole[]{RoleEnum.CompanyRole.PLATFORM, RoleEnum.CompanyRole.EMP});
        dispatcher.register(Greeter.class, (Greeter) () -> "fallback", new RoleEnum.CompanyRole[]{});

        SecurityContextHolder.set(TokenConstants.DETAILS_ROLE, "1001");
        assertEquals("fallback", dispatcher.resolve(Greeter.class).hi());
    }

    @Test
    void resolveThrowsWhenNoMatchAndNoCatchAll() {
        IdentityDispatcher dispatcher = new IdentityDispatcher();
        dispatcher.register(Greeter.class, (Greeter) () -> "platform", new RoleEnum.CompanyRole[]{RoleEnum.CompanyRole.PLATFORM, RoleEnum.CompanyRole.EMP});

        SecurityContextHolder.set(TokenConstants.DETAILS_ROLE, "1001");
        Greeter proxy = dispatcher.resolve(Greeter.class);
        PlatformException ex = assertThrows(PlatformException.class, proxy::hi);
        assertEquals("1010", ex.getCode());
    }

    /**
     * 测试用扩展点
     */
    @IdentityExtension
    interface Greeter {

        /**
         * @return 问候语
         */
        String hi();
    }
}
