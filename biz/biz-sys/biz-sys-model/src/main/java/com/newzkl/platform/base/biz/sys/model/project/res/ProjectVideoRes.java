package com.newzkl.platform.base.biz.sys.model.project.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目视频视图对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectVideoRes extends BaseRes {

    /**
     * 名称
     */
    private String name;

    /**
     * 视频地址
     */
    private String url;

    /**
     * 顺序 (升序, 从 0 开始)
     */
    private Integer index;

    /**
     * 视频额外信息
     */
    private String extra;
}
