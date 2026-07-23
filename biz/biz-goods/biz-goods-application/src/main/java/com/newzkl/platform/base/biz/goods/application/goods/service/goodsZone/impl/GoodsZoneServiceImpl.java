package com.newzkl.platform.base.biz.goods.application.goods.service.goodsZone.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.application.goods.service.goodsZone.GoodsZoneService;
import com.newzkl.platform.base.biz.goods.domain.goodsZone.service.GoodsZoneDomainService;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneAddReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZonePageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分组应用服务实现
 * @author sijiwang
 * @since 2026-03-18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsZoneServiceImpl implements GoodsZoneService {

    private final GoodsZoneDomainService goodsZoneDomainService;

    @Override
    public GoodsZoneRes add(GoodsZoneAddReq addReq) {
        log.info("开始新增商品分组：{}", addReq.getGroupName());
        GoodsZoneRes result = goodsZoneDomainService.add(addReq);
        log.info("新增商品分组成功：ID={}, 名称={}", result.getId(), result.getGroupName());
        return result;
    }

    @Override
    public GoodsZoneRes edit(GoodsZoneAddReq addReq) {
        log.info("开始编辑商品分组：ID={}, 新名称={}", addReq.getId(), addReq.getGroupName());
        GoodsZoneRes result = goodsZoneDomainService.edit(addReq);
        log.info("编辑商品分组成功：ID={}, 名称={}", result.getId(), result.getGroupName());
        return result;
    }

    @Override
    public boolean enable(Long id, String operator) {
        log.info("开始启用商品分组：ID={}, 操作人={}", id, operator);
        boolean result = goodsZoneDomainService.enable(id, operator);
        log.info("启用商品分组{}：ID={}", result ? "成功" : "失败", id);
        return result;
    }

    @Override
    public boolean disable(Long id, String operator) {
        log.info("开始禁用商品分组：ID={}, 操作人={}", id, operator);
        boolean result = goodsZoneDomainService.disable(id, operator);
        log.info("禁用商品分组{}：ID={}", result ? "成功" : "失败", id);
        return result;
    }

    @Override
    public GoodsZoneRes getById(Long id) {
        log.info("查询商品分组详情：ID={}", id);
        return goodsZoneDomainService.getById(id);
    }

    @Override
    public Page<GoodsZoneRes> pageQuery(GoodsZonePageReq queryReq) {
        log.info("分页查询商品分组：名称模糊查询={}, 状态={}, 页码={}, 页大小={}",
                queryReq.getGroupName(), queryReq.getState(), queryReq.getCurrent(), queryReq.getSize());
        return goodsZoneDomainService.pageQuery(queryReq);
    }

    @Override
    public List<GoodsZoneRes> listAllEnabled() {
        log.info("查询所有启用的商品分组");
        return goodsZoneDomainService.listAllEnabled();
    }

    @Override
    public boolean deleteById(Long id) {
        log.info("开始删除商品分组：ID={}", id);
        boolean result = goodsZoneDomainService.deleteById(id);
        log.info("删除商品分组{}：ID={}", result ? "成功" : "失败", id);
        return result;
    }
}