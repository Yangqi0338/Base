package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;


import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 获取运费模板详细信息请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetExpressFeeTemplatesReq extends HuiDingHuoBaseReq {

    /**
     * 运费模板编号列表（必填，例：["415H032R"]）
     */
    @NotEmpty(message = "运费模板编号列表不能为空")
    private List<String> ids;
}