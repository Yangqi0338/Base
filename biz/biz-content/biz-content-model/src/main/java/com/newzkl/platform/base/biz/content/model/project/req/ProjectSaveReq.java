package com.newzkl.platform.base.biz.content.model.project.req;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.check.UpdateCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 项目保存请求对象
 *
 * <p>迁移说明: 源 {@code ProjectSaveCommand}, 按 Base 约定改名为 Req 落 model 层。
 * 源用 {@code org.hibernate.validator.constraints.Length} 限长, Base 未引入
 * hibernate-validator 私有约束, 改用等价 jakarta {@code Size}, 提示文案逐字保留。</p>
 *
 * @author KC
 */
@Data
public class ProjectSaveReq {

    /**
     * 主键 (编辑时必填)
     */
    @NotNull(message = "id不能为空", groups = UpdateCommand.class)
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Size(max = 20, message = "名称超过20个字符上限")
    private String name;

    /**
     * 简介
     */
    @NotBlank(message = "简介不能为空")
    @Size(max = 20, message = "简介超过20个字符上限")
    private String desc;

    /**
     * 省编码
     */
    @NotNull(message = "省份编码不能为空")
    private Integer province;

    /**
     * 市编码
     */
    @NotNull(message = "城市编码不能为空")
    private Integer city;

    /**
     * 区编码
     */
    private Integer area;

    /**
     * 合作金额
     */
    @NotNull(message = "合作金额不能为空")
    private Money basicAmount;

    /**
     * 标签 (最多 5 个, 每个最多 4 字符)
     */
    @Size(max = 5, message = "未设置标签或超过五个上限")
    private List<String> flags;

    /**
     * 详情
     */
    @NotBlank(message = "详情不能为空")
    private String detail;

    /**
     * 是否意向
     *
     * <p>源命令携带此字段但落库不使用 (意向态存缓存, 由 interest 端点维护)。</p>
     */
    private Boolean isInterest;

    /**
     * 视频列表 (最多 10 个)
     */
    @Size(max = 10, message = "未设置视频或超过十个上限")
    @Valid
    private List<ProjectVideoReq> videoList;

    /**
     * 校验标签字符数上限
     *
     * @return 存在超过 4 字符的标签时返回 true (触发校验失败)
     */
    @AssertFalse(message = "标签每个最多四个字符")
    public boolean isCheckCharNum() {
        return flags != null && flags.stream().anyMatch(it -> StrUtil.length(it) > 4);
    }
}
