package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.course.model.course.vo.CourseExpandVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCode;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.util.Map;

/**
 * 课程数据对象
 *
 * <p>价格列单位为分, 入参单位为元, 换算在仓储实现完成。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class CourseDO extends BaseDO {

    /**
     * 课程编码
     */
    @BusinessCode(BusinessType.COURSE)
    private String courseNo;

    /**
     * 课程标题
     */
    private String title;

    /**
     * 课程简介
     */
    @ColumnType(length = 2000)
    private String intro;

    /**
     * 讲师ID
     * @ext 关联讲师表主键
     */
    private Long lecturerId;

    /**
     * 课程分类ID
     * @ext 关联课程分类表主键
     */
    private Long categoryId;

    /**
     * 原价
     */
    private Money originalPrice;

    /**
     * 售价
     */
    private Money sellPrice;

    /**
     * 虚拟购买次数
     */
    private Integer virtualPurchaseCount;

    /**
     * 实际购买数量
     */
    private Integer purchaseCount;

    /**
     * 封面图URL
     */
    private String coverImage;

    /**
     * 轮播图URL
     * @ext 多个用逗号分隔
     */
    private String carouselImages;

    /**
     * 视频介绍URL
     */
    private String videoUrl;

    /**
     * 课程详情富文本
     */
    @ColumnType(value = MysqlTypeConstant.TEXT)
    private String details;

    /**
     * 章节总数
     * @ext 冗余字段
     */
    private Integer chapterCount;

    /**
     * 课程总时长
     * @ext 单位百分秒, 冗余字段
     */
    private Long totalDurationCentisecond;

    /**
     * 是否启用
     */
    private CommonEnum.YesOrNo isEnabled;

    /**
     * 扩展信息
     */
    @JsonSerializable
    private CourseExpandVO expand;
}
