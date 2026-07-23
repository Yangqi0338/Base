package com.newzkl.platform.base.biz.finance.model.pay.res.huifu;

import lombok.Data;

import java.io.Serializable;

/**
 * @author niu
 * @description:
 * @date 2025-08-25 15:22:17
 */
@Data
public class HuiFuBaseRes implements Serializable {

    /**
     * code
     */
    private String resp_code;

    private String resp_desc;

    private String huifu_id;

}
