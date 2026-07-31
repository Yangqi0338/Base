package com.newzkl.platform.base.biz.order.infrastructure.assembler;

import com.alibaba.fastjson.JSONObject;
import com.zkl.scm.sale.domain.refund.model.vo.RefundOperationRecordExt;
import org.springframework.stereotype.Component;

/**
 * 售后操作记录 自定义转换类（处理JSON字段转换）
 *
 * @author 开发者名称
 * @since 2026-01-23
 */
@Component
public class RefundOperationRecordConvert {

    /**
     * JSON字符串转拓展VO
     * @param string ext字段的JSON字符串
     * @return 拓展VO对象
     */
    public RefundOperationRecordExt refundOperationRecordExtConvert(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        return JSONObject.parseObject(string, RefundOperationRecordExt.class);
    }

    /**
     * 拓展VO转JSON字符串
     * @param object 拓展VO对象
     * @return JSON字符串
     */
    public String refundOperationRecordExtConvert(RefundOperationRecordExt object) {
        if (object == null) {
            return null;
        }
        return JSONObject.toJSONString(object);
    }
}