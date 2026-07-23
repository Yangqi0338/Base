package com.newzkl.platform.base.biz.goods.application.goods.service.goodsZone;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneAddReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZonePageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneRes;

import java.util.List;

/**
 * 商品分组应用服务接口
 * @author sijiwang
 * @since 2026-03-18
 */
public interface IGoodsZoneService {

    /**
     * 新增商品分组
     * @param addReq 新增参数
     * @return 新增结果
     */
    GoodsZoneRes add(GoodsZoneAddReq addReq);

    /**
     * 编辑商品分组
     * @param addReq 编辑参数
     * @return 编辑结果
     */
    GoodsZoneRes edit(GoodsZoneAddReq addReq);

    /**
     * 启用商品分组
     * @param id 分组ID
     * @param operator 操作人
     * @return 是否成功
     */
    boolean enable(Long id, String operator);

    /**
     * 禁用商品分组
     * @param id 分组ID
     * @param operator 操作人
     * @return 是否成功
     */
    boolean disable(Long id, String operator);

    /**
     * 根据ID查询详情
     * @param id 主键ID
     * @return 分组详情
     */
    GoodsZoneRes getById(Long id);

    /**
     * 分页查询分组列表
     * @param queryReq 分页条件
     * @return 分页结果
     */
    Page<GoodsZoneRes> pageQuery(GoodsZonePageReq queryReq);

    /**
     * 查询所有启用的分组
     * @return 分组列表
     */
    List<GoodsZoneRes> listAllEnabled();

    /**
     * 删除商品分组
     * @param id 主键ID
     * @return 是否成功
     */
    boolean deleteById(Long id);
}