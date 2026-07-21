package com.newzkl.platform.base.common.ddd.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/9/219:35
 */
@Data
public class IdQuery implements Serializable {
    /**
     * ID
     */
    @NotNull
    private Long id;
}
