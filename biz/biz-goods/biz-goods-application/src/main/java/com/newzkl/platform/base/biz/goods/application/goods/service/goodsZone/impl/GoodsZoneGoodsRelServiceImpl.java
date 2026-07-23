package com.newzkl.platform.base.biz.goods.application.goods.service.goodsZone.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.application.goods.service.goodsZone.IGoodsZoneGoodsRelService;
import com.newzkl.platform.base.biz.goods.domain.goodsZone.service.GoodsZoneGoodsRelDomainService;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelAddReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelDelReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelPageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneGoodsRelRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分组-商品关联应用服务实现
 * @author sijiwang
 * @since 2026-03-18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsZoneGoodsRelServiceImpl implements IGoodsZoneGoodsRelService {

    private final GoodsZoneGoodsRelDomainService relDomainService;

    @Override
    public boolean batchAdd(GoodsZoneGoodsRelAddReq addReq) {
        log.info("开始批量添加商品到分组：分组ID={}, 商品数量={}",
                addReq.getGroupId(), addReq.getSpuList() != null ? addReq.getSpuList().size() : 0);
        boolean result = relDomainService.batchAdd(addReq);
        log.info("批量添加商品到分组{}：分组ID={}", result ? "成功" : "失败", addReq.getGroupId());
        return result;
    }

    @Override
    public boolean batchDelete(GoodsZoneGoodsRelDelReq delReq) {
        log.info("开始批量删除分组下商品：分组ID={}, 商品数量={}", delReq.getGroupId(), delReq.getSpuList());
        boolean result = relDomainService.batchDelete(delReq.getGroupId(), delReq.getSpuList());
        log.info("批量删除分组下商品{}：分组ID={}", result ? "成功" : "失败", delReq.getGroupId());
        return result;
    }

    @Override
    public List<GoodsZoneGoodsRelRes> listByGroupId(Long groupId) {
        log.info("查询分组下关联商品：分组ID={}", groupId);
        return relDomainService.listByGroupId(groupId);
    }

    @Override
    public Page<GoodsZoneGoodsRelRes> pageQuery(GoodsZoneGoodsRelPageReq queryReq) {
        log.info("分页查询商品关联关系：分组ID={}, 商品ID={}, 页码={}, 页大小={}",
                queryReq.getGroupId(), queryReq.getSpuId(), queryReq.getCurrent(), queryReq.getSize());
        return relDomainService.pageQuery(queryReq);
    }

    @Override
    public boolean checkExists(Long groupId, Long spuId) {
        boolean exists = relDomainService.checkExists(groupId, spuId);
        log.info("检查商品是否关联到分组：分组ID={}, 商品ID={}, 结果={}", groupId, spuId, exists);
        return exists;
    }
}