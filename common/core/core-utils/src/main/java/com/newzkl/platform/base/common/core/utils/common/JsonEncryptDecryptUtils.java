package com.newzkl.platform.base.common.core.utils.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 通用 JSON 加解密工具
 *
 * <p>AES-CBC 对称加密 + JSON 序列化 / 反序列化 + {@code timestamp} 过期校验。
 * 迁移自旧 {@code com.zkl.scm.module.util.JsonEncryptDecryptUtils}, 密钥 / IV / 有效期 /
 * 时间戳字段名逐字沿用, 否则前端已下发的 {@code sign} 全部解不开</p>
 *
 * <p>安全提示: 密钥与 IV 为源码硬编码 (旧代码原样, 注释已自认「生产环境应从配置中心读取」),
 * 且 IV 固定不随消息变化, 迁移未改动以保契约, 收口方案见迁移报告「顺带发现的缺陷」</p>
 *
 * <p>迁移删减: 旧类挂了无意义的 {@code @Component} (全静态方法, 不需要 bean),
 * 引入了未使用的 fastjson {@code JSONObject}, 并带一个调试用 {@code main},
 * 三者均未迁入</p>
 *
 * @author sijiwang
 */
public class JsonEncryptDecryptUtils {

    /**
     * AES 密钥 (16 / 24 / 32 字节)
     */
    public static final String AES_SECRET_KEY = "b4cdd51b8047726f";

    /**
     * AES 初始化向量 IV (CBC 模式必填, 固定 16 字节)
     */
    public static final String AES_IV = "8047726fb4cdd51b";

    /**
     * 过期时间 (分钟)
     */
    public static final Integer EXPIRE_MINUTES = 5;

    /**
     * JSON 中时间戳字段名 (固定, 前后端需一致)
     */
    public static final String TIMESTAMP_FIELD = "timestamp";

    /**
     * AES 变换名
     */
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * Jackson 解析器 (全局单例)
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonEncryptDecryptUtils() {
    }

    /**
     * 校验时间戳是否过期
     *
     * @param timestamp 毫秒时间戳
     * @return true 已过期
     */
    public static boolean isExpired(long timestamp) {
        return System.currentTimeMillis() - timestamp > EXPIRE_MINUTES * 60 * 1000L;
    }

    /**
     * 加密: 任意 Java 对象 → JSON 串 → AES 加密 → Base64
     *
     * @param obj 待加密对象 (需含 timestamp 字段)
     * @return 加密后的 Base64 串
     */
    public static String encrypt(Object obj) {
        try {
            return doEncrypt(OBJECT_MAPPER.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON序列化失败", e);
        }
    }

    /**
     * 加密: 直接加密 JSON 串 (适配前端已拼好的 JSON)
     *
     * @param jsonStr 待加密 JSON 串 (需含 timestamp 字段)
     * @return 加密后的 Base64 串
     */
    public static String encrypt(String jsonStr) {
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonStr);
            if (!jsonNode.has(TIMESTAMP_FIELD)) {
                throw new IllegalArgumentException("JSON字符串必须包含" + TIMESTAMP_FIELD + "字段");
            }
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON格式错误", e);
        }
        return doEncrypt(jsonStr);
    }

    /**
     * 解密: Base64 → AES 解密 → JSON 串, 并校验 timestamp
     *
     * @param encryptStr 加密后的 Base64 串
     * @return 解密后的 JSON 串
     */
    public static String decryptToJson(String encryptStr) {
        String jsonStr;
        try {
            byte[] decryptBase64 = Base64.getDecoder().decode(encryptStr);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec(), ivSpec());
            jsonStr = new String(cipher.doFinal(decryptBase64), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException("AES解密失败", e);
        }
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonStr);
            if (!jsonNode.has(TIMESTAMP_FIELD)) {
                throw new IllegalArgumentException("解密后的JSON缺少" + TIMESTAMP_FIELD + "字段");
            }
            if (isExpired(jsonNode.get(TIMESTAMP_FIELD).asLong())) {
                throw new IllegalArgumentException("请求已过期（有效期" + EXPIRE_MINUTES + "分钟）");
            }
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON解析失败", e);
        }
        return jsonStr;
    }

    /**
     * 解密并反序列化为指定类型
     *
     * @param encryptStr 加密后的 Base64 串
     * @param clazz      目标类型 (需含 timestamp 字段)
     * @param <T>        目标类型
     * @return 解密后的对象
     */
    public static <T> T decryptToObject(String encryptStr, Class<T> clazz) {
        String jsonStr = decryptToJson(encryptStr);
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON反序列化失败", e);
        }
    }

    /**
     * AES-CBC 加密并 Base64 编码
     *
     * @param jsonStr 明文 JSON 串
     * @return 加密后的 Base64 串
     */
    private static String doEncrypt(String jsonStr) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec(), ivSpec());
            return Base64.getEncoder().encodeToString(cipher.doFinal(jsonStr.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalArgumentException("AES加密失败", e);
        }
    }

    /**
     * 构建密钥
     *
     * @return AES 密钥
     */
    private static SecretKeySpec keySpec() {
        return new SecretKeySpec(AES_SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
    }

    /**
     * 构建初始化向量
     *
     * @return IV 参数
     */
    private static IvParameterSpec ivSpec() {
        return new IvParameterSpec(AES_IV.getBytes(StandardCharsets.UTF_8));
    }
}
