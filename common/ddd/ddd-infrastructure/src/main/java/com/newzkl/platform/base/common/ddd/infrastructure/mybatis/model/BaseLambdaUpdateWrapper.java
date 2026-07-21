package com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@NoArgsConstructor
public class BaseLambdaUpdateWrapper<T> extends LambdaUpdateWrapper<T> {

    public BaseLambdaUpdateWrapper(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseLambdaUpdateWrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq, Map<String, Object> paramNameValuePairs, MergeSegments expression, SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
        super(entity);
        setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = expression;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }

}
