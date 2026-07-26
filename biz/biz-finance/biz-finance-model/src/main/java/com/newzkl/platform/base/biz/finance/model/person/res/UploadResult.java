package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连文件上传响应。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UploadResult extends LianLianBaseRes {

    /**
     * 文件 ID。
     */
    private String doc_id;

    /**
     * 商户系统唯一交易流水号。
     */
    private String txn_seqno;
}
