package com.newzkl.platform.base.biz.store.model.resource.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询图片资源
 *
 * @author niu
 * @date 2024/4/12 10:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryPictureResourceReq extends PageQuery {

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 图片名称
     */
    private String pictureName;

    /** 渠道商ID */
    private Long channelId;

}
