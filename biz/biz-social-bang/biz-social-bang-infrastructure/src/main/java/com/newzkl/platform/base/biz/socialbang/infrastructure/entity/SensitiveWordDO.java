package com.newzkl.platform.base.biz.socialbang.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.TableIndex;
import org.dromara.autotable.annotation.TableIndexes;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;

import java.time.LocalDateTime;

/**
 * 敏感词持久化对象
 *
 * <p>迁移自 {@code com.zkl.scm.im.structure.tencent.entity.SensitiveWord}(表 {@code sensitive_word})。
 * 偏离说明: 源逻辑删除列为 {@code is_deleted}(0/1), Base 统一由 {@link BaseDO#getDelFlag}
 * 承担({@code del_flag}, 正常 0 / 删除 NULL), 故不再声明 {@code is_deleted}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
@TableIndexes({
        @TableIndex(name = "idx_sensitive_word", type = IndexTypeEnum.UNIQUE, fields = {"sensitive_word"}),
})
public class SensitiveWordDO extends BaseDO {

    /**
     * 敏感词内容
     */
    private String sensitiveWord;

    /**
     * 敏感词添加时间
     */
    private LocalDateTime addTime;

    /**
     * 来源类型: manual-手动添加, batch-批量导入
     */
    private String sourceType;
}
