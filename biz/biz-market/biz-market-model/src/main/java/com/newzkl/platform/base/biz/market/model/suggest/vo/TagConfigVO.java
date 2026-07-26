package com.newzkl.platform.base.biz.market.model.suggest.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.util.List;

/**
 * 标签配置视图。
 *
 * <p>迁移自 {@code com.zkl.scm.market.domain.other.model.vo.TagConfigVO};
 * 原独立 {@code id} 字段改由 {@link BaseRes} 提供。</p>
 *
 * @author KC
 */
@Data
public class TagConfigVO extends BaseRes {

    /**
     * 标签配置。
     */
    private List<String> tagConfig;

    /**
     * 现有资源标签。
     */
    private List<String> nowTag;

    /**
     * 是否开启标签选择。
     */
    private Integer tagConfigSelect;

    /**
     * 是否开启现有资源标签。
     */
    private Integer nowTagSelect;

    /**
     * 最小选择数量。
     */
    private Integer minNum;
}
