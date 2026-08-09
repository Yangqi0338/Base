package com.newzkl.platform.base.biz.content.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章数据实体
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.entity.ArticleDO}。</p>
 *
 * <p><b>不继承 </b>{@code BaseDO}: 旧 {@code article} 表列由旧 DO 逐字声明,
 * 无 {@code update_time}/{@code del_flag} 等基类列, 继承会令自动填充拼入不存在的列。</p>
 *
 * @author KC
 */
@Data
@TableName(autoResultMap = true)
public class ArticleDO extends BaseDO {

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 封面图URL
     */
    private String coverImage;

    /**
     * 海报轮播图URL列表
     * @ext JSON数组, 源列 poster_images
     */
    private String posterImages;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 是否显示
     * @ext 0-不显示, 1-显示
     */
    private CommonEnum.YesOrNo isVisible;
}
