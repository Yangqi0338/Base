package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;

import lombok.Data;

/**
 * 会订货接口响应基类（泛型抽象公共字段）
 * @param <T> 具体接口的data数据类型
 */
@Data
public class HuiDingHuoBaseRes<T> {
    private String code; // 返回代码
    private String message; // 返回消息内容
    private Integer success; // 是否成功 0不成功 1成功
    private T data; // 具体数据内容（泛型，由子类指定类型）
}
