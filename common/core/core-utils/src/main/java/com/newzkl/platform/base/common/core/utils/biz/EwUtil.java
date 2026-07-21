package com.newzkl.platform.base.common.core.utils.biz;

import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * MyBatis-Plus Lambda 条件构造辅助工具。
 *
 * @author fang
 */
public class EwUtil {

    /**
     * 将DTO的SFunction转换为DO的SFunction（基于字段名匹配）。
     *
     * @param dtoFunction DTO的字段函数
     * @param doClass     DO的Class对象
     * @param <DTO>       DTO 类型
     * @param <DO>        DO 类型
     * @param <R>         字段值类型
     * @return DO的字段函数
     */
    @SuppressWarnings("unchecked")
    public static <DTO, DO, R> SFunction<DO, R> convertToDoFunction(
            Function<DTO, R> dtoFunction,
            Class<DO> doClass
    ) {
        String fieldName = LambdaUtil.getFieldName(dtoFunction::apply);
        return doObj -> {
            try {
                return (R) ReflectUtil.getFieldValue(doClass, fieldName);
            } catch (Exception e) {
                throw new RuntimeException("调用DO getter方法失败", e);
            }
        };
    }

    public static <DTO, DO> Wrapper<DO> domain2DO(Wrapper<DTO> dtoFunction, Class<DO> doClass) {
        return new LambdaQueryWrapper<>();
    }

    @SuppressWarnings("unchecked")
    private static <DTO, T> LambdaQueryWrapper<T> wrapCondition(LambdaQueryWrapper<DTO> dtoQueryWrapper, Class<T> clazz) {
        LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>();
        try {
            Field conditionListField = AbstractLambdaWrapper.class.getDeclaredField("conditionList");
            conditionListField.setAccessible(true);
            List<?> conditionList = (List<?>) conditionListField.get(dtoQueryWrapper);

            for (Object condition : conditionList) {
                java.lang.reflect.Field columnField = condition.getClass().getDeclaredField("column");
                java.lang.reflect.Field valField = condition.getClass().getDeclaredField("val");
                java.lang.reflect.Field operatorField = condition.getClass().getDeclaredField("operator");
                columnField.setAccessible(true);
                valField.setAccessible(true);
                operatorField.setAccessible(true);

                SFunction<DTO, ?> dtoFunction = (SFunction<DTO, ?>) columnField.get(condition);
                Object val = valField.get(condition);
                String operator = (String) operatorField.get(condition);

                SFunction<T, ?> doFunction = convertToDoFunction(dtoFunction, clazz);

                invokeDoQueryWrapperMethod(queryWrapper, operator, doFunction, val);
            }
        } catch (Exception e) {
            throw new RuntimeException("转换DTO Lambda条件到DO失败", e);
        }
        return queryWrapper;
    }

    @SuppressWarnings("unchecked")
    private static <DTO, T> void invokeDoQueryWrapperMethod(
            LambdaQueryWrapper<T> doQueryWrapper,
            String operator,
            SFunction<T, ?> doFunction,
            Object val
    ) throws Exception {
        switch (operator) {
            case "EQ":
                doQueryWrapper.eq(doFunction, val);
                break;
            case "NE":
                doQueryWrapper.ne(doFunction, val);
                break;
            case "GT":
                doQueryWrapper.gt(doFunction, val);
                break;
            case "GE":
                doQueryWrapper.ge(doFunction, val);
                break;
            case "LT":
                doQueryWrapper.lt(doFunction, val);
                break;
            case "LE":
                doQueryWrapper.le(doFunction, val);
                break;
            case "LIKE":
                doQueryWrapper.like(doFunction, val);
                break;
            case "LIKE_LEFT":
                doQueryWrapper.likeLeft(doFunction, val);
                break;
            case "LIKE_RIGHT":
                doQueryWrapper.likeRight(doFunction, val);
                break;
            default:
                throw new UnsupportedOperationException("不支持的查询操作符：" + operator);
        }
    }

    public static <T> String getColumn(SFunction<T, ?> column) {
        LambdaMeta meta = LambdaUtils.extract(column);
        Map<String, ColumnCache> fieldInfoMap = LambdaUtils.getColumnMap(meta.getInstantiatedClass());
        String propertyName = meta.getImplMethodName();
        ColumnCache columnCache = fieldInfoMap.get(propertyName);
        return columnCache != null ? columnCache.getColumn() : propertyName;
    }

    public static <T> String incrSql(SFunction<T, ?> column) {
        return buildIncrementSql(column, 1);
    }

    /**
     * 构建自增SQL片段（如send_count = send_count + 1）。
     *
     * @param column    Lambda表达式
     * @param increment 增量
     * @param <T>       实体类型
     * @return 拼接后的SQL片段
     */
    public static <T> String buildIncrementSql(SFunction<T, ?> column, int increment) {
        String columnName = getColumn(column);
        return String.format("%s = %s + %d", columnName, columnName, increment);
    }
}
