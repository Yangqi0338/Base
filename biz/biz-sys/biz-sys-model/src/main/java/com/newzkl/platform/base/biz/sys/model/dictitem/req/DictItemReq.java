package com.newzkl.platform.base.biz.sys.model.dictitem.req;

import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 字典条目请求对象
 *
 * @author KC
 */
@Data
public class DictItemReq {

    /**
     * 条目 id (更新时必填)
     */
    @NotNull(groups = UpdateCommand.class)
    private Long id;

    /**
     * 父字典 id
     */
    @NotNull
    private Long dictId;

    /**
     * 条目键
     */
    @NotBlank
    private String itemKey;

    /**
     * 条目值
     */
    private String itemValue;

    /**
     * 排序 (升序)
     */
    private Integer sort;
}
