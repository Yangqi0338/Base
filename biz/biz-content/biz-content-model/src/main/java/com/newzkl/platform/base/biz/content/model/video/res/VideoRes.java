package com.newzkl.platform.base.biz.content.model.video.res;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 视频出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.res.VideoRes}。
 * {@code id} / {@code createTime} 由 {@code BaseRes} 提供。</p>
 *
 * <p><b>跨域降级字段</b>: {@code issuer}/{@code issuerImg}(发布人昵称头像)、
 * {@code isFollowed}(是否关注)、{@code isLike}(是否点赞)、{@code shareNum}/{@code likeNum}
 * (分享/点赞数) 原由用户域/商品域 RPC facade 填充。Base 现无对应 facade 且
 * {@code biz-content} 不依赖跨域模块, 这些字段迁移后<b>不再填充</b>, 保留字段仅维持前端契约结构。
 * 详见 {@code VideoDomainImpl} 内 TODO[cross-domain]。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VideoRes extends BaseRes implements Serializable {

    /**
     * 视频名称
     */
    private String name;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 视频封面地址
     */
    private String videoCoverUrl;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 发布人id
     */
    private Long issuerId;

    /**
     * 发布人(跨域降级, 暂不填充)
     */
    private String issuer;

    /**
     * 是否显示:0-不显示,1-显示
     */
    private CommonEnum.YesOrNo isVisible;

    /**
     * 是否已关注发布人(跨域降级, 暂不填充)
     */
    private Boolean isFollowed;

    /**
     * 是否已点赞(跨域降级, 暂不填充)
     */
    private Boolean isLike;

    /**
     * 分享次数(跨域降级, 暂不填充)
     */
    private Integer shareNum;

    /**
     * 点赞数(跨域降级, 暂不填充)
     */
    private Integer likeNum;

    /**
     * 发布人头像(跨域降级, 暂不填充)
     */
    private String issuerImg;
}
