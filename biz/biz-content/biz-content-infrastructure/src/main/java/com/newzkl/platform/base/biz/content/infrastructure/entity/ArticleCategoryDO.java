package com.newzkl.platform.base.biz.content.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章分类数据实体
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.entity.ArticleCategoryDO}。</p>
 *
 * <p><b>不继承 </b>{@code BaseDO}: 旧 {@code article_category} 表<b>无</b>
 * {@code update_time} / {@code del_flag} / {@code executor} 列, 继承会令 MyBatis-Plus
 * 自动填充并拼入不存在的列, 运行期 SQL 直接报错。故按旧表列逐字声明。</p>
 *
 * @author KC
 */
@Data
@TableName("article_category")
public class ArticleCategoryDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序值(小于100, 相同时按创建时间倒序)
     */
    private Integer sort;

    /**
     * 文章数量
     */
    private Integer articleCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否启用(0-禁用, 1-启用)
     */
    private Integer isEnabled;

    /**
     * 推荐人群(逗号分隔)
     *
     * @see RecommendGroupEnum
     */
    private String recommendGroups;
}
