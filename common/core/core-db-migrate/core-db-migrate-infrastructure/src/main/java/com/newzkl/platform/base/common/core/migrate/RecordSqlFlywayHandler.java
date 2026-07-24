package com.newzkl.platform.base.common.core.migrate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;

import com.newzkl.platform.base.common.core.utils.common.IgnoreStrJoiner;
import com.newzkl.platform.base.common.core.utils.properties.SysProperties;
import lombok.extern.slf4j.Slf4j;
import org.dromara.autotable.core.AutoTableGlobalConfig;
import org.dromara.autotable.core.config.PropertyConfig;
import org.dromara.autotable.core.recordsql.AutoTableExecuteSqlLog;
import org.dromara.autotable.core.recordsql.RecordSqlFileHandler;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.CoreMigrationType;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.callback.NoopCallbackExecutor;
import org.flywaydb.core.internal.database.DatabaseType;
import org.flywaydb.core.internal.database.base.Database;
import org.flywaydb.core.internal.database.base.Schema;
import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
import org.flywaydb.core.internal.jdbc.StatementInterceptor;
import org.flywaydb.core.internal.parser.ParsingContext;
import org.flywaydb.core.internal.schemahistory.SchemaHistory;
import org.flywaydb.core.internal.schemahistory.SchemaHistoryFactory;
import org.flywaydb.core.internal.sqlscript.SqlScriptExecutorFactory;
import org.flywaydb.core.internal.sqlscript.SqlScriptFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.zip.CRC32;

/**
 * 自定义 AutoTable SQL 记录处理器，以 Flyway 格式生成 SQL 文件
 * <p>
 * 版本号来自 starter 的 application.yml 中 {@code app.revision}（由 Maven 资源 filtering 把
 * {@code @project.version@} 替换为 ${revision} 实值）。IDE 触发 process-resources 即生效，
 * 无需运行 spring-boot-maven-plugin build-info goal。
 * </p>
 * @ext V{版本}__{描述}.sql
 */
@Component
@Slf4j
public class RecordSqlFlywayHandler extends RecordSqlFileHandler {

    private static Flyway flyway;

    @Autowired(required = false)
    public void setFlyway(Flyway flyway) {
        RecordSqlFlywayHandler.flyway = flyway;
    }

    /**
     * 版本号关键词替换规则
     * @ext SNAPSHOT→0，RELEASE→1
     */
    private static final List<Pair<String, String>> PROTECTED_TERMS = CollUtil.newArrayList(
            new Pair<>("-SNAPSHOT", "_0"),
            new Pair<>("-RELEASE", "")
    );

    /**
     * 根据执行日志生成 Flyway 格式的 SQL 文件路径
     * <p>
     * fat jar 部署时 CodeSource URI 形如 {@code jar:nested:/...!/BOOT-INF/lib/xxx.jar}，
     * 非 hierarchical，无法 {@code new File}；此时回退到 {@code java.io.tmpdir}，
     * 文件落临时盘但不影响 {@link #doSchemaHistory} 写库。
     * </p>
     * @ext URI
     * @ext 关键路径
     * @param autoTableExecuteSqlLog AutoTable SQL 执行日志
     * @return 目标文件路径
     */
    private static Path buildFilePath(AutoTableExecuteSqlLog autoTableExecuteSqlLog) {
        Class<?> entityClass = autoTableExecuteSqlLog.getEntityClass();
        try {
            String version = StrUtil.format("{}_{}",
                    removeIllegalCharacter(removeFixCharacter(resolveRevision(autoTableExecuteSqlLog.getVersion()))),
                    DateUtil.format(LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(autoTableExecuteSqlLog.getExecutionTime()),
                            ZoneId.systemDefault()), "yyMMddHHmmssSSS")
            );

            String sql = autoTableExecuteSqlLog.getSqlStatement();
            String tableName = autoTableExecuteSqlLog.getTableName();
            String description = StrUtil.format("{}_{}",
                    StrUtil.upperFirst(sql).charAt(0),
                    tableName
            );

            Configuration config = Opt.ofNullable(flyway).map(Flyway::getConfiguration).orElse(new ClassicConfiguration());
            String prefix = config != null ? config.getSqlMigrationPrefix() : "V";
            String separator = config != null ? config.getSqlMigrationSeparator() : "__";
            String suffix = config != null
                    ? CollUtil.getFirst(List.of(config.getSqlMigrationSuffixes()))
                    : ".sql";

            String sqlFilename = StrUtil.format("{}{}{}{}{}",
                    prefix, version, separator, description, suffix
            );

            PropertyConfig.RecordSqlProperties recordSql = AutoTableGlobalConfig.instance()
                    .getAutoTableProperties().getRecordSql();
            String folderPath = recordSql.getFolderPath();

            Path path = resolveSqlFilePath(entityClass, folderPath, tableName, sqlFilename);

            if (config != null) {
                doSchemaHistory(config, version, description, sqlFilename, sql);
            }
            return path;
        } catch (Exception e) {
            log.warn("生成 record-sql 文件路径失败, clazz: {}", entityClass, e);
            return null;
        }
    }

    /**
     * 计算 SQL 文件落地路径
     * <p>
     * 优先在源码工程目录下写文件；
     * jar 内运行时退化到 {@code java.io.tmpdir/record-sql/{module}/}。
     * </p>
     * @ext {@code src/main/resources/{folderPath}/{module}/}
     * @ext URI scheme != file
     * @param entityClass 实体类
     * @param folderPath  AutoTable 配置 folder-path
     * @param tableName   表名
     * @param sqlFilename 文件名
     * @return 文件 Path
     */
    private static Path resolveSqlFilePath(Class<?> entityClass, String folderPath, String tableName, String sqlFilename) {
        String moduleDir = StrUtil.split(tableName, "_").getFirst();
        try {
            URI uri = entityClass.getProtectionDomain().getCodeSource().getLocation().toURI();
            if ("file".equals(uri.getScheme())) {
                File classFile = new File(uri);
                if (classFile.isDirectory()) {
                    // IDE/exploded 运行：classFile 指向 target/classes → projectRoot = .../target/../
                    Path projectRoot = classFile.toPath().getParent().getParent();
                    return projectRoot.resolve(IgnoreStrJoiner.of("/")
                            .append("src/main/resources")
                            .append(folderPath)
                            .append(moduleDir)
                            .append(sqlFilename)
                            .toString()).toAbsolutePath().normalize();
                }
            }
        } catch (URISyntaxException ignore) {
            // fall through to tmpdir
        }
        // fat jar / nested jar：URI 非 hierarchical 或 classFile 非目录，落 tmpdir
        return Paths.get(System.getProperty("java.io.tmpdir"))
                .resolve("record-sql")
                .resolve(moduleDir)
                .resolve(sqlFilename)
                .toAbsolutePath()
                .normalize();
    }

    public static void doSchemaHistory(Configuration configuration, String version, String description, String fileName, String sql){
        StatementInterceptor statementInterceptor = configuration.getPluginRegister().getPlugins(StatementInterceptor.class).stream()
                .filter(i -> i.isConfigured(configuration))
                .findFirst()
                .orElse(null);
        if (configuration.getDataSource() == null) return;
        JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactory(configuration.getDataSource(), configuration, statementInterceptor);
        final DatabaseType databaseType = jdbcConnectionFactory.getDatabaseType();
        Database database = databaseType.createDatabase(configuration, jdbcConnectionFactory, statementInterceptor);
        org.flywaydb.core.internal.util.Pair<Schema, List<Schema>> schemas = SchemaHistoryFactory.prepareSchemas(configuration, database);
        Schema defaultSchema = schemas.getLeft();
        Table table = defaultSchema.getTable(configuration.getTable());
        final SqlScriptExecutorFactory sqlScriptExecutorFactory = databaseType.createSqlScriptExecutorFactory(
                jdbcConnectionFactory, NoopCallbackExecutor.INSTANCE, null);
        final ParsingContext parsingContext = new ParsingContext();
        final SqlScriptFactory sqlScriptFactory = databaseType.createSqlScriptFactory(configuration, parsingContext);
        SchemaHistory schemaHistory = new JdbcTableSchemaHistory(sqlScriptExecutorFactory, sqlScriptFactory, database, table, configuration);
        if (statementInterceptor != null) {
            schemaHistory = statementInterceptor.getSchemaHistory(configuration, schemaHistory);
        }

        schemaHistory.addAppliedMigration(MigrationVersion.fromVersion(version), description, CoreMigrationType.SQL, fileName, calculateChecksumForResource(sql), 0, true);
    }

    private static int calculateChecksumForResource(String sql) {
        final CRC32 crc32 = new CRC32();
        crc32.update(sql.replaceAll("\n","").getBytes(StandardCharsets.UTF_8));
        return (int) crc32.getValue();
    }

    /**
     * 解析 Maven 版本号
     * <p>
     * 优先取 {@code @Value} 注入值（starter application.yml 中
     * {@code @project.version@} 经 Maven 资源 filtering 替换为实值，对应 ${revision}）。
     * 注入失败或为空时回退到 AutoTable 配置中的 version。
     * </p>
     * @ext "${app.revision}"
     * @param defaultVersion 兜底版本号
     * @return 版本号字符串
     */
    private static String resolveRevision(String defaultVersion) {
        String version = SysProperties.version;
        if (StrUtil.isNotBlank(version) && !version.startsWith("@")) {
            return version;
        }
        return defaultVersion;
    }

    /**
     * 移除字符串中的非法字符
     * @ext 非字母数字转下划线，中文直接移除
     * @param str 原始字符串
     * @return 清洁后的字符串
     */
    private static String removeIllegalCharacter(String str) {
        return str.replaceAll("[^a-zA-Z0-9]+", "_")
                .replaceAll("[\\u4e00-\\u9fa5]", "");
    }

    /**
     * 替换版本号中的保护关键词
     * @ext 如 SNAPSHOT→0, RELEASE→1
     * @param str 版本号字符串
     * @return 替换后的字符串
     */
    private static String removeFixCharacter(String str) {
        for (Pair<String, String> pair : PROTECTED_TERMS) {
            str = str.replaceAll(pair.getKey(), pair.getValue());
        }
        return str;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Path getFilePath(AutoTableExecuteSqlLog autoTableExecuteSqlLog) {
        return buildFilePath(autoTableExecuteSqlLog);
    }

//    public static void main(String[] args) {
//        Class<?> entityClass = WmsWarehousePO.class;
//        String sql = "ALTER TABLE wms_warehouse add column `zip_code` varchar(255) default '' not null comment '邮编'";
//        buildFlywayFile(entityClass, sql);
//    }
//
//    public static void buildFlywayFile(Class<?> entityClass, String sql) {
//        JavaTypeToDatabaseTypeConverter.addTypeMapping(DatabaseDialect.MySQL, new MysqlStrategy().typeMapping());
//        AutoTableGlobalConfig.setAutoTableOrmFrameAdapter(new AutoTableAdapter(new ArrayList<>(),
//                CollUtil.newArrayList(new BaseEntityFieldTypeHandler())
//        ));
//        MysqlTableMetadata metadata = MysqlTableMetadataBuilder.build(entityClass);
//        SqlFormatter.Formatter sqlFormatter = SqlFormatter.of(Dialect.MySql);
//        String realSql = sqlFormatter.format(sql);
//        FlywayModel flywayModel = FlywayModel.defaults();
//        AutoTableExecuteSqlLog sqlLog = AutoTableExecuteSqlLog.of(
//                entityClass,
//                metadata.getSchema(),
//                metadata.getTableName(),
//                realSql,
//                Instant.now().toEpochMilli(),
//                Instant.now().toEpochMilli()
//        );
//        try (InputStream input = entityClass.getClassLoader().getResourceAsStream("application.yml")) {
//            Properties prop = new Properties();
//            if (input == null) {
//                System.out.println("Sorry, unable to find app.properties");
//                return;
//            }
//            // load a properties file from class path, inside static method
//            prop.load(input);
//
//            // get the property value and print it out
//            flywayModel.setLocations(CollUtil.newArrayList(prop.getProperty("dbSyncLocation")));
//            sqlLog.setVersion(prop.getProperty("projectVersion"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Path path = getFilePath(sqlLog, null, flywayModel);
//        if (!Files.exists(path)) {
//            try {
//                Files.createDirectories(path.getParent());
//                Files.createFile(path);
//            } catch (IOException e) {
//                log.error("创建日志文件{}出错", path, e);
//                path = null;
//            }
//        }
//
//        if (path != null) {
//            try {
//                String sqlStatement = sqlLog.getSqlStatement();
//                // 末尾添加换行符
//                if (!sqlStatement.endsWith(System.lineSeparator())) {
//                    sqlStatement = sqlStatement + System.lineSeparator();
//                }
//                Files.writeString(path, sqlStatement, java.nio.file.StandardOpenOption.APPEND);
//            } catch (IOException e) {
//                log.error("向{}写入SQL日志出错", path, e);
//            }
//        }
//    }
}
