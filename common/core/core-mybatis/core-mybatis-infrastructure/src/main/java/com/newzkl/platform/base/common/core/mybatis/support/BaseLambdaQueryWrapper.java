package com.newzkl.platform.base.common.core.mybatis.support;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import lombok.NoArgsConstructor;

import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@NoArgsConstructor
public class BaseLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {

    public BaseLambdaQueryWrapper(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseLambdaQueryWrapper(T entity, Class<T> entityClass, String sqlSelect, AtomicInteger paramNameSeq, Map<String, Object> paramNameValuePairs, MergeSegments expression, SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
        super(entity);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = expression;
        if (StrUtil.isNotBlank(sqlSelect)) {
            select(entityClass, (t) -> {
                List<String> fields = StrUtil.split(sqlSelect, ",");
                // 判断字段名是否在列表中
                return fields.contains(t.getColumn());
            });
        }

        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }

    public static boolean isBlank(CharSequence cs) {
        if (cs != null) {
            int length = cs.length();

            for (int i = 0; i < length; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    public <R> BaseLambdaQueryWrapper<T> notEmptyEq(SFunction<T, R> column, R val) {
        this.eq(!ObjectUtil.isEmpty(val), column, val);
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> notNullEq(SFunction<T, ?> column, Object val) {
        this.eq(ObjectUtil.isNotEmpty(val), column, val);
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> notNullNe(SFunction<T, R> column, R val) {
        this.ne(ObjectUtil.isNotEmpty(val), column, val);
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> notEmptyIn(SFunction<T, R> column, Collection<?> coll) {
        CollUtil.removeNull(coll);
        if (CollUtil.size(coll) == 1) {
            this.eq(column, CollUtil.getFirst(coll));
        } else {
            this.in(CollUtil.isNotEmpty(coll), column, CollUtil.distinct(coll));
        }
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> notEmptyNotIn(SFunction<T, R> column, Collection<?> coll) {
        CollUtil.removeNull(coll);
        if (CollUtil.size(coll) == 1) {
            this.eq(column, CollUtil.getFirst(coll));
        } else {
            this.notIn(CollUtil.isNotEmpty(coll), column, CollUtil.distinct(coll));
        }
        return this;
    }

    public BaseLambdaQueryWrapper<T> notEmptyIn(SFunction<T, ?> column, String str) {
        return notEmptyIn(column, StrUtil.split(str, CharUtil.COMMA));
    }

    public BaseLambdaQueryWrapper<T> notEmptyLike(SFunction<T, Object> column, Object val) {
        this.like(!ObjectUtil.isEmpty(val), column, val);
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> notEmptyLt(SFunction<T, R> column, R val) {
        this.lt(!ObjectUtil.isEmpty(val), column, val);
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> notEmptyLe(SFunction<T, R> column, R val) {
        this.le(!ObjectUtil.isEmpty(val), column, val);
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> notEmptyGt(SFunction<T, R> column, R val) {
        this.gt(!ObjectUtil.isEmpty(val), column, val);
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> notEmptyGe(SFunction<T, R> column, R val) {
        this.ge(!ObjectUtil.isEmpty(val), column, val);
        return this;
    }

    public BaseLambdaQueryWrapper<T> jsonLike(SFunction<T, ?> column, String key, String value) {
        return jsonLike(column, key, false, value);
    }

    public BaseLambdaQueryWrapper<T> jsonEq(SFunction<T, ?> column, String key, Object value) {
        return jsonEq(column, key, false, value);
    }

    public BaseLambdaQueryWrapper<T> jsonIn(SFunction<T, ?> column, String key, String value) {
        return jsonIn(column, key, false, value);
    }

    public BaseLambdaQueryWrapper<T> jsonLike(SFunction<T, ?> column, String key, Boolean isArray, String value) {
        if (StrUtil.isBlank(value)) return this;
        String fieldName = this.columnToString(column);
        String likeValue = StrUtil.concat(true, "%", value, "%");
        if (isArray == true) {
            // TODO
            this.apply(StrUtil.format("{}->>'$[*].{}' LIKE {0}", fieldName, key), likeValue);
        } else {
            this.apply(StrUtil.format("{}->>'$.{}' LIKE {0}", fieldName, key), likeValue);
        }
        return this;
    }

    public BaseLambdaQueryWrapper<T> jsonEq(SFunction<T, ?> column, String key, Boolean isArray, Object value) {
        if (ObjectUtil.isEmpty(value)) return this;
        String fieldName = this.columnToString(column);
        if (isArray == true) {
            this.apply(StrUtil.format("JSON_SEARCH({}, 'one', {0}, NULL, '$[*].{}') IS NOT NULL", fieldName, key), value);
        } else {
            this.apply(StrUtil.format("{}->>'$.{}' = {0}", fieldName, key), value);
        }
        return this;
    }

    public BaseLambdaQueryWrapper<T> jsonIn(SFunction<T, ?> column, String key, Boolean isArray, String value) {
        if (StrUtil.isBlank(value)) return this;
        String jsonValue = value;
        List<String> list = StrUtil.split(value, CharUtil.COMMA);
        if (list.size() > 1) {
            jsonValue = list.stream()
                    .map(str -> "\"" + str + "\"")
                    .reduce((a, b) -> a + ", " + b)
                    .orElse(jsonValue);
        }
        String fieldName = this.columnToString(column);
        this.apply("{0}({1}->>'${2}.{3}', JSON_ARRAY({4}))", isArray == true ? "JSON_OVERLAPS" : "JSON_CONTAINS", fieldName, isArray ? "[*]" : "", key, jsonValue);
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> between(SFunction<T, R> column, R start, R end) {
        return doBetween(column, start, end);
    }

    public <R> BaseLambdaQueryWrapper<T> between(SFunction<T, R> column, R[] range) {
        List<R> rangeList = CollUtil.newArrayList(range);
        return doBetween(column, CollUtil.getFirst(rangeList), CollUtil.getLast(rangeList));
    }

    public <R,V> BaseLambdaQueryWrapper<T> doBetween(SFunction<T, R> column, V start, V end) {
        boolean s = !ObjectUtil.isEmpty(start);
        boolean s1 = !ObjectUtil.isEmpty(end);
        if (s || s1) {
            this.and(i -> {
                i.ge(s, column, start);
                i.le(s1, column, end);
            });
        }
        return this;
    }

    public BaseLambdaQueryWrapper<T> betweenDate(SFunction<T, Temporal> column, String[] dates) {
        List<String> dateList = CollUtil.newArrayList(dates);
        return doBetween(column, CollUtil.getFirst(dateList), CollUtil.getLast(dateList));
    }

    public BaseQueryWrapper<T> orderBy(QuerySupport querySupport) {
        BaseQueryWrapper<T> queryWrapper = this.unwrap();
        querySupport.doSortHandle((field, mode) -> {
            queryWrapper.orderBy(StrUtil.isNotBlank(field), "ASC".equalsIgnoreCase(mode), field);
        });
        return queryWrapper;
    }

    @SafeVarargs
    public final BaseLambdaQueryWrapper<T> andLike(String value, SFunction<T, String>... columns) {
        // 最好不用这个,容易导致索引失效
        // 推荐使用虚拟列+虚拟索引
        if (StrUtil.isBlank(value)) {
            return this;
        }
        this.and(wrapper -> {
            wrapper.and(i -> {
                for (int j = 0; j < columns.length; j++) {
                    i.like(columns[j], value).or(j < columns.length - 1);
                }
            });
        });
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> notNull(SFunction<T, R> column) {
        this.isNotNull(column);
        this.ne(column, "");
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> isNull(SFunction<T, R> column) {
        // 字符串字段名 <= ''
        // ✔ 当字段为 NULL → NULL <= '' → 结果为 NULL → 满足条件，命中查询 ✔
        // ✔ 当字段为 '' → '' <= '' → 结果为 true → 满足条件，命中查询 ✔
        this.le(column, "");
        return this;
    }

    /**
     * findInSet
     * 转为
     * and ( FIND_IN_SET('1',column) or FIND_IN_SET('2',column) )
     *
     * @param column 字典
     * @param value  1,2
     * @return
     */
    public <R> BaseLambdaQueryWrapper<T> findInSet(SFunction<T, R> column, String value) {
        if (StrUtil.isBlank(value)) {
            return this;
        }
        // 待完善
        List<String> split = StrUtil.split(value, CharUtil.COMMA);
        this.and(wrapper -> {
            wrapper.and(i -> {
                for (int k = 0; k < split.size(); k++) {
                    i.apply("FIND_IN_SET({0}," + column + ")", split.get(k)).or(k < split.size() - 1);
                }
            });
        });
        return this;
    }

    /**
     * 模糊搜索一个字段为集合
     *
     * @param columns
     * @param value
     * @return
     */
    public <R> BaseLambdaQueryWrapper<T> likeList(SFunction<T, R> columns, List<?> value) {
        if (CollUtil.isEmpty(value)) {
            return this;
        }
        this.and(wrapper -> {
            wrapper.and(i -> {
                for (int j = 0; j < value.size(); j++) {
                    i.like(columns, value.get(j)).or(j < value.size() - 1);
                }
            });
        });
        return this;
    }

    /**
     * 模糊搜索一个字段为集合
     *
     * @param columns
     * @param value
     * @return
     */
    public <R> BaseLambdaQueryWrapper<T> likeList(SFunction<T, R> columns, String value) {
        if (StrUtil.isBlank(value)) {
            return this;
        }
        List<String> split = StrUtil.split(value, CharUtil.COMMA);
        this.and(wrapper -> {
            wrapper.and(i -> {
                for (int j = 0; j < split.size(); j++) {
                    i.like(columns, split.get(j)).or(j < split.size() - 1);
                }
            });
        });
        return this;
    }

    /**
     * 模糊搜索一个字段为集合
     *
     * @param columns
     * @param val
     * @return
     */
    public <R> BaseLambdaQueryWrapper<T> likeList(String val, SFunction<T, R>... columns) {
        if (columns.length <= 0 || StrUtil.isEmpty(val)) {
            return this;
        }
        this.and(wrapper -> {
            for (int i = 0; i < columns.length; i++) {
                SFunction<T, R> column = columns[i];
                wrapper.like(column, val).or(i < columns.length - 1);
            }
        });
        return this;
    }

    public BaseQueryWrapper<T> unwrap() {
        return new BaseQueryWrapper<>(null, getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                expression, lastSql, sqlComment, sqlFirst);
    }

    public BaseQueryWrapper<T> unwrap(String alias) {
        return new BaseQueryWrapper<>(null, getEntityClass(), alias, paramNameSeq, paramNameValuePairs,
                expression, lastSql, sqlComment, sqlFirst);
    }

    public BaseQueryWrapper<T> unwrapAlias() {
        return unwrap("t");
    }

    public BaseQueryWrapper<T> select(Class<?> clazz) {
        return this.unwrap().select(clazz);
    }

    public BaseLambdaUpdateWrapper<T> toUpdate() {
        return new BaseLambdaUpdateWrapper<>(this.getEntity(), this.getEntityClass(), paramNameSeq, paramNameValuePairs, expression, lastSql, sqlComment, sqlFirst);
    }

}
