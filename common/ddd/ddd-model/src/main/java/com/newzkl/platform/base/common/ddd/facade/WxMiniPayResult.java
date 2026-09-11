package com.newzkl.platform.base.common.ddd.facade;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * 支付返回基础契约
 *
 * <p>临时收敛: 原 biz-order / biz-finance 各自持有字节级相同副本(防腐), 现统一提到 ddd-model
 * 供跨模块共享, 便于启动与迁移; 后续视分层需要再分发回各域</p>
 *
 * @author KC
 */
@Data
public class WxMiniPayResult implements PayBaseResult {

    @JsonIgnore
    private String payInfo;

    @JsonAnyGetter
    public Map<String, Object> getMap(){
        return JSONUtil.toBean(this.payInfo, Map.class);
    }

}
