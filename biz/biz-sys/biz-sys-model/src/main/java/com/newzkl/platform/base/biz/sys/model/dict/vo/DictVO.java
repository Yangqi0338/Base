package com.newzkl.platform.base.biz.sys.model.dict.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典视图对象。
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictVO extends BaseRes {

    /**
     * 字典值
     */
    private String value;

    /**
     * 字典描述
     */
    private String desc;
}
