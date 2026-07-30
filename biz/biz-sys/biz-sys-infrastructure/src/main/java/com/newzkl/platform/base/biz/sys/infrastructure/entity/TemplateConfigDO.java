package com.newzkl.platform.base.biz.sys.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模板配置数据对象
 *
 * <p>迁移说明: 表名与列名与源 {@code TemplateConfigDao.xml} 逐字一致 (下划线映射由
 * MyBatis-Plus 全局配置承接), createTime/updateTime 上提至 {@code BaseDO}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("template_config")
public class TemplateConfigDO extends BaseDO {

    /**
     * 模板业务 ID (如 YB0001)
     */
    private String templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 背景色
     */
    private String themeColor;

    /**
     * 主色
     */
    private String domColor;

    /**
     * 次色
     */
    private String secColor;

    /**
     * 主色文字颜色
     */
    private String domTextColor;

    /**
     * 次色文字颜色
     */
    private String secTextColor;

    /**
     * 状态: 0-停用, 1-启用
     */
    private Integer status;

    /**
     * 应用该模板的门店数量
     */
    private Integer applyStoreCount;

    /**
     * 负责人
     */
    private String responsiblePerson;

    /**
     * 创建人账号 id
     */
    private Long creator;

    /**
     * 是否默认模板: 0-否, 1-是
     */
    private Integer isDefault;
}
