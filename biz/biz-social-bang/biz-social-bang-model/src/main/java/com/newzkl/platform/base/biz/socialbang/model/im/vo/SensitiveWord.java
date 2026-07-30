package com.newzkl.platform.base.biz.socialbang.model.im.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 敏感词领域实体
 *
 * <p>迁移自 {@code com.zkl.scm.im.structure.tencent.entity.SensitiveWord}。
 * 源类同时充当 DO 与对外返回体; 迁移后按 Base 分层拆分: 本类留在 model 作领域实体/返回体,
 * 持久化字段映射下沉 {@code SensitiveWordDO}。对外 JSON 字段名与源保持逐字一致。</p>
 *
 * @author KC
 */
@Data
public class SensitiveWord {

    /**
     * 主键ID
     */
    private Long id;

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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
