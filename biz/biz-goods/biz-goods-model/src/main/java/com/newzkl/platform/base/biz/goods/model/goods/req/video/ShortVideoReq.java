package com.newzkl.platform.base.biz.goods.model.goods.req.video;

import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 商品-短视频请求对象
 *
 * @author KC
 */
@Data
public class ShortVideoReq {

    /**
     * 短视频 ID (更新时必填)
     */
    @NotNull(groups = UpdateCommand.class, message = "ID不能为空")
    private Long id;

    /**
     * 视频路径
     */
    @NotBlank(message = "视频路径不能为空")
    private String path;

    /**
     * 封面路径
     */
    private String coverPath;

    /**
     * 关联的单个 SPU ID (与 spuIdList 合并)
     */
    private Long spuId;

    /**
     * 关联的 SPU ID 列表
     */
    private List<Long> spuIdList;
}
