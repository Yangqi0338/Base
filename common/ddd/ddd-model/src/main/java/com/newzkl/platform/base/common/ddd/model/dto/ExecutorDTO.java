package com.newzkl.platform.base.common.ddd.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 操作人信息
 *
 * <p>合并创建人与更新人及其名称, 作为 BaseDO.exector 的 JSON 序列化载体。</p>
 *
 * @author KC
 */
@Data
public class ExecutorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 更新人 ID
     */
    private Long updater;

    /**
     * 更新人名称
     */
    private String updaterName;
}
