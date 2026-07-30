package com.newzkl.platform.base.biz.account.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 入会礼包商品写入入参 (跨域出站)
 *
 * <p>字段裁剪自旧 {@code com.zkl.scm.admin.rpc.model.req.PackGoodsReq}。</p>
 *
 * @author KC
 */
@Data
public class PackGoodsSaveReq implements Serializable {

    /**
     * 礼包商品 ID (新建时为空)
     */
    private Long id;

    /**
     * 类型 (角色 ID)
     */
    private Integer type;

    /**
     * 礼包对应等级值
     */
    private Integer level;

    /**
     * 礼包图片
     */
    private String img;

    /**
     * 礼包金额
     */
    private Integer amount;

    /**
     * 礼包名称
     */
    private String name;
}
