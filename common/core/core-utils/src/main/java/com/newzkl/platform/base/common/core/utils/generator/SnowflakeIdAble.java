package com.newzkl.platform.base.common.core.utils.generator;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 雪花 ID 生成器。
 *
 * @author fang
 */
@Component
public class SnowflakeIdAble implements IdentifierGenerator, Generator {

    static {
        IdGeneratorOptions options = new IdGeneratorOptions((short) getRandom());
        YitIdHelper.setIdGenerator(options);
    }

    /**
     * 生成1-31之间的随机数。
     *
     * @return 随机 workerId
     */
    private static int getRandom() {
        int max = 31;
        int min = 1;
        Random random = new Random();
        int result = random.nextInt(max - min) + min;
        return result;
    }

    public static Long getSnowflakeId() {
        return YitIdHelper.nextId();
    }

    @Override
    public Number nextId(Object entity) {
        return getSnowflakeId();
    }

    @Override
    public String nextUUID(Object entity) {
        String result = String.valueOf(nextId(entity));
        int len = NumberUtil.parseInt(StrUtil.toString(entity));
        if (len > 0) {
            result = result.substring(0, Math.min(len, result.length()));
        }
        return result;
    }
}
