package com.newzkl.platform.base.biz.account.facade.model;



import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 供应商查询入参
 *
 * @author KC
 */
@Data
public class SupplierRpcQuery extends BizPageQuery {

    /**
     * 状态 (查询)
     * @ext 0 未开通 1 已开通; 无对应枚举, 保留 Integer
     */
    private Integer state;
    /**
     * 状态集合
     * @ext 0 未开通 1 已开通 2 已入驻; 无对应枚举, 保留 List
     */
    private List<Long> stateList;

    /**
     * 是否缴纳保证金 (查询)
     */
    private Integer promisePayState;
    /**
     * 是否设置账期 (查询)
     */
    private Integer periodSetState;
    /**
     * 账号名称 (查询)
     */
    private String username;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 审批状态
     * @ext 0 待用户提交 1 待审核 2 通过 3 未通过 4 终止; 无对应枚举, 保留 Integer
     */
    private Integer auditState;
    /**
     * 邀请人ID
     */
    private Long inviteId;
    /**
     * 创建时间开始
     */
    private LocalDateTime createTimeBegin;
    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;

    /**
     * 行业ID集合
     */
    private Long industryId;

    /**
     * 企业区域
     */
    private Long companyAreaCode;


}
