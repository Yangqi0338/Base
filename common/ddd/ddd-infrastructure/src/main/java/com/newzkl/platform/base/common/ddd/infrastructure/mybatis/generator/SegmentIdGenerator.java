package com.newzkl.platform.base.common.ddd.infrastructure.mybatis.generator;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import com.newzkl.platform.base.common.core.utils.generator.Generator;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.dao.IdGeneratorMapper;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.IdGeneratorDO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 号段模式 DB 发号器
 *
 * <p>每次从 id_generator 表原子取一段(步长 step)缓存内存,段内 AtomicLong 自增,用尽再取下一段。
 * 分布式唯一 + 单调不回退,重启仅浪费当前段剩余不重复</p>
 *
 * @author KC
 */
public class SegmentIdGenerator implements Generator {

    private final IdGeneratorMapper mapper;
    private final String generatorKey;
    private final int step;

    private final AtomicLong cursor = new AtomicLong(0);
    private volatile long max = 0;

    public SegmentIdGenerator(IdGeneratorMapper mapper, String generatorKey, int step) {
        this.mapper = mapper;
        this.generatorKey = generatorKey;
        this.step = step;
    }

    /**
     * 生成下一个数值 ID
     *
     * @param entity 上下文参数(号段模式忽略)
     * @return 生成的数值
     */
    @Override
    public synchronized Number nextId(Object entity) {
        long next = cursor.incrementAndGet();
        if (next > max) {
            loadNextSegment();
            next = cursor.incrementAndGet();
        }
        return next;
    }

    /**
     * 生成下一个字符串编码
     *
     * @param entity 截断长度(大于 0 时截断,与 SnowflakeGenerator 语义一致)
     * @return 生成的编码
     */
    @Override
    public String nextUUID(Object entity) {
        String result = String.valueOf(nextId(entity).longValue());
        int len = NumberUtil.parseInt(StrUtil.toString(entity));
        if (len > 0) {
            result = result.substring(0, Math.min(len, result.length()));
        }
        return result;
    }

    /**
     * 原子取下一段并重置游标
     */
    private void loadNextSegment() {
        long newMax = allocateSegment();
        cursor.set(newMax - step);
        max = newMax;
    }

    /**
     * 原子分配一个号段,返回该段最大值
     *
     * @return 分配后 current_value 的新值
     */
    private long allocateSegment() {
        ensureRowExists();
        mapper.update(null, new LambdaUpdateWrapper<IdGeneratorDO>()
                .setSql("current_value = current_value + " + step)
                .set(IdGeneratorDO::getUpdateTime, LocalDateTime.now())
                .eq(IdGeneratorDO::getGeneratorKey, generatorKey));
        IdGeneratorDO row = selectByKey();
        return row.getCurrentValue();
    }

    /**
     * 保证号段行存在,不存在则插入初始行(current_value = 0)
     */
    private void ensureRowExists() {
        if (selectByKey() != null) {
            return;
        }
        try {
            IdGeneratorDO init = new IdGeneratorDO();
            init.setGeneratorKey(generatorKey);
            init.setCurrentValue(0L);
            init.setUpdateTime(LocalDateTime.now());
            mapper.insert(init);
        } catch (DuplicateKeyException e) {
            // 并发下他线程已插入,忽略
        }
    }

    /**
     * 按 key 查号段行
     *
     * @return 号段行,不存在返 null
     */
    private IdGeneratorDO selectByKey() {
        return mapper.selectOne(new LambdaQueryWrapper<IdGeneratorDO>()
                .eq(IdGeneratorDO::getGeneratorKey, generatorKey));
    }

    /**
     * 号段发号器启动注册器
     *
     * <p>与发号器同文件便于管理。容器就绪后为标记 dbSegment 的 BusinessType 构造 SegmentIdGenerator
     * 覆盖注册进 BusinessCodeUtil,替换枚举默认内存发号器。注册不触库,取段在首次发号时懒加载,
     * 故用 @PostConstruct 安全</p>
     *
     * @author KC
     */
    @Component
    @RequiredArgsConstructor
    public static class Registrar {

        private final IdGeneratorMapper idGeneratorMapper;

        @Value("${id.generator.step:1000}")
        private int step;

        /**
         * 启动期覆盖注册 DB 号段发号器
         */
        @PostConstruct
        public void register() {
            for (BusinessType type : BusinessType.values()) {
                if (type.isDbSegment()) {
                    Generator generator = new SegmentIdGenerator(idGeneratorMapper, type.name(), step);
                    BusinessCodeUtil.addSequence(type, generator);
                }
            }
        }
    }
}
