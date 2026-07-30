package com.newzkl.platform.base.biz.account.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 入会礼包商品信息 (跨域出站出参)
 *
 * <p>字段裁剪自旧 {@code com.zkl.scm.admin.rpc.model.vo.packorder.PackGoodsInfo}
 * (旧类 {@code name} 误声明为 Integer, 此处修正为 String)。</p>
 *
 * @author KC
 */
@Data
public class PackGoodsInfo implements Serializable {

    /**
     * 礼包商品 ID
     */
    private Long id;

    /**
     * 礼包金额
     */
    private Integer amount;

    /**
     * 礼包对应等级值
     */
    private Integer level;

    /**
     * 类型 (角色 ID)
     */
    private Integer type;

    /**
     * 礼包名称
     */
    private String name;
}
