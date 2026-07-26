package com.newzkl.platform.base.biz.market.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 平台分类信息 (跨域出站端口返回体)。
 *
 * <p>迁移: 对应旧 goods 域 {@code category} 表一行。market 域不得直连该表,
 * 只经 {@link GoodsCategoryApi} 取此扁平结构。</p>
 *
 * @author KC
 */
@Data
public class PlatformCategoryInfo implements Serializable {

    /**
     * 平台分类ID。
     */
    private Long id;

    /**
     * 平台父分类ID; 顶层为 0。
     */
    private Long pid;

    /**
     * 分类名称。
     */
    private String name;

    /**
     * 分类描述。
     */
    private String desc;

    /**
     * 分类图片。
     */
    private String img;
}
