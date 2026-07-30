package com.newzkl.platform.base.biz.content.model.videocategory.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 视频分类权重
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.vo.VideoCategoryWeightVO}。
 * 旧类仅有两参构造, 此处补默认构造以支持 bean 拷贝。</p>
 *
 * @author KC
 */
@Data
@NoArgsConstructor
public class VideoCategoryWeightVO implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 权重值
     */
    private Integer weight;

    /**
     * 权重比例
     */
    private Integer weightRatio;

    /**
     * 构造视频分类权重
     *
     * @param id     主键ID
     * @param weight 权重值
     */
    public VideoCategoryWeightVO(Long id, Integer weight) {
        this.id = id;
        this.weight = weight;
    }
}
