package com.newzkl.platform.base.biz.market.model.suggest.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;

import java.util.List;

/**
 * 提交建议标签请求。
 *
 * <p>迁移自 {@code com.zkl.scm.market.domain.other.model.req.CommitTagReq}。</p>
 *
 * @author KC
 */
@Data
public class CommitTagReq extends BaseReq {

    /**
     * 标签配置。
     */
    private List<String> tagConfig;

    /**
     * 现有资源标签。
     */
    private List<String> nowTag;

    /**
     * 备注。
     */
    private String remark;
}
