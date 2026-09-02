package com.newzkl.platform.base.biz.content.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.dto.AccountVO;
import com.newzkl.platform.base.common.ddd.model.dto.ExecutorDTO;
import lombok.Data;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章数据实体
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
    @ColumnType(value = MysqlTypeConstant.TEXT)
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

    /**
     * 发布人信息
     */
    @JsonSerializable
    protected AccountVO issuer;
}
