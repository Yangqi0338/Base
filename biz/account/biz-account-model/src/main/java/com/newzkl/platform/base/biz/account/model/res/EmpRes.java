package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmpRes extends BaseVO {
    /**
     * 账号
     */
    private String username;
    /**
     * 父id
     */
    private Long accountId;
    /**
     * 岗位ID
     */
    private Long jobId;
    /**
     * 岗位名称
     */
    private Long jobName;
    /**
     * 企业角色集合,逗号隔开
     */
    private String roleIdList;
}
