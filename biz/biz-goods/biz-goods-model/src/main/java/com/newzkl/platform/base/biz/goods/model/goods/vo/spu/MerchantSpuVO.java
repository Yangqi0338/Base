package com.newzkl.platform.base.biz.goods.model.goods.vo.spu;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.goods.model.enums.user.identity.RoleEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * spu
 *
 * @author fang
 */
@Data
public class MerchantSpuVO extends BaseRes implements Serializable {
    /**
     * 名称 (查询)
     */
    public String name;
    /**
     * 编码 (查询)
     */
    private String code;
    /**
     * 标题 (查询)
     */
    private String title;
    /**
     * 轮播图
     */
    private String scrollImg;
    /**
     * 图片
     */
    @NotEmpty(message = "img?")
    private String img;
    /**
     * 视频
     */
    private String video;
    /**
     * 账号ID (查询) 0 平台 1 怡亚通
     */
    private Long accountId;
    /**
     * 角色ID
     */
    private RoleEnum.CompanyRole role;
    /**
     * 最大利润
     */
    private Integer maxProfit;
    /**
     * 冗余: 账号名称
     */
    private String accountName;
    /**
     * 总销量
     */
    private Integer saleNum;
    /**
     * 虚拟销量
     */
    private Integer virtualSaleNum;
    /**
     * 冗余: 供货价
     */
    private Integer supplyPrice;
    /**
     * 冗余: 销售价
     */
    private Integer salePrice;
    /**
     * 冗余: 库存
     */
    private Integer inventory;
    /**
     * 冗余: 是否上传了视频
     */
    private Integer uploadVideoFlag;
}