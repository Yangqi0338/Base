package com.newzkl.platform.base.biz.goods.application.goods.service.spu;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SupplierSpuStatisticsVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSkuVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuStateVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuVO;
import com.newzkl.platform.base.common.ddd.facade.SpuQuery;
import com.newzkl.platform.base.common.ddd.facade.SupplierSpuStatisticsQuery;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/11/317:50
 */
public interface SpuService {
    /**
     * 供应商提交审核
     * @param accountId
     * @param spuId
     * @return
     */
    Long supplierSpuSubmit(Long accountId, Long spuId);
    /**
     * 上下架
     * @param enable
     * @param spuIdList
     */
    void spuUp(Integer enable, List<Long> spuIdList);
    /**
     * 外部供应链商品同步
     * @param spuDTO
     */
    void outGoodsSync(SpuDTO spuDTO);

    /**
     * 移动 APP 供应商商品统计
     *
     * @param query 统计查询
     * @return 供应商商品统计
     */
    SupplierSpuStatisticsVO supplierSpuStatistics(SupplierSpuStatisticsQuery query);

    /**
     * 运营商查其可见供应商的商品分页
     *
     * <p>按登录运营商的运营类型 (机构 / 行业 / 区域) 拿到可见供应商 ID 列表, 收敛查询范围后走商品分页。</p>
     *
     * @param spuQuery 商品查询 (type 为必填运营类型, accountId 由入口回填为登录运营商)
     * @return 商品分页; 无可见供应商时返回空页
     */
    Page<SpuVO> operatorSpuPage(SpuQuery spuQuery);

    /**
     * 货盘选择商品
     */
    Long palletSelectGoods(SpuVO spuVO);

    /**
     * 商品选品数量增加
     */
    void spuSelectorNumAdd(List<Long> spuIdList, Integer num);

    /**
     * 验证供应商商品
     */
    void validateSupplierSpu(SpuDTO spuDTO, Long accountId);

    /**
     * API-SPU状态
     */
    List<ApiSpuStateVO> apiSpuState(Long accountId, List<Long> spuIdList);

    /**
     * API-SPU列表
     */
    List<ApiSpuVO> apiSpuVOList(Long accountId, List<Long> spuIdList);

    /**
     * API-SKU列表
     */
    List<ApiSkuVO> apiSkuVOList(Long accountId, List<Long> spuIdList);
}
