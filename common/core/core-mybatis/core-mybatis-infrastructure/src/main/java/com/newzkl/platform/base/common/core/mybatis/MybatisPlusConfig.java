package com.newzkl.platform.base.common.core.mybatis;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.dromara.mpe.autotable.IgnoreExt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * MyBatis-Plus 全局配置，注册分页插件、SQL打印拦截器及动态表名处理器
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 根据 SQL 语句和原始表名构建最终表名
     * @ext 内部调用，供动态表名拦截器使用
     * @param sql             当前执行的SQL
     * @param sourceTableName 原始表名
     * @return 处理后的表名
     */
    private static String buildTableName(String sql, String sourceTableName) {
        return buildTableName(sourceTableName);
    }

    /**
     * 去除表名中的DO后缀，生成真正的数据库表名
     *
     * @param sourceTableName 原始表名（可能含 DO 或 _d_o 后缀）
     * @return 处理后的表名
     */
    public static String buildTableName(String sourceTableName) {
        // 存在DO后缀进行去除
        String tableName = StrUtil.replaceIgnoreCase(sourceTableName, "_d_o", "");
        tableName = StrUtil.replaceIgnoreCase(tableName, "DO", "");
        return tableName;
    }

    /**
     * 注册 MyBatis-Plus 拦截器
     * @ext 分页、乐观锁、动态表名
     * @return MybatisPlusInterceptor 实例
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 动态表名
        DynamicTableNameInnerInterceptor tableNameInterceptor = new DynamicTableNameInnerInterceptor(MybatisPlusConfig::buildTableName);
        interceptor.addInnerInterceptor(tableNameInterceptor);

        // 添加分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setDbType(DbType.MYSQL); // 根据数据库类型设置
        paginationInterceptor.setOverflow(false); // 超过总页数返回空列表，不回到第一页
        paginationInterceptor.setMaxLimit(1000L); // 单页分页条数限制

        interceptor.addInnerInterceptor(paginationInterceptor);

        return interceptor;
    }

    /**
     * 注册 SQL 打印拦截器
     *
     * @return SqlPrintInterceptor 实例
     */
    @Bean
    public SqlPrintInterceptor sqlPrintInterceptor() {
        return new SqlPrintInterceptor();
    }
}