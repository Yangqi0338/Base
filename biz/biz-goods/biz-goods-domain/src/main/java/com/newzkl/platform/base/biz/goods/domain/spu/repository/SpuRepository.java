package com.newzkl.platform.base.biz.goods.domain.spu.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SkuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuAttributeDTO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuAttributeQuery;
import com.newzkl.platform.base.biz.goods.model.goods.res.spu.IndexCountRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuStateVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.biz.goods.rpc.model.count.GoodsCountVO;
import com.newzkl.platform.base.biz.goods.rpc.model.order.GoodsVO;
import com.newzkl.platform.base.biz.goods.rpc.model.order.OrderGoodsInfoVO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SkuQuery;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SpuCountQuery;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SpuQuery;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* spu
* @author fang
*/
public interface SpuRepository {

    /**
     * spu保存
     */
    Long spuSave(SpuDTO spu);

    /**
     * spu删除
     */
    void spuDelete(Long id);

    /**
     * spu实体
     */
    SpuDTO getById(Long id);

    /**
     * spu修改
     */
    void spuUpdate(SpuDTO spu);

    /**
     * spu批量删除
     */
    void deleteByQuery(SpuQuery spuQuery);

    /**
     * spu查询
     */
    SpuVO voByQuery(SpuQuery spuQuery);

    /**
     * spu批量查询
     */
    List<SpuVO> listSelect(SpuQuery spuQuery);

    /**
     * spu分页查询
     */
    Page<SpuVO> querySpuPage(SpuQuery spuQuery);

    /**
     * sku分页查询
     */
    Page<SkuVO> querySkuPage(SkuQuery skuQuery);

    /**
     * sku修改
     */
    void skuUpdate(SkuDTO skuDTO);

    /**
     * sku保存
     */
    void skuSave(SkuDTO skuDTO);
    /**
     * sku批量保存
     */
    void skuListSave(List<SkuDTO> skuDTOList);
    /**
     * sku集合查询
     */
    List<SkuVO> skuVOList(SkuQuery skuQuery);

    /**
     * sku批量删除
     */
    void skuDeleteByQuery(SkuQuery skuDelete);

    /**
     * sku删除
     */
    void skuDeleteBySpuId(Long spuId);

    /**
     * sku查询
     */
    SkuVO skuVO(Long skuId);

    /**
     * spuAttribute批量保存
     */
    void spuAttributeListSave(List<SpuAttributeDTO> spuAttributeDTOList);
    /**
     * spuAttribute批量删除
     */
    void attributeDeleteByQuery(SpuAttributeQuery spuAttributeQuery);
    /**
     * spuAttribute查询
     */
    SpuAttributeVO spuAttributeById(Long spuAttributeId);
    /**
     * spuAttribute分页查询
     */
    Page<SpuAttributeVO> querySpuAttributePage(SpuAttributeQuery spuAttributeQuery);
    /**
     * spuAttribute批量查询
     */
    List<SpuAttributeVO> querySpuAttributeList(SpuAttributeQuery spuAttributeQuery);
    /**
     * spuAttribute删除
     */
    void spuAttributeDeleteBySpuId(Long spuId);

    List<SpuCategoryVO> countSpuByCategory(List<Long> categoryIdList);

    List<SpuStateVO> spuStateList(SpuQuery spuQuery);

    void editColumn(Long id, List<EditColumnVO> editColumnDTOS);

    IndexCountRes indexCount(TimeQuery timeQuery);

    /**
     * 查询订单商品信息
     * storeId 门店id 0：api接口查选品  >0：门店id，查铺货
     * @return
     */
    List<OrderGoodsInfoVO> queryOrderGoodsInfoVOList(List<GoodsVO> goods, Long channelId, Long storeId);

    Long spuId(Integer channelType, String outSpuId);

    void resetSpuOrderCount();

    int editStateById(Integer state, List<Long> spuIdList);

    OrderGoodsInfoVO queryOrderSkuInfoVOList(Long skuId);

    /**
     * SPU下架后置处理
     * @param spuIdList
     */
    void spuDownAfter(List<Long> spuIdList);
    /**
     * SPU上架后置处理
     * @param spuIdList
     */
    void spuUpAfter(List<Long> spuIdList);

    /**
     * 刷新加价比例
     * @param skuIdList
     */
    void refreshSalePriceRate(Set<Long> skuIdList);
    /**
     * 刷新销售价
     * @param asList
     */
    void refreshSalePrice(List<Long> asList);
    /**
     * 获取SPU下SKU最大加价比例
     * @param spuId
     * @return
     */
    float maxSalePriceRate(Long spuId);
    /**
     * 开发者通知 SPU 上下架
     * @param spuId
     * @param state
     */
    void notifyUp(Long spuId, Integer state);
    /**
     * 开发者通知 SPU 基本信息
     * @param spuId
     */
    void notifySpuBase(Long spuId);
    /**
     * 开发者通知 SKU 删除
     * @param spuId
     * @param skuId
     */
    void notifySkuDelete(Long spuId, Long skuId);
    /**
     * 开发者通知 SKU 变更
     * @param spuId
     * @param skuId
     */
    void notifySkuEdit(Long spuId, Long skuId);

    SkuVO skuVO(Long spuId, String outSkuId);

    List<Map<String, Object>> countByCondition(SpuCountQuery countQuery);

    /**
     * 商品统计
     *
     * @param supplierId
     * @return
     */
    GoodsCountVO goodsCountVO(Long supplierId);

    void spuSelectorNumAdd(List<Long> spuIdList, Integer num);

}
