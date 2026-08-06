package com.newzkl.platform.base.common.core.migrate;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.math.Money;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import com.newzkl.platform.base.common.core.mybatis.MybatisPlusConfig;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.common.core.utils.common.IgnoreStrJoiner;
import com.newzkl.platform.base.common.core.model.properties.SysProperties;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import org.dromara.autotable.core.AutoTableAnnotationFinder;
import org.dromara.autotable.core.AutoTableMetadataAdapter;
import org.dromara.autotable.strategy.mysql.data.MysqlTypeHelper;
import org.dromara.mpe.autofill.annotation.JsonSerializable;
import org.dromara.mpe.autotable.CustomAutoTableMetadataAdapter;
import org.dromara.mpe.autotable.IgnoreExt;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * AutoTable元数据适配器，从Java源码注释中读取表名和字段注释
 */
@Slf4j
public class AutoTableAdapter extends CustomAutoTableMetadataAdapter implements AutoTableMetadataAdapter, AutoTableAnnotationFinder {

    /**
     * 构造器，注入忽略扩展规则列表
     *
     * @param ignoreExts 需要忽略的字段扩展规则列表
     */
    public AutoTableAdapter(List<IgnoreExt> ignoreExts) {
        super(ignoreExts);
        MysqlTypeHelper.CHAR_STRING_TYPE.add(MysqlTypeConstant.JSON);
    }

    /**
     * 获取类注释
     * @ext 用于表注释
     * @param clazz 实体类
     * @return 类注释字符串，获取失败返回空字符串
     */
    private static String getClassComment(Class<?> clazz) {
        try {
            JavaClass javaClass = CommonUtil.findJavaClass(clazz);
            String comment = javaClass.getComment();
            return comment != null ? comment.trim() : javaClass.getName() + "表";
        } catch (Exception e) {
            log.warn("获取类注释失败, clazz: {}", clazz, e);
            return "";
        }
    }

    /**
     * 获取字段注释
     * @ext 用于列注释
     * @param field 字段反射对象
     * @return 字段注释字符串，获取失败返回空字符串
     */
    private static String getFieldComment(Field field) {
        if (field == null) return "";
        Class<?> clazz = field.getDeclaringClass();
        try {
            JavaClass javaClass = CommonUtil.findJavaClass(clazz);

            String name = field.getName();
            JavaField javaField = javaClass.getFields().stream().filter(it -> it.getName().equals(name))
                    .findFirst().orElse(null);
            return Opt.ofNullable(javaField).map(JavaField::getComment).orElse(name);
        } catch (Exception e) {
            log.warn("获取字段注释失败, clazz: {}", clazz, e);
            return "";
        }
    }

    /**
     * 获取字段长度
     * @ext 用于列注释
     * @param clazz 字段反射对象
     * @return 字段长度，获取失败返回null
     */
    private static String getClassTag(Class<?> clazz, String tagName) {
        if (clazz == null) return "";
        try {
            JavaClass javaClass = CommonUtil.findJavaClass(clazz);
            DocletTag tag = javaClass.getTagByName(tagName);
            if (tag != null) {
                return tag.getValue();
            }
            return "";
        } catch (Exception e) {
            log.warn("获取字段长度失败, clazz: {}", clazz, e);
            return "";
        }
    }

    /**
     * 获取字段标签
     * @ext 用于列注释
     * @param field 字段反射对象
     * @return 字段标签，获取失败返回null
     */
    private static String getFieldTag(Field field, String tagName) {
        if (field == null) return "";
        Class<?> clazz = field.getDeclaringClass();
        try {
            JavaClass javaClass = CommonUtil.findJavaClass(clazz);

            String name = field.getName();
            JavaField javaField = javaClass.getFields().stream().filter(it -> it.getName().equals(name))
                    .findFirst().orElse(null);
            if (javaField != null) {
                DocletTag tag = javaField.getTagByName(tagName);
                if (tag != null) {
                    return tag.getValue();
                }
            }
            return "";
        } catch (Exception e) {
            log.warn("获取字段长度失败, clazz: {}", clazz, e);
            return "";
        }
    }

    /**
     * 获取表名
     * @ext 应用全局表名前缀规则
     * @param clazz 实体类
     * @return 处理后的表名
     */
    @Override
    public String getTableName(Class<?> clazz) {
        return MybatisPlusConfig.buildTableName(super.getTableName(clazz));
    }

    /**
     * 获取表的 schema，优先使用 MyBatis-Plus 配置，其次取全局配置
     * @ext 数据库名
     * @param clazz 实体类
     * @return schema 名称
     */
    @Override
    public String getTableSchema(Class<?> clazz) {
        // 获取mybatisplus的设置
        String tableSchema = super.getTableSchema(clazz);

        // 拿不到获取nacos全局配置
        if (tableSchema == null) {
            tableSchema = SysProperties.db;
        }
        return tableSchema;
    }

    /**
     * 获取表注释，从Java源码类注释中读取
     *
     * @param clazz 实体类
     * @return 表注释字符串
     */
    @Override
    public String getTableComment(Class<?> clazz) {
        // extend判断
        String ext = getClassTag(clazz, "ext");
        if (StrUtil.isNotBlank(ext)) {
            ext = "(" + ext +")";
        }
        return getClassComment(clazz) + ext;
    }

    /**
     * 获取列注释，从Java源码字段注释中读取
     *
     * @param field 字段反射对象
     * @param clazz 所属实体类
     * @return 列注释字符串
     */
    @Override
    public String getColumnComment(Field field, Class<?> clazz) {
        String comment = getFieldComment(field);
        IgnoreStrJoiner strJoiner = IgnoreStrJoiner.of(",","[","]");
        // 枚举值判断
        Class<?> fieldClass = field.getType();
        if (fieldClass.isEnum()) {
            Object[] enums = fieldClass.getEnumConstants();
            for (Object enumConstant : enums) {
                if (enumConstant instanceof IEnum iEnum) {
                    String codeStr = iEnum.getCodeStr();
                    String value = iEnum.getValue();
                    strJoiner.append(codeStr+value);
                }
            }
        }
        // extend判断
        String ext = getFieldTag(field, "ext");
        if (StrUtil.isNotBlank(ext)) {
            ext = "(" + ext +")";
        }
        return comment + ext + strJoiner;
    }

    /**
     * 判断字段是否为非空约束，依据 {@code NotNull} 注解
     *
     * @param field 字段反射对象
     * @param clazz 所属实体类
     * @return 有 {@code NotNull} 注解时返回 true
     */
    @Override
    public Boolean isNotNull(Field field, Class<?> clazz) {
        return AnnotationUtil.hasAnnotation(field, NotNull.class);
    }

    /**
     * 查找注解
     *
     * @param field           查找的目标字段
     * @param annotationClass 查找的注解
     * @param <A>             注解类型
     * @return 注解
     */
    @Override
    public <A extends Annotation> A find(Field field, Class<A> annotationClass) {
        A declaredAnnotation = field.getDeclaredAnnotation(annotationClass);
        // 判断找的注解是ColumnType
        if (annotationClass == ColumnType.class) {

            String value = null;
            Integer length = null;
            Integer decimalLength = null;
            String[] values = null;
            // 若是json字段
            if (field.isAnnotationPresent(JsonSerializable.class)) {
                value = MysqlTypeConstant.JSON;
            }
            // 若是 Money 类型,落库 BIGINT(分)
            if (Money.class.isAssignableFrom(field.getType())) {
                value = MysqlTypeConstant.BIGINT;
            }
            // 若是指定了长度
            Size size = field.getAnnotation(Size.class);
            if (size != null) {
                length = size.max();
            }
            // 若是指定了小数点长度
            Digits digits = field.getAnnotation(Digits.class);
            if (digits != null) {
                decimalLength = digits.fraction();
            }
            // 枚举值判断
            Class<?> fieldClass = field.getType();
            if (fieldClass.isEnum()) {
                Object[] enums = fieldClass.getEnumConstants();
                for (Object enumConstant : enums) {
                    if (enumConstant instanceof IEnum iEnum) {
                        String codeStr = iEnum.getCodeStr();
                        if (!NumberUtil.isNumber(codeStr)) {
                            if (Opt.ofNullable(length).orElse(0) < codeStr.length()) {
                                length = codeStr.length();
                            }
                        }else {
                            if (NumberUtil.isLong(codeStr)) {
                                value = MysqlTypeConstant.BIGINT;
                            }
                            if (NumberUtil.isInteger(codeStr)) {
                                int i = NumberUtil.parseInt(codeStr);
                                short s = (short) i;
                                if (i == s) {
                                    value = MysqlTypeConstant.INT;
                                }else {
                                    value = MysqlTypeConstant.SMALLINT;
                                }
                            }

                        }
                    }
                }
            }

            ColumnType columnType = (ColumnType) declaredAnnotation;
            if (columnType != null) {
                value = columnType.value();
                length = columnType.length();
                decimalLength = columnType.decimalLength();
                values = columnType.values();
            }

            if (value != null) {
                List<String> intTypeList = CollUtil.newArrayList(
                        MysqlTypeConstant.INT,
                        MysqlTypeConstant.TINYINT,
                        MysqlTypeConstant.SMALLINT,
                        MysqlTypeConstant.MEDIUMINT,
                        MysqlTypeConstant.BIGINT
                );
                if (intTypeList.contains(value)) {
                    length = null;
                    decimalLength = null;
                }
            }

            // 动态数值长度类型
            if (value != null || CommonUtil.biggerThanZero(length) || CommonUtil.biggerThanZero(decimalLength) || values != null) {
                String finalValue = value;
                Integer finalLength = length;
                Integer finalDecimalLength = decimalLength;
                String[] finalValues = values;
                return (A) new ColumnType() {
                    @Override
                    public String value() {
                        return Opt.ofNullable(finalValue).orElse("");
                    }
                    @Override
                    public int length() {
                        return Opt.ofNullable(finalLength).orElse(-1);
                    }
                    @Override
                    public int decimalLength() {
                        return Opt.ofNullable(finalDecimalLength).orElse(-1);
                    }
                    @Override
                    public String[] values() {
                        return Opt.ofNullable(finalValues).orElse(new String[]{});
                    }
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return ColumnType.class;
                    }
                };
            }
        }
        // 判断找的注解是索引，这里修正一下索引的comment为字段javadoc
        if (annotationClass == Index.class && declaredAnnotation != null) {
            Index index = (Index) declaredAnnotation;
            return (A) new Index() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return Index.class;
                }
                @Override
                public String name() {
                    return index.name();
                }
                @Override
                public IndexTypeEnum type() {
                    return index.type();
                }
                @Override
                public String method() {
                    return index.method();
                }
                @Override
                public String comment() {
                    return Opt.ofBlankAble(index.comment()).orElse(getFieldComment(field));
                }
            };
        }
        return declaredAnnotation;
    }
}
