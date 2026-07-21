package com.newzkl.platform.base.common.core.utils.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.newzkl.platform.base.common.core.model.constants.StringPool;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JSON 序列化 / 反序列化工具 (Jackson + Hutool + Fastjson2)。
 *
 * <p>迁移说明: 原静态块显式注册 jackson-datatype-jsr310 的 JavaTimeModule；
 * 通用层未引入该依赖，改为运行期 {@code findAndRegisterModules()} 自动发现
 * (若类路径存在 jsr310 则生效)。</p>
 *
 * @author fang
 */
public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
    /**
     * 定义jackson对象
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.findAndRegisterModules();
    }

    public static cn.hutool.json.JSONObject parseObj(Object value) {
        return parseNestedJson(JSONUtil.parseObj(value));
    }

    public static cn.hutool.json.JSONArray parseArr(Object value) {
        return parseNestedJson(JSONUtil.parseArray(value));
    }

    public static <T> T parseNestedJson(T jsonObjOrArray) {
        if (jsonObjOrArray instanceof cn.hutool.json.JSONObject) {
            processJSONObject((cn.hutool.json.JSONObject) jsonObjOrArray);
        } else if (jsonObjOrArray instanceof cn.hutool.json.JSONArray) {
            processJSONArray((cn.hutool.json.JSONArray) jsonObjOrArray);
        }
        return jsonObjOrArray;
    }

    private static void processJSONObject(cn.hutool.json.JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);

            if (value instanceof String) {
                Object parsedValue = tryParseJsonString((String) value);
                if (parsedValue != null) {
                    jsonObject.set(key, parsedValue);
                    parseNestedJson(parsedValue);
                }
            } else if (value instanceof cn.hutool.json.JSONObject) {
                processJSONObject((cn.hutool.json.JSONObject) value);
            } else if (value instanceof cn.hutool.json.JSONArray) {
                processJSONArray((cn.hutool.json.JSONArray) value);
            }
        }
    }

    private static void processJSONArray(cn.hutool.json.JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.size(); i++) {
            Object element = jsonArray.get(i);

            if (element instanceof String) {
                Object parsedElement = tryParseJsonString((String) element);
                if (parsedElement != null) {
                    jsonArray.set(i, parsedElement);
                    parseNestedJson(parsedElement);
                }
            } else if (element instanceof cn.hutool.json.JSONObject) {
                processJSONObject((cn.hutool.json.JSONObject) element);
            } else if (element instanceof cn.hutool.json.JSONArray) {
                processJSONArray((cn.hutool.json.JSONArray) element);
            }
        }
    }

    private static Object tryParseJsonString(String jsonStr) {
        if (StrUtil.isBlank(jsonStr)) {
            return null;
        }

        try {
            return JSONUtil.parseObj(jsonStr);
        } catch (JSONException e) {
            try {
                return JSONUtil.parseArray(jsonStr);
            } catch (JSONException e2) {
                return null;
            }
        }
    }

    public static <T> String toJson(T value) {
        try {
            return getInstance().writeValueAsString(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static byte[] toJsonAsBytes(Object object) {
        try {
            return getInstance().writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    public static <T> T parse(String content, Class<T> valueType) {
        try {
            return getInstance().readValue(content, valueType);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T parse(String content, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(content, typeReference);
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    public static <T> T parse(byte[] bytes, Class<T> valueType) {
        try {
            return getInstance().readValue(bytes, valueType);
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    public static <T> T parse(byte[] bytes, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(bytes, typeReference);
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    public static <T> T parse(InputStream in, Class<T> valueType) {
        try {
            return getInstance().readValue(in, valueType);
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    public static <T> T parse(InputStream in, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(in, typeReference);
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    public static <T> List<T> parseArray(String content, Class<T> valueTypeRef) {
        try {

            if (!StrUtil.startWithIgnoreCase(content, StringPool.LEFT_SQ_BRACKET)) {
                content = StringPool.LEFT_SQ_BRACKET + content + StringPool.RIGHT_SQ_BRACKET;
            }

            List<Map<String, Object>> list = getInstance().readValue(content, new TypeReference<List<Map<String, Object>>>() {
            });
            List<T> result = new ArrayList<>();
            for (Map<String, Object> map : list) {
                result.add(toPojo(map, valueTypeRef));
            }
            return result;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T mapToPojo(Map map, Class<T> beanType) {
        return MAPPER.convertValue(map, beanType);
    }

    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String objectToJson(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> toMap(String content) {
        try {
            return getInstance().readValue(content, Map.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> Map<String, T> toMap(String content, Class<T> valueTypeRef) {
        try {
            Map<String, Map<String, Object>> map = getInstance().readValue(content, new TypeReference<Map<String, Map<String, Object>>>() {
            });
            Map<String, T> result = new HashMap<>(16);
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), toPojo(entry.getValue(), valueTypeRef));
            }
            return result;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T toPojo(Map fromValue, Class<T> toValueType) {
        return getInstance().convertValue(fromValue, toValueType);
    }

    public static JsonNode readTree(String jsonString) {
        try {
            return getInstance().readTree(jsonString);
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    public static JsonNode readTree(InputStream in) {
        try {
            return getInstance().readTree(in);
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    public static JsonNode readTree(byte[] content) {
        try {
            return getInstance().readTree(content);
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    public static JsonNode readTree(JsonParser jsonParser) {
        try {
            return getInstance().readTree(jsonParser);
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    public static ObjectMapper getInstance() {
        return JacksonHolder.INSTANCE;
    }

    public static String sort4JsonString(String sourceJson, int maxLayer) {
        if (StrUtil.isBlank(sourceJson)) {
            return "";
        } else {
            Map m = JSONObject.parseObject(sourceJson, TreeMap.class);
            if (maxLayer > 0) {
                Iterator it = m.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    int layer = 0;
                    if (entry.getValue() instanceof JSONArray array) {
                        ++layer;
                        sortJsonArray(array, layer, maxLayer);
                    }
                }
            }

            return JSON.toJSONString(m);
        }
    }

    public static String loopSort4JsonString(String sourceJson, int maxLayer, boolean needLoop) {
        if (StrUtil.isBlank(sourceJson)) {
            return "";
        }

        TreeMap<String, Object> m = JSONObject.parseObject(sourceJson, TreeMap.class);
        if (maxLayer > 0) {
            for (Map.Entry<String, Object> entry : m.entrySet()) {
                int layer = 0;
                if (entry.getValue() instanceof JSONArray array) {
                    ++layer;
                    sortJsonArray(array, layer, maxLayer);
                }
            }
        }
        if (needLoop && MapUtil.isNotEmpty(m)) {
            List<Map.Entry<String, Object>> handleMapList = m.entrySet().stream().filter(it -> it.getValue() instanceof JSONObject).collect(Collectors.toList());

            if (CollUtil.isNotEmpty(handleMapList)) {
                for (Map.Entry<String, Object> map : handleMapList) {
                    String sortJson = loopSort4JsonString(JSON.toJSONString(map.getValue()), maxLayer + 1, needLoop);
                    map.setValue(sortJson);
                }
            }
        }

        return JSON.toJSONString(m);
    }

    private static void sortJsonArray(JSONArray array, int layer, int maxLayer) {
        if (layer >= maxLayer) {
            throw new ScmException(BaseErrorCode.PARAM);
        } else {
            for (int i = 0; i < array.size(); ++i) {
                JSONArray var10000;
                if (array.get(i) instanceof JSONArray) {
                    var10000 = (JSONArray) array.get(i);
                    ++layer;
                    sortJsonArray(var10000, layer, maxLayer);
                } else if (!(array.get(i) instanceof Comparable)) {
                    Map map = JSON.parseObject(array.get(i).toString(), TreeMap.class);
                    array.set(i, map);
                    Iterator it = map.entrySet().iterator();

                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        if (entry.getValue() instanceof JSONArray) {
                            var10000 = (JSONArray) entry.getValue();
                            ++layer;
                            sortJsonArray(var10000, layer, maxLayer);
                        }
                    }
                }
            }

        }
    }

    private static class JacksonHolder {
        private static final ObjectMapper INSTANCE = new JacksonObjectMapper();
    }

    public static class JacksonObjectMapper extends ObjectMapper {
        private static final long serialVersionUID = 4288193147502386170L;

        private static final Locale CHINA = Locale.CHINA;

        public JacksonObjectMapper() {
            super();
            super.setLocale(CHINA);
            super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            super.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN, Locale.CHINA));
            super.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
            super.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
            super.findAndRegisterModules();
            super.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            super.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            super.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            super.findAndRegisterModules();
        }

        @Override
        public ObjectMapper copy() {
            return super.copy();
        }
    }
}
