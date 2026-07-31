package com.newzkl.platform.base.biz.store.model.template.res;

import com.newzkl.platform.base.biz.store.model.enums.AuditEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 样板店VO
 * @date 2024/4/7 14:54
 */
@Data
public class ModelShopRes {
    /** 主键ID */
    private Long id;

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 样板店名称
     */
    private String modelShopName;

    /**
     * 样板店描述
     */
    private String modelDescription;

    /**
     * 审核状态
     * @see AuditEnum.State
     */
    private Integer auditState;

    /**
     * 审核信息
     */
    private String auditInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 样式code
     */
    private String styleCode;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 创建人名
     */
    private String createName;

    /**
     * 使用门店数
     */
    private Integer useStoreNum;

    /**
     * 来源模板code
     */
    private String sourceCode;

    /**
     * 来源模板名称
     */
    private String sourceName;

    /**
     * 状态:0正常，1已禁用
     */
    private Integer state;

    /**
     * 预览图
     */
    private String previewImage;

}
