package com.newzkl.platform.base.common.ddd.model.query;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.copier.Copier;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author god
 */
@Data
public class QuerySupport {
    /**
     * 排序字段
     */
    private List<String> sortField;
    /**
     * 排序方式 : 与排序字段按下标一一对应
     */
    private List<String> sortMode;
    /**
     * 分组字段
     */
    @JsonIgnore
    private List<String> groupField;
    /*
     * 字段
     * */
    private List<String> field;

    /**
     * 根据内部枚举类和编码获取真实排序字段名
     *
     * @param <E>      枚举类型（必须实现SortField）
     * @param innerClz 内部枚举类
     * @param code     排序字段编码
     * @return 真实字段名，无匹配返回编码字符串
     */
    private static <E extends Enum<E> & SortField> String getRealFieldByCode(Class<?> innerClz, Integer code) {
        Class<E> clazz = (Class<E>) innerClz;
        SortField instance = EnumUtil.getBy(clazz, (enumInstance) -> {
            if (enumInstance != null) {
                String field = enumInstance.getField(code);
                // 找到匹配的field
                return field != null;
            }
            return false;
        });
        return Opt.ofNullable(instance).map(it -> it.getField(code)).orElse(code + "");
    }

    /**
     * 初始化默认排序字段
     * @ext 当sortField为空时才设置
     * @param field  排序字段名
     * @param isDesc 是否降序
     */
    public void initSortField(String field, boolean isDesc) {
        if (CollUtil.isNotEmpty(sortField)) {
            return;
        }
        if (isDesc) {
            addDescSortField(field);
        } else {
            addSortField(field);
        }
    }

    /**
     * 设置排序字段列表
     * @ext 追加方式，每个字段单独解析
     * @param sortField 排序字段列表
     */
    public void setSortField(List<String> sortField) {
        if (CollUtil.isEmpty(sortField)) return;
        if (this.sortField == null) {
            this.sortField = CollUtil.newArrayList();
        }
        sortField.forEach(this::doAddSortField);
    }

    /**
     * 内部方法：将单个排序字段添加到列表
     * @ext 自动解析枚举编码
     * @param field 排序字段名或枚举编码
     */
    private void doAddSortField(String field) {
        if (StrUtil.isBlank(field)) return;
        if (NumberUtil.isNumber(field)) {
            try {
                Class<?> subClass = this.getClass();
                // 1. 遍历子类的所有内部类（包括私有）
                Class<?>[] innerClasses = subClass.getDeclaredClasses();
                for (Class<?> innerClz : innerClasses) {
                    // 2. 筛选条件：①是枚举类型 ②实现SortField
                    if (innerClz.isEnum() && SortField.class.isAssignableFrom(innerClz)) {
                        field = getRealFieldByCode(innerClz, NumberUtil.parseInt(field));
                    }
                }
            } catch (Exception ignored) {
            }
        }
        sortField.add(field);
    }

    /**
     * 添加降序排序字段
     *
     * @param field 排序字段名
     */
    public void addDescSortField(String field) {
        addSortField(field, true);
    }
    public <T> void addDescSortField(Function<T,?> function) {
        addSortField(LambdaUtil.getFieldName(function::apply), true);
    }

    /**
     * 添加升序排序字段
     *
     * @param field 排序字段名
     */
    public void addSortField(String field) {
        addSortField(field, false);
    }

    /**
     * 添加排序字段，可指定排序方向
     *
     * @param field  排序字段名
     * @param isDesc 是否降序
     */
    public void addSortField(String field, boolean isDesc) {
        this.sortField = add(this.sortField, field);
        this.sortMode = add(this.sortMode, isDesc ? "DESC" : "ASC");
    }

    /**
     * 添加分组字段
     *
     * @param fields 分组字段名
     */
    public void addGroupField(String... fields) {
        this.groupField = add(this.groupField, fields);
    }

    /**
     * 添加分组字段
     *
     * @param fields 分组字段名
     */
    @SafeVarargs
    public final <T> void addGroupField(Function<T, ?>... functions) {
        this.groupField = addFunc(this.groupField, functions);
    }

    private static List<String> add(List<String> field, String... functions) {
        if (ArrayUtil.isEmpty(functions)) return field;
        List<String> list = field;
        if (CollUtil.isEmpty(field)) {
            list = new ArrayList<>();
        }
        list.addAll(Arrays.asList(functions));
        return list;
    }

    @SafeVarargs
    private static <T> List<String> addFunc(List<String> field, Function<T, ?>... functions) {
        if (ArrayUtil.isEmpty(functions)) return field;
        List<String> list = field;
        if (CollUtil.isEmpty(field)) {
            list = new ArrayList<>();
        }
        for (Function<T, ?> function : functions) {
            String fieldName = StrUtil.toUnderlineCase(LambdaUtil.getFieldName(function::apply));
            list.add(fieldName);
        }
        return list;
    }

    public void addField(String... fields) {
        this.field = add(this.field, fields);
    }

    public void addField(String field, String key) {
        addField(field + " as " + key);
    }

    @SafeVarargs
    public final <T> void addField(Function<T, ?>... functions) {
        for (Function<T, ?> function : functions) {
            String fieldName = StrUtil.toUnderlineCase(LambdaUtil.getFieldName(function::apply));
            this.field = add(this.field, fieldName);
        }
    }

    /**
     * 添加sum字段
     * @ext 字符串数组
     * @param fields 字段名数组
     */
    public void addSumField(String... fields) {
        for (String field : fields) {
            if (!StrUtil.containsIgnoreCase(field, "SUM")) {
                field = String.format("SUM(%s) AS %s",field, field);
            }
            this.field = add(this.field, field);
        }
    }

    @SafeVarargs
    public final <T> void addSumField(Function<T, ?>... functions) {
        for (Function<T, ?> function : functions) {
            String fieldName = StrUtil.toUnderlineCase(LambdaUtil.getFieldName(function::apply));
            this.field = add(this.field, fieldName);
        }
    }

    public String getSortSQL() {
        StrJoiner sortSQL = new StrJoiner(",", "order by ", "");
        sortSQL.setEmptyResult("");

        doSortHandle((field, mode) -> sortSQL.append(field + " " + mode));
        return sortSQL.toString();
    }

    public void doSortHandle(BiConsumer<String, String> consumer) {
        String sortFiledMode = "ASC";
        if (CollUtil.isNotEmpty(sortField)) {
            for (int i = 0; i < sortField.size(); i++) {
                String field = sortField.get(i);
                String mode = CollUtil.get(sortMode, i);
                if (mode != null) {
                    sortFiledMode = mode;
                }
                consumer.accept(field, sortFiledMode);
            }
        }
    }

    public String getGroupSQL() {
        StrJoiner groupSQL = new StrJoiner(",", "group by ", "");
        groupSQL.setEmptyResult("");

        if (CollUtil.isNotEmpty(groupField)) {
            groupField.forEach((field)-> {
                if (!getCountField().equals(field)) {
                    groupSQL.append(field);
                }
            });
        }
        return groupSQL.toString();
    }

    public String getFieldSQL() {
        StrJoiner fieldSQL = new StrJoiner(", ", "", "");
        fieldSQL.setEmptyResult("");

        int size = 0;
        if (CollUtil.isNotEmpty(groupField)) {
            if (!groupField.contains(getCountField())) {
                addCountField();
            }

            size = groupField.size();
            for (int i = 0; i < size; i++) {
                String field = groupField.get(i);
                fieldSQL.append(String.format("%s AS count%s", field, i));
            }
        }

        if (CollUtil.isNotEmpty(this.field)) {
            for (String field : this.field) {
                if (!StrUtil.containsIgnoreCase(field, "AS")) {
                    fieldSQL.append(String.format("%s AS count%s", field, size++));
                }
            }
        }
        return fieldSQL.toString();
    }

    /**
     * field只允许下划线
     */
    public static String formatField(String source) {
        return StrUtil.toUnderlineCase(source);
    }

    public void buildFieldSQL(Class<?> resClazz) {
        Field[] fields = ReflectUtil.getFieldsDirectly(resClazz, false);
        for (Field field : fields) {
            addField(StrUtil.toUnderlineCase(field.getName()));
        }
    }

    public String getCountField() {
        return "count(*)";
    }

    public void addCountField() {
        addGroupField(getCountField());
    }

    public String buildInSQL(String fieldName) {
        Object fieldValue = ReflectUtil.getFieldValue(this, fieldName);
        if (fieldValue == null) {
            return "in ('空数据填充值')";
        } else {
            if (fieldValue instanceof List) {
                return "in (" + CollUtil.join((List<?>) fieldValue, ",") + ")";
            } else {
                return "= " + fieldValue;
            }
        }
    }

    public String buildInSQL(List<?> list, String prefix) {
        if (ObjectUtil.isEmpty(list)) {
            return "";
        } else {
            try {
                return " AND " + prefix + " in (" + CollUtil.join(list, ",") + ")";
            } catch (Exception e) {
                return "";
            }
        }
    }

    public String buildInSQL(String fieldName, String prefix) {
        Object fieldValue = ReflectUtil.getFieldValue(this, fieldName);
        if (ObjectUtil.isEmpty(fieldValue)) {
            return "";
        } else {
            try {
                prefix = " AND " + prefix;
                if (fieldValue instanceof List) {
                    return prefix + " in (" + CollUtil.join((List<?>) fieldValue, ",") + ")";
                } else {
                    return prefix + " = " + fieldValue;
                }
            } catch (Exception e) {
                return "";
            }
        }
    }

    public String buildBetweenSQL(String sqlField, String lFieldName, String rFieldName) {
        Object lValue = ReflectUtil.getFieldValue(this, lFieldName);
        Object rValue = ReflectUtil.getFieldValue(this, rFieldName);
        boolean lNotEmpty = ObjectUtil.isNotEmpty(lValue);
        boolean rNotEmpty = ObjectUtil.isNotEmpty(rValue);
        String betweenSQL = "";
        if (lNotEmpty || rNotEmpty) {
            if (lNotEmpty && rNotEmpty) {
                betweenSQL = sqlField + " between " + lValue + " and " + rValue;
            } else if (lNotEmpty) {
                betweenSQL = sqlField + " >= " + lValue;
            } else {
                betweenSQL = sqlField + " < " + rValue;
            }
            betweenSQL = " AND " + betweenSQL;
        }
        return betweenSQL;
    }

    public interface SortField {
        String getField(Integer code);
    }

    /* -------------- 以下为工具方法 -------------- */

    public static <T> List<T> doWrapperList(List<T> list, T obj) {
        return CollUtil.setOrAppend(Opt.ofNullable(list).orElse(CollUtil.newArrayList()), 0, obj);
    }

    public <T> List<T> wrapList(List<T> list, T obj) {
        return CollUtil.setOrAppend(Opt.ofNullable(list).orElse(CollUtil.newArrayList()), 0, obj);
    }

    public <T> List<T> add(List<T> list, T obj) {
        List<T> newList = Opt.ofNullable(list).orElse(CollUtil.newArrayList());
        // 添加元素到末尾
        return CollUtil.setOrAppend(newList, newList.size(), obj);
    }
}
