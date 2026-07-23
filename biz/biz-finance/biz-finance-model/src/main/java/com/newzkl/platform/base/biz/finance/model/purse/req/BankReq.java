package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.biz.finance.model.purse.vo.BankBranchVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 银行(Bank)实体类
 *
 * @author kc
 * @since 2025-09-18 09:17:58
 */
@Data
public class BankReq implements Serializable {
    private static final long serialVersionUID = 629140036425054081L;
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 银行编码
     */
    private String bankCode;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 支行列表
     */
    private List<BankBranchVO> branchList;

}

