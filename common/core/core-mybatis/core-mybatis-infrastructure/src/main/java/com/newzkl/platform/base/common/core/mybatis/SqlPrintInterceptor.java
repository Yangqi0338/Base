package com.newzkl.platform.base.common.core.mybatis;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.MDC;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

/**
 * MyBatis SQL 打印拦截器，拦截 StatementHandler.prepare 并将完整SQL及耗时打印到日志
 */

@Intercepts({
        @Signature(
                type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}
        )
})
@Slf4j
public class SqlPrintInterceptor implements Interceptor {

    /**
     * 是否打印SQL日志的线程变量，默认为 true
     */
    public static final ThreadLocal<Boolean> needPrint = ThreadLocal.withInitial(() -> true);
    /**
     * 日期格式化器，用于SQL中日期参数的显示
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 拦截 SQL 执行，打印完整SQL及耗时信息
     *
     * @param invocation MyBatis 拦截调用对象
     * @return 拦截链执行结果
     * @throws Throwable 执行异常
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        //先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler，然后就到BaseStatementHandler的成员变量mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        String statementId = mappedStatement.getId();
        BoundSql boundSql = statementHandler.getBoundSql();

        Configuration configuration = mappedStatement.getConfiguration();

        Object parameterObject = boundSql.getParameterObject();
        String sql = getSql(boundSql, parameterObject, configuration);
        Object proceed = invocation.proceed();

        long end = System.currentTimeMillis();
        long timing = end - start;
        Thread thread = Thread.currentThread();
        String traceId = MDC.get("traceId");
        String message = StrUtil.format("\n执行sql耗时:{} ms  方法ID: {} 线程ID: {} \n[tradeId=%s] SQL语句:{}",
                timing, statementId, thread.getId(), sql, traceId);
        if (log.isInfoEnabled() && needPrint.get()) {
            log.info(message);
        }
        return proceed;
    }

    /**
     * 包装目标对象，仅对 StatementHandler 生效
     *
     * @param target 目标对象
     * @return 包装后的代理对象，或原始对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }

    }

    /**
     * 将 BoundSql 中的占位符替换为实际参数值，生成可读SQL
     *
     * @param boundSql        MyBatis BoundSql 对象
     * @param parameterObject 参数对象
     * @param configuration   MyBatis 配置
     * @return 参数填充后的完整 SQL 字符串
     */
    private String getSql(BoundSql boundSql, Object parameterObject, Configuration configuration) {
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (parameterMappings != null) {
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    sql = replacePlaceholder(sql, value);
                }
            }
        }
        return sql;
    }

    /**
     * 将 SQL 中第一个占位符 ? 替换为实际参数值的字符串表示
     *
     * @param sql           含占位符的 SQL
     * @param propertyValue 实际参数值
     * @return 替换后的 SQL
     */
    private String replacePlaceholder(String sql, Object propertyValue) {
        String result;
        if (propertyValue != null) {
            if (propertyValue instanceof String) {
                result = "'" + propertyValue + "'";
            } else if (propertyValue instanceof Date) {
                result = "'" + DATE_FORMAT.format(propertyValue) + "'";
            } else {
                result = propertyValue.toString();
            }
        } else {
            result = "null";
        }
        return sql.replaceFirst("\\?", Matcher.quoteReplacement(result));
    }
}
