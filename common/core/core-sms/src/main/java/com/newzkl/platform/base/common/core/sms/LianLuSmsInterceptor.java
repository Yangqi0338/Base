package com.newzkl.platform.base.common.core.sms;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dtflys.forest.converter.ForestEncoder;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.interceptor.Interceptor;

import com.newzkl.platform.base.common.core.model.check.CheckCommand;
import com.newzkl.platform.base.common.core.model.check.UpdateCommand;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import jakarta.validation.groups.Default;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import static com.newzkl.platform.base.common.core.sms.SmsConfig.LianLuSmsConfig.*;


/**
 * 联麓拦截器
 */
@Slf4j
@Component
public class LianLuSmsInterceptor implements Interceptor<Void> {

    /**
     * 在请求体数据序列化后，发送请求数据前调用该方法
     * 默认为 什么都不做
     * 注: multlipart/data类型的文件上传格式的 Body 数据不会调用该回调函数
     *
     * @param request     Forest请求对象
     * @param encoder     Forest转换器
     * @param encodedData 序列化后的请求体数据
     */
    @Override
    public byte[] onBodyEncode(ForestRequest request, ForestEncoder encoder, byte[] encodedData) {
        // 获取原始数据
        JSONObject obj = JSONUtil.parseObj(new String(encodedData));
        log.info("业务参数：" + obj);

        // 检查
        request.getBody().getObjectItems().forEach(item -> validate(request, item));

        OAuth oAuth = new OAuth();
        oAuth.setMchId(MchId);
        oAuth.setAppId(AppId);
        oAuth.setVersion(Version);
        oAuth.setSignType(SignType);
        oAuth.setTimeStamp(System.currentTimeMillis());

        obj.putAll(JSONUtil.parseObj(oAuth));

        // 签名
        obj.set("Signature", sign(obj));

        // 将数据合并
        log.info("入参参数：" + obj);
        String str = obj.toString();
        return str.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 对请求对象进行参数校验
     *
     * @param request Forest请求对象
     * @param obj     待校验的请求体对象
     */
    public <T> void validate(ForestRequest request, T obj) {
        String isModify = request.getHeaderValue("isModify");

        Class<?>[] checkGroupClazz = new Class[0];
        if (Boolean.TRUE.toString().equalsIgnoreCase(isModify)) {
            checkGroupClazz = ArrayUtil.append(checkGroupClazz, UpdateCommand.class);
        } else {
            checkGroupClazz = ArrayUtil.append(checkGroupClazz, Default.class, CheckCommand.class);
        }

        CommonUtil.validate(obj, checkGroupClazz);
    }


    /**
     * 该方法在请求发送之前被调用，若返回false则不会继续发送请求
     *
     * @param request Forest请求对象
     * @return 是否继续发送请求
     */
    @Override
    public boolean beforeExecute(ForestRequest request) {
        // 执行在发送请求之前处理的代码
        return true;
    }

    /**
     * 对请求参数进行MD5签名
     *
     * @param params 请求参数
     * @return 大写的MD5签名字符串
     */
    public String sign(Map<String, Object> params) {
        String jsonStr = JSONUtil.toJsonStr(params);

        TreeMap<String, Object> sortParams = MapUtil.sort(
                MapUtil.filter(params, e ->
                        !signExcludeFields.contains(e.getKey()) && ObjectUtil.isNotEmpty(e.getValue())
                ), String::compareTo);

        sortParams.put("key", AppKey);
        log.info("签名参数：" + sortParams);
        // 请求参数过滤后按参数名排序，最后加上appKey
        String str = URLUtil.buildQuery(sortParams, StandardCharsets.UTF_8);

        try {
            // 拼接后字符串MD5加密
            MessageDigest md = MessageDigest.getInstance(SignType);
            md.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            // 加密后大写
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            log.error("短信签名失败: {}", e.getMessage());
            throw new PlatformException(BaseErrorCode.CUSTOM, e.getMessage());
        }
    }

    /**
     * 联麓短信签名认证参数
     */
    @Data
    static class OAuth {
        /**
         * 商户ID
         */
        private String MchId;
        /**
         * 应用ID
         */
        private String AppId;
        /**
         * 版本号
         */
        private String Version;
        /**
         * 签名类型
         */
        private String SignType;
        /**
         * 时间戳
         */
        private Long TimeStamp;
    }
}

