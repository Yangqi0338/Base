package com.newzkl.platform.base.common.core.redis.fastjson;

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.newzkl.platform.base.common.core.model.money.Money;

import java.lang.reflect.Type;

/**
 * Fastjson2 中 {@link Money} 的注册器，使用分作为底层 JSON 形态，避免 Redis 缓存还原失败
 * @ext long
 */
public final class MoneyFastjson2 {

    private MoneyFastjson2() {
    }

    /**
     * 注册 Money 在 Fastjson2 中的读写器
     * @ext 按分进行序列化与反序列化
     */
    public static void register() {
        JSONFactory.getDefaultObjectWriterProvider().register(Money.class, new MoneyWriter());
        JSONFactory.getDefaultObjectReaderProvider().register(Money.class, new MoneyReader());
    }

    /**
     * 将 Money 写为分的长整型，便于 Redis 跨进程恢复
     */
    private static final class MoneyWriter implements ObjectWriter<Money> {
        /**
         * {@inheritDoc}
         */
        @Override
        public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
            if (object == null || ((Money) object).isNull()) {
                jsonWriter.writeNull();
                return;
            }
            jsonWriter.writeInt64(((Money) object).getCent());
        }
    }

    /**
     * 从分的长整型还原 Money
     */
    private static final class MoneyReader implements ObjectReader<Money> {
        /**
         * {@inheritDoc}
         */
        @Override
        public Money readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
            if (jsonReader.isNull()) {
                jsonReader.readNull();
                return Money.nullVal();
            }
            Long cent = jsonReader.readInt64();
            return Money.of(cent);
        }
    }
}
