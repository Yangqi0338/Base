package com.newzkl.platform.base.common.core.utils.generator;

import cn.hutool.core.map.MapUtil;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务Code生成工具类
 * 线程安全，支持日期切换自动重置。
 *
 * @author fang
 */
public class BusinessCodeUtil {

    // 序列号存储器
    private static final Map<BusinessType, Generator> SEQUENCE_MAP = new ConcurrentHashMap<>();

    static {
        for (BusinessType type : BusinessType.values()) {
            addSequence(type, type.getGenerator());
        }
    }

    /**
     * 注册或覆盖业务类型的发号器
     *
     * <p>后注册覆盖先注册,供运行期(如 DB 号段发号器)替换枚举默认的内存发号器</p>
     *
     * @param businessType 业务类型
     * @param generator    生成器
     */
    public static void addSequence(BusinessType businessType, Generator generator) {
        if (businessType == null || generator == null) {
            return;
        }

        SEQUENCE_MAP.put(businessType, generator);
    }

    private BusinessCodeUtil() {
        throw new IllegalStateException("工具类不允许实例化");
    }

    /**
     * 生成带时间戳的业务Code（格式：前缀 + 日期 + 时间 + 4位序列号）
     *
     * @param businessType 业务类型
     * @return 业务Code
     */
    public static String generate(BusinessType businessType) {
        return generate(businessType, 0);
    }

    /**
     * 生成自定义长度的业务Code
     *
     * @param type           业务类型
     * @param sequenceLength 序列号长度
     * @return 业务Code
     */
    public static String generate(BusinessType type, int sequenceLength) {
        Generator sequence = getSequence(type);
        return type.getPrefix() + sequence.nextUUID(sequenceLength);
    }

    /**
     * 批量生成业务Code
     *
     * @param type  业务类型
     * @param count 生成数量
     * @return 业务Code数组
     */
    public static String[] generateBatch(BusinessType type, int count) {
        return generateBatch(type, count, 0);
    }

    public static String[] generateBatch(BusinessType type, int count, int sequenceLength) {
        if (count <= 0) {
            throw new IllegalArgumentException("生成数量必须大于0");
        }
        if (count > 9999) {
            throw new IllegalArgumentException("单次生成数量不能超过9999");
        }

        String[] codes = new String[count];
        Generator sequence = getSequence(type);

        for (int i = 0; i < count; i++) {
            String sequenceStr = sequence.nextUUID(sequenceLength);
            codes[i] = type.getPrefix() + sequenceStr;
        }

        return codes;
    }

    public static Generator getSequence(BusinessType type) {
        Generator generator = MapUtil.get(SEQUENCE_MAP, type, Generator.class);
        if (generator == null) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "!!未设置序列化器!!");
        }
        return generator;
    }
}
