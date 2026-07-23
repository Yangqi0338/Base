package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * @author fang
 */
@Data
public class AccountStructureVO extends BaseRes {
    /**
     * 角色id
     *
     */
    private String roleIdList;
    /**
     * 父id
     *
     */
    private Long pid;
    /**
     * 父id列表
     */
    private String pidList;
}