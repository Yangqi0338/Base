package com.newzkl.platform.base.biz.order.model.support.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 账号
 *
 * <p>迁移说明: 跨域 ACL 本地副本, 源自 biz-account 的
 * {@code com.newzkl.platform.base.biz.account.model.enums.AccountEnum},
 * 后续应上移 ddd-model 共享内核(登 deferred)</p>
 *
 * @author fang
 */
@Data
public class AccountEnum {

    public static final Long MAIN_ACCOUNT_PID = 0L;
    public static final String USER_DEFAULT_PASSWORD = "123456";

    /**
     * 子用户类型
     */
    @Getter
    @AllArgsConstructor
    public enum SubUserType {
        /**
         * 主账号
         */
        MAIN(0, "主账号"),
        /** 子账号 */
        ACCOUNT(1, "子账号"),
        /** 子客户 */
        MEMBER(2, "子客户"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    /**
     * 主体类型
     */
    @Getter
    @AllArgsConstructor
    public enum BodyType {
        /** 企业 */
        COMPANY(0, "企业"),
        /** 个人 */
        PERSON(1, "个人"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    /**
     * 账号状态
     */
    @Getter
    @AllArgsConstructor
    public enum State {
        /** 已销毁 */
        DESTROY(-1, "已销毁"),
        /** 禁用,冻结 */
        DISABLE(0, "禁用,冻结"),
        /** 正常 */
        ENABLE(1, "正常"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    /**
     * 登录类型
     */
    @Getter
    @AllArgsConstructor
    public enum LoginType {
        /** 密码 */
        PASSWORD(0, "密码"),
        /** 验证码 */
        CODE(1, "验证码"),
        /**
         * 自动刷新
         */
        REFRESH(2, "自动刷新", false),
        /** 切换身份 */
        TOGGLE(3, "切换身份", false),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
        private final boolean needLog;

        LoginType(Integer code, String value) {
            this.code = code;
            this.value = value;
            this.needLog = true;
        }
    }

    /**
     * 账号状态
     */
    @Getter
    @AllArgsConstructor
    public enum ImSyncState {
        /**
         * 未同步
         */
        INIT(0, "未同步"),
        /**
         * 已同步
         */
        SYNC(1, "已同步"),
        /**
         * 同步失败
         */
        FAIL(2, "同步失败"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }
}
