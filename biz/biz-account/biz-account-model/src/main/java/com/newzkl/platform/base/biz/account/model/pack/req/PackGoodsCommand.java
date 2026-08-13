package com.newzkl.platform.base.biz.account.model.pack.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * 入会礼包商品写入入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.packgoods.model.command.PackGoodsCommand}。
 * 旧 {@code jakarta.validation} 改 {@code jakarta.validation}。</p>
 *
 * @author KC
 */
@Data
public class PackGoodsCommand implements Serializable {

    /**
     * 礼包商品 ID（新增为空）
     */
    private Long id;

    /**
     * 类型（角色 ID）
     */
    private Integer type;

    /**
     * 礼包等级
     */
    private Integer level;

    /**
     * 礼包金额（分）
     */
    private Integer amount;

    /**
     * 礼包名称
     */
    private String name;

    /**
     * 礼包图片
     */
    @NotEmpty
    private String img;

    /**
     * 礼包简介
     */
    private String desc;

    /**
     * 状态 0 下架 1 上架
     */
    private Integer state;
}
