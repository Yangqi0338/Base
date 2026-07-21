package com.newzkl.platform.base.biz.user.model.enums;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

/**
 * 角色枚举。
 *
 * <p>身份/账号域概念，因被 relation 域模型作为字段类型直接引用（无法用 port 抽象），
 * 迁移期自包含于 biz-user-model。后续可提升至 common 共享内核统一。</p>
 *
 * @author fang
 */
public class RoleEnum {

    /**
     * 公司角色枚举。
     */
    @Getter
    @AllArgsConstructor
    public enum CompanyRole {
        /**
         * 平台管理员
         */
        PLATFORM(1L, "平台管理员", CommonEnum.Client.ADMIN),
        /** 平台员工 */
        EMP(2L, "平台员工", CommonEnum.Client.ADMIN),
        /** C端客户 */
        MEMBER(1000L, "C端客户", CommonEnum.Client.USER),
        /** 供应商 */
        SUPPLIER(1001L, "供应商", CommonEnum.Client.SUPPLIER),
        /** 渠道商 */
        CHANNEL(1002L, "渠道商", CommonEnum.Client.CHANNEL),
        /** 运营商 */
        OPERATOR(1004L, "运营商", CommonEnum.Client.OPERATOR, 3),
        /** 交易师 */
        DEALER(1005L, "交易师", CommonEnum.Client.OPERATOR, 2),
        /** 甄选师 */
        SELECTOR(1006L, "甄选师", CommonEnum.Client.OPERATOR, 1),
        /** 游客 */
        OPERATOR_GUEST(-1L, "游客", CommonEnum.Client.OPERATOR, 0),

        ;

        @JsonValue
        @EnumValue
        private final Long code;
        private final String value;
        /** 所属端 */
        private final CommonEnum.Client client;
        /** 角色级别 */
        private final Integer level;

        CompanyRole(Long code, String value, CommonEnum.Client client) {
            this.code = code;
            this.value = value;
            this.client = client;
            this.level = 0;
        }

        /**
         * 根据编码获取枚举。
         *
         * @param code 编码
         * @return 匹配的枚举，未匹配返回 null
         */
        public static CompanyRole getByCode(Long code) {
            if (code == null) {
                return null;
            }
            return Stream.of(CompanyRole.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }

        /**
         * 是否游客。
         *
         * @param role 角色
         * @return 是否游客
         */
        public static boolean isGuest(CompanyRole role) {
            return role != null && role.getCode() >= 0;
        }

        /**
         * 判断角色编码串是否包含指定角色。
         *
         * @param role      角色
         * @param roleIdStr 角色编码串
         * @return 是否包含
         */
        public static boolean contains(CompanyRole role, String roleIdStr) {
            if (StrUtil.isBlank(roleIdStr) || role == null) {
                return false;
            }
            return StrUtil.contains(roleIdStr, role.getCodeStr());
        }

        /**
         * 从角色列表中找出被编码串包含的角色。
         *
         * @param roleList  角色列表
         * @param roleIdStr 角色编码串
         * @return 匹配的角色
         */
        public static CompanyRole contains(List<CompanyRole> roleList, String roleIdStr) {
            return CollUtil.findOne(roleList, role -> contains(role, roleIdStr));
        }

        /**
         * 编码字符串。
         *
         * @return 编码字符串
         */
        public String getCodeStr() {
            return getCode() + "";
        }

        /**
         * 将角色编码串转换为角色名称串。
         *
         * @param roleIdStr 角色编码串
         * @return 角色名称串
         */
        public static String transferValue(String roleIdStr) {
            String result = null;
            for (CompanyRole companyRole : values()) {
                result = StrUtil.replace(roleIdStr, companyRole.getCodeStr(), companyRole.getValue());
            }
            return result;
        }

    }

    /**
     * 角色状态。
     */
    @Getter
    @AllArgsConstructor
    public enum State {
        /** 已销毁 */
        DESTROY(-1, "已销毁"),
        /** 未开通 */
        NOT_OPEN(0, "未开通"),
        /** 已开通 */
        OPEN(1, "已开通"),
        /** 已入驻 */
        IN(2, "已入驻"),
        ;
        private Integer code;
        private String value;
    }

    /**
     * 可结算节点。
     */
    @Getter
    @AllArgsConstructor
    public enum OrderType {
        /** 订单完成 */
        ORDER_SUCCESS(0, "订单完成"),
        /** 收货完成 */
        RECEIVE(1, "收货完成"),
        ;
        private Integer code;
        private String value;
    }

    /**
     * 结算周期类型。
     */
    @Getter
    @AllArgsConstructor
    public enum DataType {
        /** 每月固定 */
        MONTH_ONLY(0, "每月固定"),
        /** 商品审核完成 */
        GOODS_AUDIT(1, "商品审核完成"),
        ;
        private Integer code;
        private String value;
    }

    /**
     * 开关。
     */
    @Getter
    @AllArgsConstructor
    public enum Switch {
        /** 关 */
        OFF(0, "禁用,无效,否,减少,初始"),
        /** 开 */
        ON(1, "启用,有效,是,增加,修改"),
        ;
        private Integer code;
        private String value;

        /**
         * 根据编码获取枚举。
         *
         * @param code 编码
         * @return 匹配的枚举
         */
        public static Switch getByCode(Integer code) {
            return Stream.of(Switch.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }

        /**
         * 取反。
         *
         * @param code 编码
         * @return 取反后的枚举
         */
        public static Switch reverse(Integer code) {
            Switch aSwitch = getByCode(code);
            if (aSwitch == null) {
                return null;
            }
            return aSwitch == ON ? OFF : ON;
        }
    }
}
