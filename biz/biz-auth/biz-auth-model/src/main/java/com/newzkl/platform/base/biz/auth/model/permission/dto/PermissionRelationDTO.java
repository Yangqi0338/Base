package com.newzkl.platform.base.biz.auth.model.permission.dto;

import com.newzkl.platform.base.biz.auth.model.enums.RelationEnum;
import lombok.Data;

/**
 * 权限关系数据传输对象
 *
 * @author KC
 */
@Data
public class PermissionRelationDTO {

    /** 主键ID */
    private Long id;

    /** 关系类型 */
    private RelationEnum.Type type;

    /** 源对象ID */
    private Long sourceId;

    /** 目标对象ID */
    private Long targetId;

    /** 关系来源 */
    private RelationEnum.Source source;
}
