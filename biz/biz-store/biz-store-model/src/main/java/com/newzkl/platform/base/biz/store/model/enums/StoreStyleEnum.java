package com.newzkl.platform.base.biz.store.model.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 门店样式枚举
 */
@Data
public class StoreStyleEnum {

    //类型
    @Getter
    @AllArgsConstructor
    public enum Type {
        OTHERS(0,"其它"),
        DEFAULT(1,"默认"),
        ;
        private Integer code;
        private String value;
    }

    //页面类型
    @Getter
    @AllArgsConstructor
    public enum PageType {
        HOME_PAGE("首页"),
        ;
        private String value;
    }

}
