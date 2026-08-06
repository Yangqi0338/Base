package com.newzkl.platform.base.common.ddd.infrastructure.support;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.newzkl.platform.base.common.core.model.money.Money;
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

    public <R> BaseLambdaUpdateWrapper<T> notEmptySet(SFunction<T, R> column, R value) {
        this.set(!ObjectUtil.isNotEmpty(value), column, value);
        return this;
    }

    public <R> BaseLambdaUpdateWrapper<T> append(SFunction<T, R> column, R value) {
        return this.append(column, value,',');
    }

    public <R> BaseLambdaUpdateWrapper<T> append(SFunction<T, R> column, R value, Character separator) {
        boolean condition = !ObjectUtil.isNotEmpty(value);
        if (condition) {
            String realColumn = columnToString(column);
            this.setSql(String.format("CONCAT(%s, '%s', '%s'", realColumn, separator, value));
        }
        return this;
    }

    public BaseLambdaUpdateWrapper<T> setIncrBy(SFunction<T, ?> column, Money money) {
        super.setIncrBy(money.greaterThanZero(), column, money.getCent());
        return this;
    }
}
