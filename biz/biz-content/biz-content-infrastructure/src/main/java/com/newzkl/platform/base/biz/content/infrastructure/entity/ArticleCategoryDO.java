package com.newzkl.platform.base.biz.content.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
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
@TableName
public class ArticleCategoryDO extends BaseDO {
    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序值
     * @ext 小于100, 相同时按创建时间倒序
     */
    private Integer sort;

    /**
     * 文章数量
     */
    private Integer articleCount;

    /**
     * 是否启用
     * @ext 0-禁用, 1-启用
     */
    private CommonEnum.YesOrNo isEnabled;

    /**
     * 推荐人群
     * @ext 逗号分隔
     *
     * @see RecommendGroupEnum
     */
    private String recommendGroups;
}
