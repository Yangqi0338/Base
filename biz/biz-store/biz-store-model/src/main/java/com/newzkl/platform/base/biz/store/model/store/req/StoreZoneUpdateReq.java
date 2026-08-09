package com.newzkl.platform.base.biz.store.model.store.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 门店专区领域对象
 */
@Data
public class StoreZoneUpdateReq implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 专区名称
     */
    private String zoneName;

    /**
     * 专区副标题
     */
    private String zoneSubtitle;

    /**
     * 描述
     */
    private String zoneDescribe;

    /**
     * 状态
     * @ext 取值范围: 0=禁用, 1=启用
     */
    private Integer state;

}