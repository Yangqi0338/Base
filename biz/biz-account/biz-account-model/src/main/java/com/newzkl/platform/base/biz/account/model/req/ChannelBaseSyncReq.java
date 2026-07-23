package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/1316:14
 */
@Data
public class ChannelBaseSyncReq implements Serializable {

    /**
     * 营业执照
     */
    private String license;
    /**
     * 店铺地址，省CODE, 6位
     */
    private Integer shipProvinceCode;
    /**
     * 店铺地址，市CODE, 6位
     */
    private Integer shipCityCode;
    /**
     * 店铺地址，区CODE, 6位
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
