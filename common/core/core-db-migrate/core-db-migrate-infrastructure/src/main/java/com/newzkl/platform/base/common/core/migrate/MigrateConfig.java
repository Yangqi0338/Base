package com.newzkl.platform.base.common.core.migrate;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.newzkl.platform.base.common.core.mybatis.SqlPrintInterceptor;
import com.newzkl.platform.base.common.core.utils.common.FeiShuMessageSendUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.autotable.core.AutoTableMetadataAdapter;
import org.dromara.mpe.autotable.IgnoreExt;
import org.flywaydb.core.api.FlywayException;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * MyBatis-Plus 全局配置，注册分页插件、SQL打印拦截器及动态表名处理器
 */
@Slf4j
@Configuration
public class MigrateConfig {

    /**
     * 注册 AutoTable 元数据适配器
     * @ext 读取Java源码注释作为表/列注释
     * @param ignoreExts 忽略字段扩展规则列表
     * @return AutoTableMetadataAdapter 实例
     */
    @Bean
    @Primary
    public AutoTableMetadataAdapter autoTableOrmFrameAdapter(List<IgnoreExt> ignoreExts) {
        return new AutoTableAdapter(ignoreExts);
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            try {
                // 尝试正常迁移
                flyway.migrate();
            } catch (FlywayException e) {
                log.info("Flyway迁移失败，尝试修复...", e);
                // 2. 迁移失败，执行 repair
                flyway.repair();
                log.info("Flyway修复完成，再次尝试迁移...");
                try {
                    // 3. 修复后再次尝试迁移
                    flyway.migrate();
                } catch (FlywayException ex) {
                    // 4. 如果修复后仍然失败，记录错误并发送通知
                    log.error("Flyway修复后迁移仍然失败", ex);
                    // 在这里调用你的飞书通知方法
                    FeiShuMessageSendUtil.sendTextMessage("https://open.feishu.cn/open-apis/bot/v2/hook/edbf040c-bbc6-4567-9ff3-17f2d4dc4bc5", ex.toString());
                    throw ex;
                }
            }
        };
    }
}