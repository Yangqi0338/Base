package com.newzkl.platform.base.common.core.utils.biz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 通用JSON加解密工具。
 * 核心：AES-CBC对称加密 + JSON序列化/反序列化 + timestamp过期验证。
 *
 * @author sijiwang
 */
@Component
public class JsonEncryptDecryptUtils {

    /**
     * AES密钥（16/24/32字节，推荐16字节）。
     * 生产环境：从配置中心读取，禁止硬编码！
     */
    public final static String AES_SECRET_KEY = "b4cdd51b8047726f";
    /**
     * AES初始化向量IV（CBC模式必填，固定16字节）。
     */
    public final static String AES_IV = "8047726fb4cdd51b";
    /**
     * 过期时间（分钟）。
     */
    public final static Integer EXPIRE_MINUTES = 5;
    /**
     * JSON中时间戳字段名（固定，前后端需一致）。
     */
    public final static String TIMESTAMP_FIELD = "timestamp";
    /**
     * Jackson JSON解析器（全局单例）。
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 校验时间戳是否过期。
     *
     * @param timestamp 时间戳
     * @return 是否过期
     */
    public static boolean isExpired(long timestamp) {
        return System.currentTimeMillis() - timestamp > EXPIRE_MINUTES * 60 * 1000L;
    }

    /**
     * 加密：任意Java对象 → JSON字符串 → AES加密 → Base64编码。
     *
     * @param obj 待加密的Java对象（需包含timestamp字段）
     * @return 加密后的Base64字符串
     */
    public static String encrypt(Object obj) {
        try {
            String jsonStr = OBJECT_MAPPER.writeValueAsString(obj);
            SecretKeySpec keySpec = new SecretKeySpec(AES_SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(AES_IV.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encryptBytes = cipher.doFinal(jsonStr.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptBytes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON序列化失败", e);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * 加密：直接加密JSON字符串（适配前端已拼接好的JSON）。
     *
     * @param jsonStr 待加密的JSON字符串（需包含timestamp字段）
     * @return 加密后的Base64字符串
     */
    public static String encrypt(String jsonStr) {
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonStr);
            if (!jsonNode.has(TIMESTAMP_FIELD)) {
                throw new RuntimeException("JSON字符串必须包含" + TIMESTAMP_FIELD + "字段");
            }
            SecretKeySpec keySpec = new SecretKeySpec(AES_SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(AES_IV.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encryptBytes = cipher.doFinal(jsonStr.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptBytes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON格式错误", e);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * 解密：Base64字符串 → AES解密 → JSON字符串 → 验证timestamp → 返回JSON字符串。
     *
     * @param encryptStr 加密后的Base64字符串
     * @return 解密后的JSON字符串
     */
    public static String decryptToJson(String encryptStr) {
        try {
            byte[] decryptBase64 = Base64.getDecoder().decode(encryptStr);
            SecretKeySpec keySpec = new SecretKeySpec(AES_SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(AES_IV.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decryptBytes = cipher.doFinal(decryptBase64);
            String jsonStr = new String(decryptBytes, StandardCharsets.UTF_8);

            JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonStr);
            if (!jsonNode.has(TIMESTAMP_FIELD)) {
                throw new RuntimeException("解密后的JSON缺少" + TIMESTAMP_FIELD + "字段");
            }
            long timestamp = jsonNode.get(TIMESTAMP_FIELD).asLong();
            if (isExpired(timestamp)) {
                throw new RuntimeException("请求已过期（有效期" + EXPIRE_MINUTES + "分钟）");
            }

            return jsonStr;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("AES解密/JSON解析失败", e);
        }
    }

    /**
     * 解密：Base64字符串 → 转为指定Java对象。
     *
     * @param encryptStr 加密后的Base64字符串
     * @param clazz      目标Java类（需包含timestamp字段）
     * @param <T>        目标类型
     * @return 解密后的Java对象
     */
    public static <T> T decryptToObject(String encryptStr, Class<T> clazz) {
        try {
            String jsonStr = decryptToJson(encryptStr);
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }
}
