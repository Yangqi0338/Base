package com.newzkl.platform.base.biz.goods.model.goods.res.spu;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuExpandVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SPU 审核出参
 *
 * <p>SPU 审核列表专用瘦出参, 仅透出审核决策必需字段, 去掉 {@code SpuVO} 的销量/销售额/售后量/
 * 选品数/SKU 列表/属性列表/视频列表等分析与详情类冗余。id 与 createTime 继承自 {@link BaseRes};
 * 分类名/品牌名/账号名与价格区间由 {@link SpuExpandVO} 承载并经 {@code @JsonUnwrapped} 平展输出,
 * 与 {@code SpuVO} 对外字段口径一致; 金额字段为 {@link Money}, 经全局 Jackson 序列化器输出元为
 * 单位两位小数字符串</p>
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpuAuditRes extends BaseRes {

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 标题
     */
    private String title;

    /**
     * 主图
     */
    private String img;

    /**
     * 商品类型 (0实物商品/1课程/2服务)
     */
    private Integer goodsType;

    /**
     * 渠道类型
     */
    private SpuEnum.ChannelType channelType;

    /**
     * 供应商账号 ID
     */
    private Long accountId;

    /**
     * 所属平台分类 ID
     */
    private Long categoryId;

    /**
     * 品牌 ID
     */
    private Long brandId;

    /**
     * 状态 (0仓库中/2上架中/3待上架)
     */
    private Integer state;

    /**
     * 审批状态 (0待用户提交/1待审核/2通过/3未通过/4终止)
     */
    private Integer auditState;

    /**
     * 最后拒绝原因
     */
    private String lastRefuseReason;

    /**
     * 市场价 (元字符串两位小数)
     */
    private Money marketPrice;

    /**
     * 供货价 (元字符串两位小数)
     */
    private Money supplyPrice;

    /**
     * 销售价 (元字符串两位小数)
     */
    private Money salePrice;

    /**
     * 建议零售价 (元字符串两位小数)
     */
    private Money unitPrice;

    /**
     * 扩展字段 (由 {@code @JsonUnwrapped} 平展输出, 对外契约与 SpuVO 一致)
     */
    @JsonUnwrapped
    private SpuExpandVO expand;
}
