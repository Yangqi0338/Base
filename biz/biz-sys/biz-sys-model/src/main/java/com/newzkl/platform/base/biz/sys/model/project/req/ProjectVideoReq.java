package com.newzkl.platform.base.biz.sys.model.project.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 项目视频请求对象
 *
 * <p>迁移说明: 源 {@code ProjectVideoVO} 同时充当入参与出参; 此处按 Base 约定拆为
 * {@code ProjectVideoReq}(入参) 与 {@code ProjectVideoRes}(出参), JSON 字段名保持一致。
 * 源用 {@code org.hibernate.validator.constraints.Length}, Base 未引入 hibernate-validator
 * 私有约束, 改用等价的 jakarta {@code Size}, 校验语义 (最大长度) 与提示文案不变。</p>
 *
 * @author KC
 */
@Data
public class ProjectVideoReq {

    /**
     * 主键
     *
     * <p>为空表示新增视频, 非空表示更新既有视频。</p>
     */
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "视频名称不能为空")
    @Size(max = 4, message = "视频名称超过4个字符上限")
    private String name;

    /**
     * 视频地址
     */
    @NotBlank(message = "视频地址不能为空")
    private String url;

    /**
     * 顺序
     *
     * <p>由服务端按列表次序从 0 重排, 入参值不生效。</p>
     */
    private Integer index;

    /**
     * 视频额外信息
     */
    private String extra;
}
