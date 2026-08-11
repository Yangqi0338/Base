package com.newzkl.platform.base.common.core.mybatis.support;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.*;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.*;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 孔祥基
 * @date 2023/6/12 19:36:07
 * @mail 247967116@qq.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BaseQueryWrapper<T> extends QueryWrapper<T> {

    private QueryWrapper<T> realEw;

    private QueryWrapper<T> sourceEw;

    public BaseQueryWrapper(Class<T> entityClass) {
        super();
        setEntityClass(entityClass);
    }

    public BaseQueryWrapper(T entity, Class<T> entityClass, String alias, AtomicInteger paramNameSeq,
                            Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                            SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
        super(entity);
        super.setEntityClass(entityClass);
        if (alias != null) {
            this.expression = new MergeSegments();

            List<ISqlSegment> segments = new ArrayList<>();
            boolean isField = true;

            for (ISqlSegment segment : mergeSegments.getNormal()) {
                boolean addSegments = true;
                int nowSize = this.expression.getNormal().size();
                if (segment instanceof SqlKeyword) {
                    segments.add(segment);
                    addSegments = false;
                } else {
                    // 非关键字，且遇到的第一个元素被判定为字段名
                    if (isField) {
                        segments.add(columnToSqlSegment(alias + "." + segment.getSqlSegment()));
                        isField = false;
                    } else {
                        segments.add(segment);
                    }
                }
                if (addSegments) {
                    this.appendSqlSegments(ArrayUtil.toArray(segments, ISqlSegment.class));
                }
                // 集合数量不对，说明加入进去了，初始化循环数据
                if (nowSize != this.expression.getNormal().size()) {
                    isField = true;
                    segments = new ArrayList<>();
                }
            }
        } else {
            this.expression = mergeSegments;
        }
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }

    public BaseQueryWrapper<T> notEmptyEq(String column, Object val) {
        this.eq(!ObjectUtil.isEmpty(val), column, val);
        return this;
    }

    public BaseQueryWrapper<T> notEmptyNe(String column, Object val) {
        this.ne(!ObjectUtil.isEmpty(val), column, val);
        return this;
    }

    public BaseQueryWrapper<T> notNullEq(String column, Object val) {
        this.eq(ObjectUtil.isNotEmpty(val), column, val);
        return this;
    }

    public BaseQueryWrapper<T> notEmptyIn(String column, Collection<?> coll) {
        this.in(CollUtil.isNotEmpty(coll), column, coll);
        return this;
    }

    public BaseQueryWrapper<T> notEmptyIn(String column, String str) {
        this.in(StrUtil.isNotBlank(str), column, StrUtil.split(str, CharUtil.COMMA));
        return this;
    }

    public BaseQueryWrapper<T> notEmptyLike(String column, String val) {
        this.like(StrUtil.isNotBlank(val), column, val);
        return this;
    }

    public BaseQueryWrapper<T> between(String column, String[] dates) {
        String[] newDates = ArrayUtil.removeBlank(dates);
        if (ArrayUtil.isNotEmpty(newDates)) {
            this.and(i -> {
                String date = dates[0];
                i.ge(!ObjectUtil.isEmpty(date), column, date);
                if (dates.length > 1) {
                    i.le(!ObjectUtil.isEmpty(dates[1]), column, dates[1]);
                }
            });
        }
        return this;
    }

    public BaseQueryWrapper<T> between(String column, Date[] dates) {
        Date[] newDates = ArrayUtil.removeNull(dates);
        if (ArrayUtil.isNotEmpty(newDates)) {
            this.and(i -> {
                i.ge(!ObjectUtil.isEmpty(dates[0]), column, dates[0]);
                if (dates.length > 1) {
                    i.lt(!ObjectUtil.isEmpty(dates[1]), column, dates[1]);
                }
            });
        }
        return this;
    }

    public BaseQueryWrapper<T> andLike(String value, String... columns) {
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

    public BaseQueryWrapper<T> notNull(String column) {
        this.isNotNull(column);
        this.ne(column, "");
        return this;
    }

    public BaseQueryWrapper<T> isNullStr(String column) {
        this.and(qw -> qw.isNull(column).or(qw2 -> qw2.eq(column, "")));
        return this;
    }

    public BaseQueryWrapper<T> isNullStrEq(String column, String value) {
        this.and(qw -> qw.isNull(column).or(qw2 -> qw2.eq(column, value)));
        return this;
    }

    public BaseQueryWrapper<T> isNotNullStr(String column) {
        this.and(qw -> qw.isNotNull(column).and(qw2 -> qw2.ne(column, "")));
        return this;
    }

    /**
     * findInSet
     * 转为
     * and ( FIND_IN_SET('1',column) or FIND_IN_SET('2',column) )
     *
     * @param column 字段
     * @param value  1,2
     * @return
     */
    public BaseQueryWrapper<T> findInSet(String column, String value) {
        if (StrUtil.isBlank(value)) {
            return this;
        }
        List<String> split = StrUtil.split(value, CharUtil.COMMA);
        return findInSet(column, split);
    }

    /**
     * findInSet
     * 转为
     * and ( FIND_IN_SET('1',column) or FIND_IN_SET('2',column) )
     *
     * @param column 字段
     * @param split  1,2
     * @return
     */
    public BaseQueryWrapper<T> findInSet(String column, List<String> split) {
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
     * @param val
     * @return
     */
    public BaseQueryWrapper<T> likeList(String columns, List<String> val) {
        if (ArrayUtil.isEmpty(columns) || CollUtil.isEmpty(val)) {
            return this;
        }
        this.and(wrapper -> {
            wrapper.and(i -> {
                for (int j = 0; j < val.size(); j++) {
                    i.like(columns, val.get(j)).or(j < val.size() - 1);
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
    public BaseQueryWrapper<T> likeList(String val, String... columns) {
        if (ArrayUtil.isEmpty(columns) || StrUtil.isEmpty(val)) {
            return this;
        }
        this.and(wrapper -> {
            for (int i = 0; i < columns.length; i++) {
                String column = columns[i];
                wrapper.like(column, val).or(i < columns.length - 1);
            }
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
    public BaseQueryWrapper<T> likeList(String columns, String val) {
        if (ObjectUtil.isEmpty(val)) {
            return this;
        }
        return this.likeList(columns, StrUtil.split(val, CharUtil.COMMA));
    }

    public BaseQueryWrapper<T> jsonLike(String fieldName, String key, Boolean isArray, Object value) {
        if (ObjectUtil.isEmpty(value)) return this;
        if (isArray == true) {
            // TODO
            this.apply(StrUtil.format("{}->>'$[*].{}' LIKE '%{}%'", fieldName, key, value));
        } else {
            this.apply(StrUtil.format("{}->>'$.{}' LIKE '%{}%'", fieldName, key, value));
        }
        return this;
    }

    public BaseQueryWrapper<T> jsonEq(String fieldName, String key, Boolean isArray, Object value) {
        if (ObjectUtil.isEmpty(value)) return this;
        if (isArray == true) {
            this.apply(StrUtil.format("JSON_SEARCH({}, 'one', {0}, NULL, '$[*].{}') IS NOT NULL", fieldName, key), value);
        } else {
            this.apply(StrUtil.format("{}->>'$.{}' = {0}", fieldName, key), value);
        }
        return this;
    }

    public BaseQueryWrapper<T> jsonIn(String fieldName, String key, Boolean isArray, String value) {
        String jsonValue = value;
        List<String> list = StrUtil.split(value, CharUtil.COMMA);
        if (list.size() > 1) {
            jsonValue = list.stream()
                    .map(str -> "\"" + str + "\"")
                    .reduce((a, b) -> a + ", " + b)
                    .orElse(jsonValue);
        }
        this.apply("{0}({1}->>'${2}.{3}', JSON_ARRAY({4}))", isArray == true ? "JSON_OVERLAPS" : "JSON_CONTAINS", fieldName, isArray ? "[*]" : "", key, jsonValue);
        return this;
    }

    public BaseQueryWrapper<T> apply(String applySql, Object... params) {
        this.apply(ObjectUtil.isAllNotEmpty(params), applySql, params);
        return this;
    }

    public String getCustomConditionSqlSegment() {
        MergeSegments expression = getExpression();
        if (Objects.nonNull(expression)) {
            NormalSegmentList normal = expression.getNormal();
            if (normal.isEmpty()) {
                return StringPool.EMPTY;
            } else {
                return Constants.AND + StringPool.SPACE + normal.getSqlSegment();
            }
        }
        return StringPool.EMPTY;
    }

    public String getCustomOrderSqlSegment() {
        MergeSegments expression = getExpression();
        if (Objects.nonNull(expression)) {
            OrderBySegmentList orderBy = expression.getOrderBy();
            if (orderBy.isEmpty()) {
                return StringPool.EMPTY;
            } else {
                return orderBy.getSqlSegment();
            }
        }
        return StringPool.EMPTY;
    }

    public String getCustomGroupSqlSegment() {
        MergeSegments expression = getExpression();
        if (Objects.nonNull(expression)) {
            GroupBySegmentList groupBy = expression.getGroupBy();
            if (groupBy.isEmpty()) {
                return StringPool.EMPTY;
            } else {
                return groupBy.getSqlSegment();
            }
        }
        return StringPool.EMPTY;
    }

    public String getCustomHavingSqlSegment() {
        MergeSegments expression = getExpression();
        if (Objects.nonNull(expression)) {
            HavingSegmentList having = expression.getHaving();
            if (having.isEmpty()) {
                return StringPool.EMPTY;
            } else {
                return having.getSqlSegment();
            }
        }
        return StringPool.EMPTY;
    }

    public <M extends BaseMapper<T>> BaseQueryWrapper<T> setEntityClass(M mapper) {
        Class<T> entityClass = (Class<T>) ReflectionKit.getSuperClassGenericType(mapper.getClass(), BaseMapper.class, 0);
        this.setEntityClass(entityClass);
        return this;
    }

    public BaseQueryWrapper<T> select(Class<?> clazz) {
        Class<T> entityClass = getEntityClass();
        if (entityClass == null || entityClass.isAssignableFrom(clazz)) return this;
        Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(entityClass);

        List<String> fieldList = Arrays.stream(ReflectUtil.getFields(clazz))
                // 只有与entityClass相同的字段才可以进行查询, 否则会报错
                .filter(it -> columnMap.containsKey(it.getName()))
                .map(it -> StrUtil.toUnderlineCase(it.getName()))
                .distinct().collect(Collectors.toList());
        this.select(fieldList);
        return this;
    }

    @Override
    public BaseQueryWrapper<T> clone() {
        BaseQueryWrapper<T> queryWrapper = new BaseQueryWrapper<>(getEntity(), getEntityClass(), null,
                new AtomicInteger(paramNameSeq.get()), new HashMap<>(paramNameValuePairs),
                TransferUtils.transfer(this.expression, MergeSegments.class),
                Opt.ofNullable(lastSql).map(it -> new SharedString(it.getStringValue())).orElse(null),
                Opt.ofNullable(sqlComment).map(it -> new SharedString(it.getStringValue())).orElse(null),
                Opt.ofNullable(sqlFirst).map(it -> new SharedString(it.getStringValue())).orElse(null)
        );
        queryWrapper.setSourceEw(this);
        return queryWrapper;
    }


    @Override
    public BaseLambdaQueryWrapper<T> lambda() {
        return new BaseLambdaQueryWrapper<T>(this.getEntity(), this.getEntityClass(), this.getSqlSelect(), this.paramNameSeq, this.paramNameValuePairs, this.expression, this.lastSql, this.sqlComment, this.sqlFirst);
    }
}
