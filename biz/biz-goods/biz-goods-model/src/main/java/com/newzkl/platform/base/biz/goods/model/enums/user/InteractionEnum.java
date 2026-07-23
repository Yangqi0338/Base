package com.newzkl.platform.base.biz.goods.model.enums.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 互动枚举
 */
public class InteractionEnum {

    /**
     * 被操作对象类型
     */
    @Getter
    @AllArgsConstructor
    public enum TargetTypeEnum {
        /**
         * 用户视频
         */
        USER_VIDEO("USER_VIDEO", "用户视频"),
        /** 商品 */
        PRODUCT("PRODUCT", "商品"),
        /** 商品视频 */
        PRODUCT_VIDEO("PRODUCT_VIDEO", "商品视频"),
        /**
         * 达人视频
         */
        INFLUENCER_VIDEO("INFLUENCER_VIDEO", "达人视频"),
        /**
         * 文章
         */
        ARTICLE("ARTICLE", "文章"),
        ;

        @EnumValue
        @JsonValue
        private final String code;
        private final String desc;

        /**
         * 根据编码获取枚举
         */
        public static TargetTypeEnum getByCode(String code) {
            for (TargetTypeEnum type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * 操作类型枚举
     */
    @Getter
    @AllArgsConstructor
    public enum ActionTypeEnum {
        /** 浏览 */
        VIEW("VIEW", "浏览"),
        /** 点赞 */
        LIKE("LIKE", "点赞"),
        /** 转发 */
        SHARE("SHARE", "转发"),
        ;

        @EnumValue
        @JsonValue
        private final String code;
        private final String desc;

        /**
         * 根据编码获取枚举
         */
        public static ActionTypeEnum getByCode(String code) {
            for (ActionTypeEnum type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }

}
