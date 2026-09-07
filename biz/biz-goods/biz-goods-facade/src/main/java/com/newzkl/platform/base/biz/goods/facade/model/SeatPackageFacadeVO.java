package com.newzkl.platform.base.biz.goods.facade.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 席位套餐对外投影 (facade DTO)
 *
 * <p>对等旧 {@code com.zkl.scm.terminal.interfaces.rpc.facede.model.SeatPackageRpcResp}
 * 中被跨域消费的字段。供 biz-finance 渠道商/供应商购买席位时取套餐数量与价格, 避免调用方直连
 * biz-goods 内部 domain / model</p>
 *
 * @author KC
 */
@Data
public class SeatPackageFacadeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 席位套餐 code
     */
    private String seatPackageCode;

    /**
     * 席位套餐名称
     */
    private String seatPackageName;

    /**
     * 商品个数
     */
    private Integer seatNum;

    /**
     * 套餐价格
     */
    private Integer packagePrice;

    /**
     * 描述
     */
    private String packageDescribe;

    /**
     * 状态: 0 禁用, 1 启用
     */
    private Integer state;
}
