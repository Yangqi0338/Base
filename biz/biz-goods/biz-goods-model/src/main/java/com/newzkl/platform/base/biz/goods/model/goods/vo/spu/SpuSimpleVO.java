package com.newzkl.platform.base.biz.goods.model.goods.vo.spu;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * spu
 *
 * @author fang
 */
@Data
public class SpuSimpleVO extends BaseRes implements Serializable {
    /**
     * 编码 (查询)
     */
    private String code;
    /**
     * 名称 (查询)
     */
    @NotEmpty(message = "name?")
    private String name;
    /**
     * 图片
     */
    @NotEmpty(message = "img?")
    private String img;
}