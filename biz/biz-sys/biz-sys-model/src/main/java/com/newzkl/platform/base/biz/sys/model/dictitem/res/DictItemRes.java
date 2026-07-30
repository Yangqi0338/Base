package com.newzkl.platform.base.biz.sys.model.dictitem.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典条目视图对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictItemRes extends BaseRes {

    /**
     * 父字典 id
     */
    private Long dictId;

    /**
     * 条目键
     */
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
