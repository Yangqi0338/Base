package com.newzkl.platform.base.biz.goods.domain.spu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuAttributeQuery;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuCategoryQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.spu.OutSpuEditCommand;
import com.newzkl.platform.base.biz.goods.model.goods.req.spu.SpuCategoryReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.spu.SpuUpdateRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.facade.GoodsCountVO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SkuQuery;
import com.newzkl.platform.base.common.ddd.facade.SpuQuery;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;

import java.util.List;

/**
 * spu领域
* @author fang
*/
public interface SpuDomain {
    /**
     * spu草稿创建
     */
    Long spuPreSave(SpuDTO spuDTO);

    /**
     * spu创建
     */
    Long spuCreate(SpuDTO spuDTO);

    /**
     * spu删除并新增
     */
    void spuPreUpdate(SpuDTO spuDTO);

    /**
     * spu实体
     */
    SpuDTO getById(Long id);

    /**
     * spu修改
     */
    SpuUpdateRes spuUpdate(SpuDTO spuDTO);
    /**
     * 商品审批通过
     * @param spuVO 供应商提交的商品
     * @param skuSalePriceJson 平台修改的数据
     */
    void spuAuditSuccess(SpuVO spuVO, String skuSalePriceJson);
    /**
     * 上下架
     * @param enable
     * @param spuIdList
     */
    int spuUp(Integer enable, List<Long> spuIdList);

    /**
     * 上下架
     * @param enable
     * @param spuIdList
     */
    int platformSpuUp(Integer enable, List<Long> spuIdList);
    /**
     * Sku列表
     * @param skuQuery
     * @return
     */
    List<SkuVO> skuVOList(SkuQuery skuQuery);
    /**
     * 商品上传审核失败
     * @param spuVO
     * @param lastRefuseReason
     */
    void spuAuditFail(SpuVO spuVO, String lastRefuseReason);
    /**
     * 商品上传提交成功
     * @param spuId
     * @param flowId
     */
    void spuSubmit(Long spuId, Long flowId);
    /**
     * 商品上传终止
     * @param spuVO
     */
    void spuAuditStop(SpuVO spuVO);

    void editColumn(Long spuId, List<EditColumnVO> editColumnDTOS);

    void spuDelete(List<Long> spuIdList);

    /**
     * 查询订单商品信息
     * @param goods
     * @param channelId
     * @param storeId 门店id 0：api接口查选品  >0：门店id，查铺货
     * @return
     */
    //List<OrderGoodsInfoVO> queryOrderGoodsInfoVOList(List<GoodsVO> goods, Long channelId, Long storeId);
    /**
     * 外部商品修改
     * @param outSpuEditCommand
     */
    void outSpuEdit(OutSpuEditCommand outSpuEditCommand);

    //OrderGoodsInfoVO queryOrderSkuInfoVOList(Long skuId);

    /**
     * spu分页查询
     */
    Page<SpuVO> querySpuPage(SpuQuery spuQuery);

    /**
     * sku分页查询
     */
    Page<SkuVO> querySkuPage(SkuQuery skuQuery);

    /**
     * spu批量删除
     */
    SpuVO voByQuery(SpuQuery spuQuery);

    /**
     * spuAttribute批量查询
     */
    List<SpuAttributeVO> querySpuAttributeList(SpuAttributeQuery spuAttributeQuery);

    /**
     * spu批量查询
     */
    List<SpuVO> listSelect(SpuQuery spuQuery);

    /**
     * 商品统计
     */
    GoodsCountVO goodsCountVO(Long supplierId);

    /**
     * sku查询
     */
    SkuVO skuVO(Long skuId);

    /**
     * spuAttribute查询
     */
    SpuAttributeVO spuAttributeById(Long spuAttributeId);

    /**
     * spuAttribute分页查询
     */
    Page<SpuAttributeVO> querySpuAttributePage(SpuAttributeQuery spuAttributeQuery);

    /**
     * 分类保存
     *
     * @return
     */
    Long categorySave(SpuCategoryReq categoryReq);

    /**
     * 分类删除
     *
     * @return
     */
    void categoryDelete(List<Long> idList);

    /**
     * 分类列表
     *
     * @param categoryQuery
     * @return
     */
    List<SpuCategoryVO> categoryList(SpuCategoryQuery categoryQuery);

    /**
     * 分类详情
     *
     * @param id
     * @return
     */
    SpuCategoryVO category(Long id);

    /**
     * 分类树
     *
     * @param categoryQuery
     * @return
     */
    List<SpuCategoryVO> categoryTree(SpuCategoryQuery categoryQuery);
}
