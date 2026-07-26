package com.newzkl.platform.base.biz.sys.model.arole.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 后台角色视图对象。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AroleRes extends BaseRes {

    /**
     * 角色名称。
     */
    private String name;

    /**
     * 备注。
     */
    private String comment;

    /**
     * 关联账号数量 (派生统计, 非持久化列, 列表场景填充)。
     */
    private Integer adminCount;
}
