package com.newzkl.platform.base.biz.content.model.common.res;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 内容域分页出参壳
 *
 * <p>旧实现直接外泄 MyBatis-Plus {@code Page} 作为出参。本域 domain 层不得依赖
 * 持久化框架类型, 且 {@code biz-content-domain} 未声明 {@code core-utils}
 * (mybatis-plus 由其传递而来), 故在 model 层自建分页壳。</p>
 *
 * <p><b>字段名与 MyBatis-Plus </b>{@code Page}<b> 逐字一致</b>
 * ({@code records} / {@code total} / {@code size} / {@code current} / {@code pages}),
 * 序列化后的 JSON 结构与旧接口相同, 前端无需改造。</p>
 *
 * @param <T> 记录类型
 * @author KC
 */
@Data
public class ContentPage<T> implements Serializable {

    /**
     * 当前页数据
     */
    private List<T> records = new ArrayList<>();

    /**
     * 总记录数
     */
    private long total;

    /**
     * 每页条数
     */
    private long size;

    /**
     * 当前页码
     */
    private long current;

    /**
     * 总页数
     */
    private long pages;

    /**
     * 构建分页结果
     *
     * @param current 当前页码
     * @param size    每页条数
     * @param total   总记录数
     * @param records 当前页数据
     * @param <T>     记录类型
     * @return 分页结果
     */
    public static <T> ContentPage<T> of(long current, long size, long total, List<T> records) {
        ContentPage<T> page = new ContentPage<>();
        page.setCurrent(current);
        page.setSize(size);
        page.setTotal(total);
        page.setRecords(records == null ? new ArrayList<>() : records);
        page.setPages(size <= 0 ? 0 : (total + size - 1) / size);
        return page;
    }
}
