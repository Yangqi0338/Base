package com.newzkl.platform.base.biz.account.facade.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 供应商
 * @author fang
 */
@Data
public class SupplierRefundVO implements Serializable {
     /**
     * ID
     */
     private Long id;
     /**
      * 收货地址
      */
     private ReceiveAddressOutVO receiveAddressVO;
}