package com.newzkl.platform.base.common.core.redis;


import com.alibaba.fastjson2.support.spring6.data.redis.GenericFastJsonRedisSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Spring Data Redis 序列化配置类
 *
 * @author niu
 */
@Configuration
public class RedisConfig {

    @Bean
    public static BeanPostProcessor redisTemplateCustomizer() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) {
                if (bean instanceof RedisTemplate<?, ?> template) {

                    GenericFastJsonRedisSerializer fastJsonSerializer = new GenericFastJsonRedisSerializer();
                    StringRedisSerializer stringSerializer = new StringRedisSerializer();

                    template.setKeySerializer(stringSerializer);
                    template.setValueSerializer(fastJsonSerializer);
                    template.setHashKeySerializer(stringSerializer);
                    template.setHashValueSerializer(fastJsonSerializer);

                    template.afterPropertiesSet();
                }
                return bean;
            }
        };
    }

    /**
     * 创建并注册 Redisson 客户端 Bean
     *
     * @return Redisson 自动配置定制器
     */
    @Bean
    public RedissonAutoConfigurationCustomizer redissonCustomizer() {
        return (config) -> {
            config.setCodec(new FastJson2Codec());
        };
    }

    public static class FastJson2Codec extends BaseCodec {
        private final GenericFastJsonRedisSerializer valueSerializer = new GenericFastJsonRedisSerializer();
        private final StringRedisSerializer keySerializer = new StringRedisSerializer();

        @Override
        public Decoder<Object> getValueDecoder() {
            return (buf, state) -> {
                String str = buf.toString(StandardCharsets.UTF_8);
                buf.readerIndex(buf.readableBytes());
                return valueSerializer.deserialize(str.getBytes(StandardCharsets.UTF_8));
            };
        }

        @Override
        public Encoder getValueEncoder() {
            return (in) -> {
                ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
                try (ByteBufOutputStream os = new ByteBufOutputStream(out)) {
                    os.write(valueSerializer.serialize(in));
                    return os.buffer();
                } catch (Exception e) {
                    out.release();
                    throw new IOException(e);
                }
            };
        }

        @Override
        public Decoder<Object> getMapKeyDecoder() {
            return (buf, state) -> {
                String str = buf.toString(StandardCharsets.UTF_8);
                buf.readerIndex(buf.readableBytes());
                return keySerializer.deserialize(str.getBytes(StandardCharsets.UTF_8));
            };
        }

        @Override
        public Encoder getMapKeyEncoder() {
            return (in) -> {
                ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
                out.writeBytes(keySerializer.serialize(in.toString()));
                return out;
            };
        }
    }
}
