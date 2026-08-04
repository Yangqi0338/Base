package com.newzkl.platform.base.biz.content.model.video.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 推荐视频视图对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.vo.RecommendVideoVO}。</p>
 *
 * <p><b>跨域降级字段</b>: {@code issuer}/{@code issuerImg}/{@code isFollowed}/{@code shareNum}/
 * {@code likeNum}/{@code isLike} 原由用户域/商品域 RPC facade 填充, Base 现无对应 facade,
 * 迁移后不再填充。详见 {@code VideoDomainImpl} 内 TODO[cross-domain]。</p>
 *
 * @author KC
 */
@Data
public class RecommendVideoVO implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 视频名称
     */
    private String name;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 视频封面地址
     */
    private String videoCoverUrl;

    /**
     * 发布人id
     */
    private Long issuerId;

    /**
     * 发布人头像(跨域降级, 暂不填充)
     */
    private String issuerImg;

    /**
     * 发布人(跨域降级, 暂不填充)
     */
    private String issuer;

    /**
     * 是否已关注发布人(跨域降级, 暂不填充)
     */
    private Boolean isFollowed;

    /**
     * 分享次数(跨域降级, 暂不填充)
     */
    private Integer shareNum;

    /**
     * 点赞数(跨域降级, 暂不填充)
     */
    private Integer likeNum;

    /**
     * 是否已点赞(跨域降级, 暂不填充)
     */
    private Boolean isLike;
}
