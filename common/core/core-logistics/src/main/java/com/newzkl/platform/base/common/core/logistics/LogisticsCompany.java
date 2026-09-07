package com.newzkl.platform.base.common.core.logistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 快递公司选项
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsCompany implements Serializable {

    /**
     * 快递公司名称
     *
     * <p>与发货单 {@code expressCompanyName} 同名同值域: 前端下拉框选中本字段后原样回传发货接口即可</p>
     */
    private String expressCompanyName;

    /**
     * 快递公司编码
     */
    private String expressCompanyCode;
}
