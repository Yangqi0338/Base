package com.newzkl.platform.base.biz.order.model.support.api.spu;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.facade.SkuRpcVO;
import com.newzkl.platform.base.common.ddd.facade.SpuAttributeRpcVO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * spu
 * @author fang
 */
@Data
public class SpuRpcVO extends BaseRes implements Serializable {
     /**
     * ID (查询)
     */
     private Long id;
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
     * 标题 (查询)
     */
     private String title;
     /**
     * 轮播图
     */
     private String scrollImg;
     /**
      * ** 搜索关键字,逗号隔开
      */
     private String searchKey;
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
     * 详情
     */
     private String detail;
     /**
     * 自定义类型. 0 供应商商品 (查询)
     */
     private Integer customType;
     /**
     * 商品类型 0:实物商品 1:课程 2:服务 (查询)
     */
     private Integer goodsType;
     /**
     * 账号ID (查询)
     */
     private Long accountId;
     /**
     * 所属平台分类 (查询)
     */
     @NotNull(message = "categoryId?")
     private Long categoryId;
     /**
     * 所属平台分类名称完整
     */
     private String categoryName;
     /**
     * 品牌id (查询)
     */
     private Long brandId;
     /**
     * 品牌名称
     */
     private String brandName;
     /**
     * 运费模板id (查询)
     */
     private Long freightTemplateId;
     /**
     * 发货时效类型 0 三日内 1 大于三日 (查询)
     */
     private Integer deliverTimeType;
     /**
     * 最大发货天数
     */
     private Integer maxDeliverDay;
     /**
     * 状态 0:仓库中 2:上架中 3:待上架 (查询)
     */
     private Integer state;
     /**
      * 总销量
      */
     private Integer saleNum;
    /**
     * 虚拟销量
     */
    private Integer virtualSaleNum;
     /**
      * 选品数量
      */
     private Integer selectionNum;
     /**
      * 平台销售额
      */
     private Integer adminSaleAmount;
     /**
      * 渠道商销售额
      */
     private Integer channelSaleAmount;
     /**
      * 成交数量
      */
     private Integer dealNum;
     /**
      * 售后数量
      */
     private Integer refundNum;
     /**
      * sku列表
      */
     @NotEmpty(message = "skuList?")
     private List<SkuRpcVO> skuList;
     /**
      * 销售属性
      */
     @NotEmpty(message = "spuSaleAttributeList?")
     private List<SpuAttributeRpcVO> spuSaleAttributeList;
     /**
      * 参数属性
      */
     private List<SpuAttributeRpcVO> spuParamAttributeList;
     /**
      * 冗余: 账号名称
      */
     private String accountName;
     /**
      * 冗余: 市场价
      */
     private Integer marketPrice;
     /**
      * 冗余: 供货价
      */
     private Integer supplyPrice;
     /**
      * 冗余: 毛利率
      */
     private Integer profit;
     /**
      * 追加:审批ID
      */
     private Long flowId;
     /**
      * 追加:审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过",4,"终止")
      */
     private Integer auditState;
     /**
      * 追加: 市场数量
      */
     private Integer marketNum;
     /**
      * 追加:最后拒绝原因: 状态变更未待用户提交前的最后一次拒绝原因
      */
     private String lastRefuseReason;
     /**
      * 冗余: 销售价起始
      */
     private Integer salePriceBegan;
     /**
      * 冗余: 销售价结束
      */
     private Integer salePriceEnd;
     /**
      * 冗余: 供货价起始
      */
     private Integer supplierPriceBegan;
     /**
      * 冗余: 供货价结束
      */
     private Integer supplierPriceEnd;

     /** 是否选中 */
     private Boolean choose;
}