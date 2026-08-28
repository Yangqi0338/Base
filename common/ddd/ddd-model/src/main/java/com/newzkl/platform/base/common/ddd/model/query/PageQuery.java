package com.newzkl.platform.base.common.ddd.model.query;

import com.newzkl.platform.base.common.ddd.model.auth.OauthUserInjection;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

/**
 * @author god
 */
@Data
@OauthUserInjection
public class PageQuery extends QuerySupport {
    /**
     * 当前页
     */
    private Integer pageNo = 0;
    /**
     * 每页的数量
     */
    private Integer pageSize = 0;
    /**
     * 不分页
     */
    private boolean nonPaged = false;

    public <T extends PageQuery> T setNonPaged(boolean nonPaged) {
        this.nonPaged = nonPaged;
        if (nonPaged) {
            resetQueryList();
        }
        return (T) this;
    }

    /* 设置为普通模式 */
    public <T extends PageQuery> T reset() {
        this.pageNo = 1;
        this.pageSize = 10;
        return (T) this;
    }

    /* 仅分页 */
    public <T extends PageQuery> T resetOnlyPage() {
        this.pageNo = 0;
        this.pageSize = -1;
        return (T) this;
    }

    /* 查单个 */
    public <T extends PageQuery> T resetQuerySingle() {
        this.pageNo = 0;
        this.pageSize = 1;
        return (T) this;
    }

    /* 不分页 */
    public <T extends PageQuery> T resetQueryList() {
        this.pageNo = 1;
        this.pageSize = Integer.MAX_VALUE;
        return (T) this;
    }

    @AssertTrue(message = "分页参数错误")
    public boolean isParamsValid() {
        if (nonPaged) {
            return true;
        }
        return (pageNo != null && pageNo > 0) && (pageSize != null && pageSize > 0);
    }

}
