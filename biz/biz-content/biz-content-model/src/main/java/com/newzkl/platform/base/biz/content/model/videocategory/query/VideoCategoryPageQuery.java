package com.newzkl.platform.base.biz.content.model.videocategory.query;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 视频分类分页查询入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.query.VideoCategoryPageQuery},
 * 旧父类 {@code BusinessPageQuery} 换为 Base {@code PageQuery}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VideoCategoryPageQuery extends PageQuery implements Serializable {

    /**
     * 分类名称(模糊匹配)
     */
    private String name;
}
