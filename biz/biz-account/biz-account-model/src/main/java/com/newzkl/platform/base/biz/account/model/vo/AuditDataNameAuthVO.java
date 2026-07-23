package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

/**
 * 实名认证审核数据
 *
 * @author fang
 */
@Data
public class AuditDataNameAuthVO extends BaseVO {
    /**
     * 姓名 查询
     */
    private String name;
    /**
     * 实名认证信息
     */
    private String nameAuthInfo;
}