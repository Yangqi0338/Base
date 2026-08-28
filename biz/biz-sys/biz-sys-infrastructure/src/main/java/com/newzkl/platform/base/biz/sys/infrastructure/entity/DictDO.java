package com.newzkl.platform.base.biz.sys.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.mybatis.handler.RawJsonStringTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.TableIndex;
import org.dromara.autotable.annotation.TableIndexes;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

/**
 * 字典数据对象
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap = true)
@TableIndexes({
        @TableIndex(name = "key", type = IndexTypeEnum.UNIQUE, fields = {"code", "delFlag"})
})
public class DictDO extends BaseDO {

    /**
     * 字典业务键
     *
     * <p>稳定业务键, 对应 DictEnum.Key 写死码值, 与物理主键 id 解耦</p>
     */
    private Long code;

    /**
     * 字典值
     */
    @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class)
    private String value;

    /*
     * 字典描述
     */
    @TableField("`desc`")
    private String desc;
}
