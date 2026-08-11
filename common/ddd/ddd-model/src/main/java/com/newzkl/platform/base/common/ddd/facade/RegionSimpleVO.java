package com.newzkl.platform.base.common.ddd.facade;

import lombok.Data;

import java.io.Serializable;

/**
 * 行政区域轻量视图
 *
 * <p>跨域 RPC 契约: 消费方(如 biz-content 项目案例)按 code + pid 回填省/市/区名称。
 * 仅承载回填所需的 code/pid/name 三字段, 不含子树/层级等 biz-sys 域内部结构</p>
 *
 * @author KC
 */
@Data
public class RegionSimpleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 区域编码
     */
    private Integer code;

    /**
     * 父编码
     */
    private Integer pid;

    /**
     * 名称
     */
    private String name;
}
