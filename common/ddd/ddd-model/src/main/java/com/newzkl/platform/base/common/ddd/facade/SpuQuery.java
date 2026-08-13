package com.newzkl.platform.base.common.ddd.facade;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
* spu
* @author fang
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class SpuQuery extends BizPageQuery implements Serializable {

    /**
     * 市场ID
     */
    private Long marketId;

    /**
     * 关联类型
     */
    private RoleEnum.CompanyRole role;

    /**
     * 关联类型
     */
    private Integer relationType;

    /**
     * 编码列表
     */
    private List<String> codeList;

    /**
     * 所属平台分类集合
     */
    private List<Long> categoryIdList;

    /**
    * 名称 (查询)
    */
    private String name;
    /**
    * 标题 (查询)
    */
    private String title;
    /**
    * 自定义类型. 0 供应商商品 (查询)
    */
    private Integer customType;
    /**
    * 商品类型 0:实物商品 1:课程 2:服务 (查询)
    */
    private Integer goodsType;
    /**
     * 发货时效类型
    */
    private SpuEnum.DeliverTimeType deliverTimeType;
    /**
     * 状态
     */
    private List<SpuEnum.State> stateList;

    /**
    * 品牌id (查询)
    */
    private Long brandId;
    /**
    * 运费模板id (查询)
    */
    private Long freightTemplateId;
    /**
     * 审批状态
     */
    private List<AuditEnum.State> auditStateList;
    /**
     * 渠道类型
     */
    private SpuEnum.ChannelType channelType;
    /**
     * 运营类型
     */
    /**
     * 规格类型
     */
    private SpuEnum.SpecType specType;

    public void setCode(String code) {
        this.codeList = doWrapperList(codeList, code);
    }

    public void setCategoryId(Long categoryId) {
        this.categoryIdList = doWrapperList(categoryIdList, categoryId);
    }

    public SpuEnum.State getState() {
        return CollUtil.getFirst(stateList);
    }

    /**
     * 冗余: 账号名称
     */
    private String accountName;
    /**
     * 供货价起始
     */
    private Integer supplyPriceStart;
    /**
     * 供货价结束
     */
    private Integer supplyPriceEnd;
    /**
     * 零售价起始
     */
    private Integer salePriceStart;
    /**
     * 零售价结束
     */
    private Integer salePriceEnd;
    /**
     * 利润起始
     */
    private Integer profitStart;
    /**
     * 利润结束
     */
    private Integer profitEnd;
    /**
     * 销量起始
     */
    private Integer saleNumStart;
    /**
     * 销量结束
     */
    private Integer saleNumEnd;

    public void setState(SpuEnum.State state) {
        this.stateList = doWrapperList(stateList, state);
    }
    /**
     * 外部供应链商品ID
     */
    private String outSpuId;
    /**
     * 供应商id集合
     */
    private List<Long> supplierIdList;

    public AuditEnum.State getAuditState() {
        return CollUtil.getFirst(auditStateList);
    }

    public void setAuditState(AuditEnum.State auditState) {
        this.auditStateList = doWrapperList(auditStateList, auditState);
    }
    /**
     * 最小计价数
     */
    private Double minPricingNum;
    @AllArgsConstructor
    @Getter
    public enum SpuSortType implements SortField {
        /**
         * 默认
         */
        DEFAULT(1, "id"),
        /**
         * 销量
         */
        SALE_NUM(2, "(sale_num + virtual_sale_num)"),
        /** 供应商价格 */
        SUPPLIER_PRICE(3, "supplier_price_began"),
        /** 利润 */
        PROFIT(4, "(unit_price - sale_price)"),
        ;

        private final Integer code;
        private final String field;

        @Override
        public String getField(Integer code) {
            return Arrays.stream(SpuSortType.values()).filter(item -> item.getCode().equals(code)).findFirst()
                    .map(SpuSortType::getField).orElse(null);
        }
    }
}
