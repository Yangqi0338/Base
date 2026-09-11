package com.newzkl.platform.base.common.ddd.model.auth;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.vo.RequestInfo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;


/**
 * 权限获取工具类
 *
 * <p>迁移说明: 原 {@code getIdentity()} 依赖业务枚举 {@code RoleEnum.CompanyRole}，
 * 已随业务枚举下沉到业务层</p>
 *
 * @author ruoyi
 */
public class SecurityUtils {
    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public static Long getAccountId() {
        String s = SecurityContextHolder.get(TokenConstants.DETAILS_ACCOUNT_ID, String.class);
        return NumberUtil.parseLong(s, null);
    }

    public static Long getUpId() {
        String s = SecurityContextHolder.get(TokenConstants.DETAILS_UP_ID, String.class);
        return Long.parseLong(s);
    }

    public static AccountEnum.Client doGetRequestClient() {
        // 先检查前端有没有指定Client
        String client = SecurityContextHolder.get(TokenConstants.REQUEST_CLIENT, String.class);
        return AccountEnum.Client.getByCode(client);
    }

    public static AccountEnum.Client getRequestClient() {
        // 先检查前端有没有指定Client
        AccountEnum.Client client = doGetRequestClient();
        if (client == null) {
            throw new PlatformException(AccountErrorCode.NO_CLIENT);
        }
        return client;
    }

    public static AccountEnum.Client getClient() {
        // 先检查前端有没有指定Client
        AccountEnum.Client client = doGetRequestClient();
        if (client == null) {
            String clientList = SecurityContextHolder.get(TokenConstants.DETAILS_CLIENT, String.class);
            client = AccountEnum.Client.getByCode(CollUtil.getFirst(StrUtil.split(clientList,',')));
        }
        if (client == null) {
            throw new PlatformException(AccountErrorCode.NO_CLIENT);
        }
        return client;
    }

    public static String getUsername() {
        String s = SecurityContextHolder.get(TokenConstants.DETAILS_USERNAME, String.class);
        return s;
    }

    public static String getNickName() {
        String s = SecurityContextHolder.get(TokenConstants.DETAILS_NICKNAME, String.class);
        return s;
    }

    public static AccountEnum.Identity getIdentity() {
        String identity = SecurityContextHolder.get(TokenConstants.DETAILS_IDENTITY, String.class);
        Long identityCode = ArrayUtil.get(StrUtil.splitToLong(identity, ','), 0);
        return AccountEnum.Identity.getByCode(identityCode);
    }

    public static List<AccountEnum.Identity> getIdentityList() {
        String identity = SecurityContextHolder.get(TokenConstants.DETAILS_IDENTITY, String.class);
        if (StrUtil.isBlank(identity)) {
            return CollUtil.newArrayList();
        }
        return Arrays.stream(StrUtil.splitToLong(identity, ','))
                .mapToObj(AccountEnum.Identity::getByCode).toList();
    }

    public static RequestInfo getRequestInfo() {
        return SecurityContextHolder.get(TokenConstants.REQUEST_INFO, RequestInfo.class);
    }
}
