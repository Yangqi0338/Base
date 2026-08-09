package com.newzkl.platform.base.biz.socialbang.model.im.query;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 敏感词分页查询
 *
 * <p>迁移自 {@code com.zkl.scm.im.base.tencent.req.SensitiveWordQueryDTO}。
 * 源类直接继承 mybatis-plus {@code Page}, 分页入参为 {@code current}/{@code size};
 * 本仓分页基类为 {@code BizPageQuery}({@code pageNo}/{@code pageSize})。
 * 为不破坏源已有请求体, 保留 {@code current}/{@code size} 别名读写, 内部落到 pageNo/pageSize。</p>
 *
 * <p>偏离说明: 源标量字段 {@code sortField}(String) 与本仓 {@code QuerySupport.sortField}
 * ({@code List<String>}) 同名不同型, 无法共存。排序改走本仓统一机制
 * ({@code sortField}/{@code sortMode} 列表 + {@code orderBy(query)}), 默认值 create_time desc
 * 与源一致; 源 {@code sortType} 字段一并去除, 排序方向用 {@code sortMode}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SensitiveWordQuery extends BizPageQuery {

    /**
     * 敏感词(模糊匹配)
     */
    private String sensitiveWord;

    /**
     * 来源类型
     * @ext manual-手动添加 batch-批量导入
     */
    private String sourceType;

    /**
     * 添加时间-开始
     */
    private LocalDateTime startTime;

    /**
     * 添加时间-结束
     */
    private LocalDateTime endTime;

    /**
     * 构造: 未传排序字段时默认按 create_time 倒序(与源 sortField/sortType 默认值一致)
     */
    public SensitiveWordQuery() {
        if (CollUtil.isEmpty(super.getSortField())) {
            super.addDescSortField("create_time");
        }
    }

    /**
     * 兼容源 {@code Page.current} 入参。
     *
     * @return 当前页
     */
    public Integer getCurrent() {
        return getPageNo();
    }

    /**
     * 兼容源 {@code Page.current} 入参。
     *
     * @param current 当前页
     */
    public void setCurrent(Integer current) {
        setPageNo(current);
    }

    /**
     * 兼容源 {@code Page.size} 入参。
     *
     * @return 每页条数
     */
    public Integer getSize() {
        return getPageSize();
    }

    /**
     * 兼容源 {@code Page.size} 入参。
     *
     * @param size 每页条数
     */
    public void setSize(Integer size) {
        setPageSize(size);
    }
}
