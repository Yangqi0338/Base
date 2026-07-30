package com.newzkl.platform.base.biz.sys.model.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * sys 域字典键
 *
 * <p>码值逐字取自 new-scm {@code com.zkl.scm.model.constants.admin.DictEnum.Key},
 * 与 biz-store 的同名枚举一致 (1010/1011/1012)。</p>
 *
 * <p><b>跨域码值分叉告警</b>: Base 的 biz-account / biz-finance 另有一套重编号
 * ({@code ORDER_CONFIG=1002}、{@code CHANNEL_CONFIG=1003}), 与本枚举及 biz-store
 * 不一致。字典 id 即 {@code dict} 表主键, 若两套码值并存, 写入端 (本域
 * {@code /admin/config/channelConfigSet}) 与读取端 (biz-finance
 * {@code AccountPurseConfigRepositoryImpl}) 会各读各的行, 配置静默失效。
 * 此分叉在本次迁移前已存在于 Base, 归属跨域数据口径决策, 需人工统一后收口。</p>
 *
 * @author KC
 */
public class DictEnum {

    /**
     * 字典键
     */
    @Getter
    @AllArgsConstructor
    public enum Key {

        /**
         * 订单配置
         */
        ORDER_CONFIG(1010L, "订单配置"),

        /**
         * 应用配置
         */
        APP_CONFIG(1011L, "应用配置"),

        /**
         * 数智门店设置
         */
        CHANNEL_CONFIG(1012L, "数智门店设置"),
        ;

        /**
         * 字典 id (dict 表主键)
         */
        private final Long code;

        /**
         * 字典含义
         */
        private final String value;

        /**
         * 按码值取枚举
         *
         * @param code 字典 id
         * @return 匹配的枚举, 无匹配返回 null
         */
        public static Key getByCode(Long code) {
            return Stream.of(Key.values())
                    .filter(it -> it.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }
}
