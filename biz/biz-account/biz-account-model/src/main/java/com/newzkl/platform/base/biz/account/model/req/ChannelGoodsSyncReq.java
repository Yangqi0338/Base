package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 渠道商产品类别同步请求
 *
 * @author muc_fang
 * @date 2024/4/13 16:14
 */
@Data
public class ChannelGoodsSyncReq implements Serializable {
    /**
     * SaaS手机账号
     */
    @NotNull
    private String phone;
    /**
     * 产品类别数组
     */
    @NotNull
    private List<String> categoryNameList;
}
