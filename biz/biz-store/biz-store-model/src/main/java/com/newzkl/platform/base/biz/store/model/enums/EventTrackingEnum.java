package com.newzkl.platform.base.biz.store.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 指标枚举
 *
 * @author fang
 */
public class EventTrackingEnum {

    /**
     * 店铺
     */
    @Getter
    @AllArgsConstructor
    public enum Store {
        VIEW(0, "浏览店铺"),
        ;
        private final Integer code;
        private final String value;

        public Integer getCode() {
            return code + IStore.plus;
        }
    }

    public interface IStore {
        Integer plus = 0;
    }
}

