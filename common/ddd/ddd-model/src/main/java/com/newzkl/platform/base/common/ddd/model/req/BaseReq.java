package com.newzkl.platform.base.common.ddd.model.req;


import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 基础视图实体类
 *
 * @author god
 */
@Setter
@Getter
public class BaseReq implements Serializable {

    /**
     * id
     */
    @NotNull(groups = UpdateCommand.class)
    private Long id;

}
