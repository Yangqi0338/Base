package com.newzkl.platform.base.biz.market.model.suggest.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.util.List;

/**
 * 提交建议标签视图
 *
 * <p>迁移自 {@code com.zkl.scm.market.domain.other.model.vo.CommitTagVO};
 * 原独立 {@code id} 字段改由 {@code BaseRes} 提供。</p>
 *
 * @author KC
 */
@Data
public class CommitTagVO extends BaseRes {

    /**
     * 标签配置
     */
    private List<String> tagConfig;

    /**
     * 现有资源标签
     */
    private List<String> nowTag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态 0:配置中 1:已配置
     */
    private Integer state;

    /**
     * 提交人手机号
     */
    private String mobile;
}
