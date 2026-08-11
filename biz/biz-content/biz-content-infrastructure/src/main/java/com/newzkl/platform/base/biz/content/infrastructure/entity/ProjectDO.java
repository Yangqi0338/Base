package com.newzkl.platform.base.biz.content.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;

/**
 * 项目数据对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class ProjectDO extends BaseDO {

    /**
     * 名称
     */
    private String name;

    /**
     * 简介
     *
     * @ext {@code desc} 为 SQL 保留字, 列名需反引号包裹。
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 省编码
     */
    private Integer province;

    /**
     * 市编码
     */
    private Integer city;

    /**
     * 区编码
     */
    private Integer area;

    /**
     * 合作金额
     *
     * @ext Money 类型, 落库 BIGINT 分
     */
    private Money basicAmount;

    /**
     * 标签
     *
     * @ext 逗号分隔串
     */
    private String flags;

    /**
     * 详情
     */
    @ColumnType(value = MysqlTypeConstant.TEXT)
    private String detail;

    /**
     * 意向人数
     */
    private Integer interestNum;

    /**
     * 意向人
     */
    private String interestPerson;
}
