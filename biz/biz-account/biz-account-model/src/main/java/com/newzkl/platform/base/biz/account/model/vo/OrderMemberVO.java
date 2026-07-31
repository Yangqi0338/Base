package com.newzkl.platform.base.biz.account.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/715:31
 */
@Data
public class OrderMemberVO implements Serializable {
    /** 主键ID */
    private Long id;
    /** 商户ID */
    private Long merchantId;
    /** 渠道商ID */
    private Long channelId;
}
