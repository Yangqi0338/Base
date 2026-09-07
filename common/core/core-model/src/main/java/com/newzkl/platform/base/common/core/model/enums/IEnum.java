package com.newzkl.platform.base.common.core.model.enums;

import cn.hutool.core.lang.EnumItem;
import cn.hutool.core.util.StrUtil;

import java.util.List;

/**
 * 通用枚举接口
 * <p>所有业务枚举均实现此接口，配合 MyBatis-Plus @EnumValue 和 Jackson @JsonValue 使用</p>
 *
 * @param <T> code 类型
 */
public interface IEnum<T> extends EnumItem<IEnum<T>> {

    /**
     * 多语义分隔符
     *
     * <p>枚举描述可用该字符串接多套语义, 如 "是|启用", 由语义位下标决定取哪一套</p>
     */
    char SEMANTIC_SEPARATOR = '|';

    /**
     * 获取枚举编码
     * @ext 持久化值
     * @return 枚举编码
     */
    T getCode();

    default String getCodeStr(){
        return getCode().toString();
    }

    /**
     * 获取枚举描述
     *
     * @return 枚举描述字符串
     */
    String getValue();

    /**
     * 按语义位获取枚举描述
     *
     * <p>描述含 {@link #SEMANTIC_SEPARATOR} 时按下标截取对应语义, 无分隔符则原样返回, 下标越界回落首位</p>
     *
     * @param index 语义位下标, 从 0 开始
     * @return 指定语义位的枚举描述
     */
    default String getValue(int index) {
        String text = getValue();
        if (!StrUtil.contains(text, SEMANTIC_SEPARATOR)) {
            return text;
        }
        List<String> parts = StrUtil.split(text, SEMANTIC_SEPARATOR);
        return index >= 0 && index < parts.size() ? parts.get(index) : parts.get(0);
    }

    default int intVal(){
        T code = getCode();
        if (code instanceof Integer){
            return (Integer) code;
        }else {
            return -1;
        }
    }

    @Override
    default IEnum<T> fromInt(Integer intVal) {
        IEnum<T> tiEnum = null;
        T code = getCode();
        // 若code是int类型，则必须完全匹配
        if (code instanceof Integer){
            tiEnum = EnumItem.super.fromInt(intVal);
        }else {
            // 不是int类型，根据ordinal判断
            IEnum<T>[] vs = items();
            for (int i = 0; i < vs.length; i++) {
                if (intVal == i) {
                    tiEnum = vs[i];
                }
            }
        }
        return tiEnum;
    }

    @Override
    default IEnum<T> fromStr(String strVal) {
        // 根据名字和code来判断
        IEnum<T> tiEnum = EnumItem.super.fromStr(strVal);
        if (tiEnum == null) {
            IEnum<T>[] vs = items();
            for (IEnum<T> enumItem : vs) {
                if (strVal.equals(enumItem.getCodeStr())) {
                    tiEnum = enumItem;
                }
            }
        }
        return tiEnum;
    }
}
