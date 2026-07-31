package com.newzkl.platform.base.common.ddd.model.res;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * openapi 分页响应包装
 * <p>迁移自 new-scm scm-common/common-rpc com.zkl.scm.rpc.model.ApiPage,
 * 通用对外分页契约壳, 上移 ddd-model 共享内核, 与 PlatformResult 并列
 *
 * @param <E> 列表元素类型
 * @author fang
 */
@Data
public class ApiPage<E> implements Serializable {

    /**
     * 页码, 从1开始
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * 总数
     */
    private long total;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 列表数据
     */
    private List<E> list;

    /**
     * 构造分页壳
     *
     * @param list     列表数据
     * @param pageNum  页码
     * @param pageSize 页面大小
     * @param total    总数
     * @param <T>      列表元素类型
     * @return 分页壳
     */
    public static <T> ApiPage<T> of(List<T> list, int pageNum, int pageSize, long total) {
        ApiPage<T> apiPage = new ApiPage<>();
        apiPage.setPageNum(pageNum);
        apiPage.setPageSize(pageSize);
        apiPage.setTotal(total);
        apiPage.setList(list);
        return apiPage;
    }

    /**
     * 转模填充分页
     *
     * @param result   源列表
     * @param function 转换函数
     * @param pageNum  页码
     * @param pageSize 页面大小
     * @param pages    总页数
     * @param total    总数
     * @param <S>      源元素类型
     */
    public <S> void of(List<S> result, Function<S, E> function, int pageNum, int pageSize, int pages, long total) {
        this.list = new ArrayList<>();
        for (S s : result) {
            this.list.add(transfer(s, function));
        }
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = pages;
        this.total = total;
    }

    /**
     * 单元素转换
     *
     * @param s        源元素
     * @param function 转换函数
     * @param <S>      源元素类型
     * @param <E>      目标元素类型
     * @return 目标元素
     */
    public static <S, E> E transfer(S s, Function<S, E> function) {
        return function.apply(s);
    }

    /**
     * 填充分页
     *
     * @param list     列表数据
     * @param pageNum  页码
     * @param pageSize 页面大小
     * @param pages    总页数
     * @param total    总数
     */
    public void of(List<E> list, int pageNum, int pageSize, int pages, long total) {
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = pages;
        this.total = total;
    }
}
