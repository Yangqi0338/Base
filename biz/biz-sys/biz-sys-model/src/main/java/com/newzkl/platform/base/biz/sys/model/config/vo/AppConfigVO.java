package com.newzkl.platform.base.biz.sys.model.config.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 应用配置 VO
 *
 * <p>本地化自旧 {@code com.zkl.scm.rpc.order.AppConfigVO}, 以 JSON 数组串存于字典
 * {@code DictEnum.Key.APP_CONFIG}。</p>
 *
 * @author KC
 */
@Data
public class AppConfigVO implements Serializable {

    /**
     * 应用 id
     */
    private Long id;

    /**
     * 图片
     */
    private String img;

    /**
     * 名称
     */
    private String name;

    /**
     * 价格
     */
    private String price;

    /**
     * 是否可售卖 0 否 1 是
     */
    private Integer canSale;

    /**
     * 是否通知第三方
     */
    private Integer isNotify;

    /**
     * 第三方通知地址
     */
    private String notifyUrl;
}
