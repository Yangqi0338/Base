package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 渠道商基础信息同步请求
 *
 * @author muc_fang
 * @date 2024/4/13 16:14
 */
@Data
public class ChannelBaseSyncReq implements Serializable {

    /**
     * 营业执照
     */
    private String license;
    /**
     * 店铺省编码
     * @ext 省 CODE, 6 位
     */
    private Integer shipProvinceCode;
    /**
     * 店铺市编码
     * @ext 市 CODE, 6 位
     */
    private Integer shipCityCode;
    /**
     * 店铺区编码
     * @ext 区 CODE, 6 位
     */
    private Integer shipAreaCode;
    /**
     * 联系人名称
     */
    private String contactsName;
    /**
     * 店铺名称
     */
    private String storeName;
}
