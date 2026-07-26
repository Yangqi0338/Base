package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.util.List;

/**
 * 角色持久化对象。
 *
 * <p>对应旧表 {@code role} (旧 {@code com.zkl.scm.user.infrastructure.entity.RoleDO})。
 * 语义为"可申请的企业角色配置", 与鉴权角色表 {@code auth_role} 无关。
 * 旧 {@code data_group_id_list} 列以 String 存 JSON 并由业务手工 {@code JSONObject.toJSONString} /
 * {@code parseArray} 转换, 本仓改为 {@link JacksonTypeHandler} 自动映射 (同 {@link LevelDO} 范式,
 * 需 {@code @TableName(autoResultMap = true)})。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "role", autoResultMap = true)
public class RoleDO extends BaseDO {

    /**
     * 角色名称
     */
    @Index
    private String name;

    /**
     * 申请条件
     */
    private String applyCondition;

    /**
     * 提供服务
     */
    private String provideServices;

    /**
     * 当前角色用户量
     */
    private Integer totalUserNum;

    /**
     * 描述
     */
    private String des;

    /**
     * 资料组 ID 集合 (JSON 列)
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> dataGroupIdList;
}
