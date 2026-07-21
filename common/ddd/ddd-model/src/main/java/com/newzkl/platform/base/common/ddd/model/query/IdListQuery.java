package com.newzkl.platform.base.common.ddd.model.query;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/9/219:35
 */
@Data
public class IdListQuery implements Serializable {
    /**
     * ID集合
     */
    @NotEmpty
    private List<Long> idList;
}
