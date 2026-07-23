package com.newzkl.platform.base.biz.store.model.enums;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author fang
 */
public class RoleEnum {

    /**
     * 角色申请数据缓存的 Redis_key
     */
    public static final String ApplyCommandRedisKeyPre = "role:roleApplyCommand:";
    public static final String PLATFORM = "0";

    @Getter
    @AllArgsConstructor
    public enum GuestCompanyRole {
        OPERATOR(-1L, "游客"),
        ADMIN(-2L, "游客"),
        MARKET(-3L, "游客"),
        USER(-4L, "游客"),
        SUPPLIER(-5L, "游客"),
        CHANNEL(-6L, "游客"),
        ;
        private final Long code;
        private final String value;

        public static GuestCompanyRole findByClient(CommonEnum.Client client) {
            return Arrays.stream(values()).filter(item -> item.name().equals(client.name())).findFirst().orElse(null);
        }

        public static boolean isGuest(Long roleId) {
            return Arrays.stream(GuestCompanyRole.values()).anyMatch(it -> it.getCode().equals(roleId));
        }
    }

    @Getter
    @AllArgsConstructor
    public enum CompanyRole {
        /* 平台管理 */
        PLATFORM(0L, "平台管理员", CommonEnum.Client.ADMIN),

        /* 用户 */
        MEMBER(1000L, "C端客户", CommonEnum.Client.USER),

        /* 供应商 */
        SUPPLIER(1001L, "供应商", CommonEnum.Client.SUPPLIER),

        /* 渠道商 */
        CHANNEL(1002L, "渠道商", CommonEnum.Client.CHANNEL),
        PARTNER(1003L, "合伙人", CommonEnum.Client.CHANNEL),
        MERCHANT(1007L, "商户", CommonEnum.Client.CHANNEL),

        /* 运营商 */
        OPERATOR(1004L, "运营商", CommonEnum.Client.OPERATOR),
        DEALER(1005L, "交易师", CommonEnum.Client.OPERATOR),
        SELECTOR(1006L, "甄选师", CommonEnum.Client.OPERATOR),
        OPERATOR_GUEST(-1L, "游客", CommonEnum.Client.OPERATOR),

        ;
        private final Long code;
        private final String value;
        private final CommonEnum.Client client;

        public static RoleEnum.CompanyRole getByCode(Long code) {
            return Stream.of(RoleEnum.CompanyRole.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }

        public static List<Long> findClientRoleIdList(CommonEnum.Client client) {
            return Arrays.stream(CompanyRole.values()).filter(it -> it.getClient().equals(client)).map(CompanyRole::getCode).collect(Collectors.toList());
        }

        public static List<CompanyRole> findClientRoleList(CommonEnum.Client client) {
            return Arrays.stream(CompanyRole.values()).filter(it -> it.getClient().equals(client)).collect(Collectors.toList());
        }

        public static List<CompanyRole> findClientRoleList(CommonEnum.Client client, String roleIdStr) {
            List<CompanyRole> companyRoleList;
            if (client == null) {
                companyRoleList = CollUtil.newArrayList(CompanyRole.values());
            } else {
                companyRoleList = findClientRoleList(client);
            }
            Map<String, CompanyRole> companyRoleMap = CollUtil.toMap(companyRoleList, new HashMap<>(16), it -> it.getCode().toString());
            if (MapUtil.isEmpty(companyRoleMap) || StrUtil.isBlank(roleIdStr)) {
                return new ArrayList<>();
            }
            List<CompanyRole> roleList = StrUtil.split(roleIdStr, ",").stream()
                    .map(companyRoleMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(CompanyRole::getClient, TreeMap::new, Collectors.toList()))
                    .values().stream().map(CollUtil::getLast)
                    .filter(Objects::nonNull).collect(Collectors.toList());
            return roleList;
        }

        public static List<CompanyRole> getLevelUpEnumList() {
            return findClientRoleList(CommonEnum.Client.OPERATOR);
        }

        public static List<CompanyRole> getLevelUpEnumList(String roleIdStr) {
            List<CompanyRole> companyRoleList = getLevelUpEnumList();
            return companyRoleList.stream().filter(it -> roleIdStr.contains(it.getCode().toString())).collect(Collectors.toList());
        }

        public static List<Long> getLevelUpRoleList(List<String> roleIdStrList) {
            if (CollUtil.isEmpty(roleIdStrList)) {
                return Collections.emptyList();
            }
            List<CompanyRole> companyRoleList = getLevelUpEnumList();
            return roleIdStrList.stream().map(roleIdStr -> companyRoleList.stream().filter(it ->
                            roleIdStr.contains(it.getCode().toString())
                    ).findFirst().map(CompanyRole::getCode).orElse(null)
            ).filter(Objects::nonNull).collect(Collectors.toList());
        }

        public static List<CompanyRole> getNextLevelUpEnumList(Long code) {
            // 根据设定顺序倒序排列
            List<CompanyRole> companyRoleList = getLevelUpEnumList().stream()
                    .sorted(Comparator.comparing(CompanyRole::ordinal).reversed())
                    .collect(Collectors.toList());
            if (code != null) {
                companyRoleList = CollUtil.sub(companyRoleList, companyRoleList.indexOf(getByCode(code)) + 1, companyRoleList.size());
            }
            return companyRoleList;
        }

        public static List<CompanyRole> getNextEnumList(CommonEnum.Client client, Long code) {
            // 根据设定顺序倒序排列
            List<CompanyRole> companyRoleList = findClientRoleList(client).stream()
                    .sorted(Comparator.comparing(CompanyRole::ordinal).reversed())
                    .collect(Collectors.toList());
            if (code != null) {
                companyRoleList = CollUtil.sub(companyRoleList, companyRoleList.indexOf(getByCode(code)) + 1, companyRoleList.size());
            }
            return companyRoleList;
        }
    }
    //角色状态
    @Getter
    @AllArgsConstructor
    public enum State {
        DESTROY(-1,"已销毁"),
        NOT_OPEN(0,"未开通"),
        OPEN(1,"已开通"),
        IN(2,"已入驻"),
        ;
        private Integer code;
        private String value;
    }
    //可结算节点
    @Getter
    @AllArgsConstructor
    public enum OrderType {
        ORDER_SUCCESS(0,"订单完成"),
        RECEIVE(1,"收货完成"),
        ;
        private Integer code;
        private String value;
    }
    //结算周期类型
    @Getter
    @AllArgsConstructor
    public enum DataType {
        MONTH_ONLY(0,"每月固定"),
        GOODS_AUDIT(1,"商品审核完成"),
        ;
        private Integer code;
        private String value;
    }
}

