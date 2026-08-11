package com.newzkl.platform.base.common.ddd.infrastructure.mybatis.generator;

import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import com.newzkl.platform.base.common.core.utils.generator.Generator;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.mapper.IdGeneratorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 业务码 DB 发号器启动注册器
 *
 * <p>容器就绪后,为标记 dbSegment 的 BusinessType 构造 SegmentIdGenerator 并覆盖注册进
 * BusinessCodeUtil,替换枚举默认的内存发号器</p>
 *
 * @author KC
 */
@Component
@RequiredArgsConstructor
public class BusinessCodeGeneratorInitializer implements ApplicationRunner {

    private final IdGeneratorMapper idGeneratorMapper;

    @Value("${id.generator.step:1000}")
    private int step;

    /**
     * 启动期覆盖注册 DB 号段发号器
     *
     * @param args 应用参数
     */
    @Override
    public void run(ApplicationArguments args) {
        for (BusinessType type : BusinessType.values()) {
            if (type.isDbSegment()) {
                Generator generator = new SegmentIdGenerator(idGeneratorMapper, type.name(), step);
                BusinessCodeUtil.addSequence(type, generator);
            }
        }
    }
}
