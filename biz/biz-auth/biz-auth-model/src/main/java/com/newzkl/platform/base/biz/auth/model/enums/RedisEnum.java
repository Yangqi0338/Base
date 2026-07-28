package com.newzkl.platform.base.biz.auth.model.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认证授权域 Redis key 枚举。
 *
 * <p>沿用本仓按域复制 {@code RedisEnum} 的既有惯例 (biz-order / biz-finance / biz-goods / biz-market
 * 各持一份)。biz-auth 仅收录 RBAC / 网关鉴权两类 key, key 字符串与 biz-account 原枚举逐字一致,
 * 以保证与线上既有缓存数据共存。</p>
 *
 * @author fang
 */
public class RedisEnum {

    /**
     * Redis key 定义。
     */
    @Getter
    @AllArgsConstructor
    public enum Key {
        /**
         * 账号无权限接口列表前缀, 拼接账号 ID
         */
        UN_PERMISSION_KEY_PREFIX(ModuleEnum.COMMON, "gateway:un_permission:", "账号无权限接口列表前缀"),
        /**
         * 接口清单缓存前缀
         */
        INTERFACE_KEY_PREFIX(ModuleEnum.COMMON, "interface:list:", "接口清单缓存前缀"),
        ;

        private final ModuleEnum module;
        private final String code;
        private final String value;

        /**
         * 格式化取 key。
         *
         * @param params 占位参数
         * @return 完整 key
         */
        public String getCode(Object... params) {
            return String.format(this.getCode(), params);
        }

        /**
         * 取 key (带模块前缀, 模块编码为空则返回裸 key)。
         *
         * @return 完整 key
         */
        public String getCode() {
            if (module == null || StrUtil.isBlank(module.getCode())) {
                return code;
            }
            return module.getCode() + ":" + code;
        }

        @Override
        public String toString() {
            return getCode();
        }
    }
}
