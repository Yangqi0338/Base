package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/1316:14
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
