package com.newzkl.platform.base.biz.finance.model.enums.goods;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kc
 * @description: 视频
 */
public class VideoEnum {

    @AllArgsConstructor
    @Getter
    public enum Type {
        /**
         * 短视频
         */
        SHORT(1, "短视频"),
        FAMOUS(2, "达人视频"),
        ;

        @EnumValue
        @JsonValue
        private final Integer value;
        private final String desc;
    }
}
