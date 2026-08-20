package com.newzkl.platform.base.common.ddd.model.enums.account;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

/**
 * 账号枚举
 * @author fang
 */
public class AccountEnum {

    /**
     * 账号状态
     */
    @Getter
    @AllArgsConstructor
    public enum State implements IEnum<Integer> {
        /** 已注销: 用户主动注销, 记 cancelTime; 24h 内重新登录可恢复正常, 超时被回收后可重新注册 */
        DESTROY(-1, "已注销"),
        /** 已封禁: 平台拉黑, 不可登录/注册 */
        DISABLE(0, "已封禁"),
        /** 正常 */
        ENABLE(1, "正常"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    /**
     * 客户端
     */
    @Getter
    @AllArgsConstructor
    public enum Client implements IEnum<String> {
        /** 平台端 */
        ADMIN("admin", "平台端", Identity.EMP),
        /** 后台端 */
        USER("user", "用户端", Identity.MEMBER),
        /** 服务商端 */
        PARTNER("partner", "服务商端", Identity.PARTNER),
        /** 渠道商端 */
        CHANNEL("channel", "渠道商端", Identity.CHANNEL),
        /** 供应商端 */
        SUPPLIER("supplier", "供应商端", Identity.SUPPLIER),
        ;

        @EnumValue
        @JsonValue
        private final String code;
        private final String value;
        private final Identity defaultIdentity;

        public static AccountEnum.Client getByCode(String code) {
            return Stream.of(values())
                    .filter(extension -> extension.getCode().equalsIgnoreCase(code))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * 公司角色枚举
     */
    @Getter
    @AllArgsConstructor
    public enum Identity {
        /**
         * 平台管理员
         */
        PLATFORM(1L, "平台管理员", Client.ADMIN),
        /** 平台员工 */
        EMP(2L, "平台员工", Client.ADMIN),
        /** 会员 */
        MEMBER(1000L, "会员", Client.USER),
        /** 供应商 */
        SUPPLIER(1001L, "供应商", Client.SUPPLIER),
        /** 渠道商 */
        CHANNEL(1002L, "渠道商", Client.CHANNEL),
        /** 服务商 */
        PARTNER(1003L, "服务商", Client.PARTNER),

        ;

        @JsonValue
        @EnumValue
        private final Long code;
        private final String value;
        /** 所属端 */
        private final Client client;
        /** 角色级别 */
        private final Integer level;

        Identity(Long code, String value, Client client) {
            this.code = code;
            this.value = value;
            this.client = client;
            this.level = 0;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 编码
         * @return 匹配的枚举，未匹配返回 null
         */
        public static Identity getByCode(Long code) {
            if (code == null) {
                return null;
            }
            return Stream.of(Identity.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }

        /**
         * 是否游客
         *
         * @param identity 角色
         * @return 是否游客
         */
        public static boolean isGuest(Identity identity) {
            return identity != null && identity.getCode() >= 0;
        }

        /**
         * 判断角色编码串是否包含指定角色
         *
         * @param identity      角色
         * @param identityIdStr 角色编码串
         * @return 是否包含
         */
        public static boolean contains(Identity identity, String identityIdStr) {
            if (StrUtil.isBlank(identityIdStr) || identity == null) {
                return false;
            }
            return StrUtil.contains(identityIdStr, identity.getCodeStr());
        }

        /**
         * 从角色列表中找出被编码串包含的角色
         *
         * @param roleList  角色列表
         * @param roleIdStr 角色编码串
         * @return 匹配的角色
         */
        public static Identity contains(List<Identity> roleList, String roleIdStr) {
            return CollUtil.findOne(roleList, role -> contains(role, roleIdStr));
        }

        /**
         * 编码字符串
         *
         * @return 编码字符串
         */
        public String getCodeStr() {
            return getCode() + "";
        }

        /**
         * 将角色编码串转换为角色名称串
         *
         * @param roleIdStr 角色编码串
         * @return 角色名称串
         */
        public static String transferValue(String roleIdStr) {
            String result = null;
            for (Identity companyRole : values()) {
                result = StrUtil.replace(roleIdStr, companyRole.getCodeStr(), companyRole.getValue());
            }
            return result;
        }

    }

    /**
     * 员工类型
     */
    @Getter
    @AllArgsConstructor
    public enum EmpType {
        MANAGER(0, "管理员"),
        SIMPLE(1, "普通"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        public static EmpType getByRole(Identity identity) {
            if (identity != Identity.PLATFORM && identity != Identity.EMP) {
                return null;
            }
            return identity == Identity.PLATFORM ? MANAGER : SIMPLE;
        }
    }
}
