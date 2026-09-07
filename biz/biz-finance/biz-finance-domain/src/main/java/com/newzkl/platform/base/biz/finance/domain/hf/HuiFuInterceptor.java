package com.newzkl.platform.base.biz.finance.domain.hf;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.util.ParameterizedTypeImpl;
import com.dtflys.forest.converter.ForestEncoder;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.http.body.ObjectRequestBody;
import com.dtflys.forest.interceptor.ResponseResult;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.properties.FinanceProperties.HuiFuProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import static com.newzkl.platform.base.common.ddd.model.properties.FinanceProperties.HuiFuProperties.sysId;

/**
 * 汇付拦截器
 */
@Slf4j
@Component
public class HuiFuInterceptor extends ValidateForestInterceptor {

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
        // 返回的字节数组将替换原有的序列化结果
        Base.Req req = (Base.Req) request.getArgument(0);
        req.build(HuiFuProperties.getHuiFuId());

        // 参数校验: 解包出真实业务 DTO, 直接传 ObjectRequestBody 壳会导致校验注解空转
        for (ObjectRequestBody item : request.getBody().getObjectItems()) {
            validate(request, item.getObject());
        }

        String json = JSONUtil.toJsonStr(req);
        String params = HuiFuMethod.loopSort4JsonString(json, 0, true);
        log.info("排序后参数：" + params);
        String requestSign = HuiFuMethod.sign(params);
        OAuth oAuth = new OAuth();
        oAuth.setSys_id(HuiFuProperties.sysId);
        oAuth.setData(JSONObject.parseObject(params));
        oAuth.setSign(requestSign);
        oAuth.setProduct_id(HuiFuProperties.productId);
        log.info("请求前参数：" + JSON.toJSONString(oAuth));
        return JSON.toJSONString(oAuth).getBytes(StandardCharsets.UTF_8);
    }


    /**
     * 该方法在请求发送之前被调用, 若返回false则不会继续发送请求
     *
     * @Param request Forest请求对象
     */
    @Override
    public boolean beforeExecute(ForestRequest request) {
        // 执行在发送请求之前处理的代码
        return true;
    }

    /**
     * 默认回调函数: 接受到请求响应时调用该方法
     * 默认返回未知状态，继续执行后续逻辑
     *
     * @param request  Forest请求对象
     * @param response Forest响应对象
     * @return 请求响应结果: proceed(), success(), 或 error()
     */
    @Override
    public ResponseResult onResponse(ForestRequest request, ForestResponse response) {
        if (response.isError() && !response.statusOk()) {
            log.error("请求失败，response：" + response);
            return error(response.getException());
        }

        String content = response.readAsString();
        log.info("汇付返回：" + content);

        Type bizType = request.getMethod().getReturnType();
        Type syncType = new ParameterizedTypeImpl(new Type[]{bizType}, null, Base.SyncRes.class);
        Base.SyncRes<Base.Res> res = JSON.parseObject(content, syncType);
        Base.Res data = res.getData();
        if (data == null) {

            log.error("汇付返回缺少 data 节点，response：" + response);
            throw new PlatformException(BaseErrorCode.EXECUTE, "汇付返回报文异常");
        }

        // 剥掉外层通用结构, 只把 data 业务对象作为 Api 返回值, Api 层无需感知 SyncRes
        return success(data);
    }

    /**
     * 该方法在请求发送失败时被调用
     */
    @Override
    public void onError(ForestRuntimeException ex, ForestRequest req, ForestResponse res) {
        // 执行发送请求失败后处理的代码
    }

    /**
     * 在请重试前调用 onRetry 回调函数
     *
     * @param req Forest请求对象
     * @param res Forest响应对象
     */
    @Override
    public void onRetry(ForestRequest req, ForestResponse res) {
        // req.getCurrentRetryCount() 获取请求当前重试次数
    }

    @Data
    static class OAuth {
        private String sys_id;
        private JSONObject data;
        private String product_id;
        private String sign;
    }
}

