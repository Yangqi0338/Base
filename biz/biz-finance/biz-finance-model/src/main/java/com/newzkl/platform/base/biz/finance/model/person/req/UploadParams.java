package com.newzkl.platform.base.biz.finance.model.person.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连文件上传请求。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UploadParams extends TripartiteBaseParam {

    /**
     * 商户系统唯一交易流水号。
     */
    private String txn_seqno;

    /**
     * 交易时间。
     */
    private String txn_time;

    /**
     * 文件类型, 支持 bmp / png / jpeg / jpg / gif。
     */
    private String file_type;

    /**
     * 内容类型, 如 UBO_IMAGE。
     */
    private String context_type;

    /**
     * 文件名称, 内容类型为 SUPPLEMENT_CSV 时必填。
     */
    private String file_name;

    /**
     * 文件内容。文件流 Base64 编码 (不带 {@code data:image/png;base64} 前缀), 最大 6M。
     */
    private String file_context;
}
