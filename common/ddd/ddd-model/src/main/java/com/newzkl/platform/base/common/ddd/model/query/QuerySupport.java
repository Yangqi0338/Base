package com.newzkl.platform.base.common.ddd.model.query;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

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

    public void setSortField(List<String> sortField) {
        if (CollUtil.isEmpty(sortField)) return;
        if (this.sortField == null) {
            this.sortField = CollUtil.newArrayList();
        }
        sortField.forEach(this::doAddSortField);
    }

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

    public void addDescSortField(String field) {
        addSortField(field, true);
    }

    public void addSortField(String field) {
        addSortField(field, false);
    }

    public void addSortField(String field, boolean isDesc) {
        if (StrUtil.isBlank(field)) return;
        if (this.sortField == null) {
            this.sortField = CollUtil.newArrayList();
        }
        doAddSortField(field);
        if (sortMode == null) {
            sortMode = CollUtil.newArrayList();
        }
        sortMode.add(isDesc ? "DESC" : "ASC");
    }

    public void addGroupField(String field) {
        if (StrUtil.isBlank(field)) return;
        if (groupField == null) {
            groupField = CollUtil.newArrayList();
        }
        groupField.add(field);
    }

    public void addField(String... fields) {
        if (ArrayUtil.isEmpty(fields)) return;
        if (this.field == null) {
            this.field = CollUtil.newArrayList();
        }
        CollUtil.addAll(this.field, fields);
    }

    public void addField(String field, String key) {
        addField(field + " as " + key);
    }

    public final <T> void addField(Function<T, ?>... functions) {
        if (ArrayUtil.isEmpty(functions)) return;
        if (this.field == null) {
            this.field = CollUtil.newArrayList();
        }
        for (Function<T, ?> function : functions) {
            String fieldName = StrUtil.toUnderlineCase(LambdaUtil.getFieldName((it) -> function.apply(null)));
            this.field.add(fieldName);
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
            groupField.forEach(groupSQL::append);
        }
        return groupSQL.toString();
    }

    public String getFieldSQL() {
        StrJoiner fieldSQL = new StrJoiner(", ", "", "");
        fieldSQL.setEmptyResult("");

        if (CollUtil.isNotEmpty(field)) {
            field.forEach(fieldSQL::append);
        }

        if (CollUtil.isNotEmpty(groupField)) {
            if (!groupField.contains(getCountField())) {
                addCountField();
            }
            for (int i = 0; i < groupField.size(); i++) {
                String field = groupField.get(i);
                fieldSQL.append(String.format("%s AS count%s", field, i));
            }
        }
        return fieldSQL.toString();
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
