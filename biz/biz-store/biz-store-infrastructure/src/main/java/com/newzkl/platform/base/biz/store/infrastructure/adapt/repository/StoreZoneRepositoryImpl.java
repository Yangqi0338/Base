package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreZone;
import com.newzkl.platform.base.biz.store.model.store.query.StoreZoneQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreZoneRes;
import com.newzkl.platform.base.biz.store.domain.store.repository.StoreZoneRepository;
import com.newzkl.platform.base.biz.store.infrastructure.dao.StoreZoneDAO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreZoneDO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 门店专区仓储实现
 */
@Slf4j
@Repository
public class StoreZoneRepositoryImpl extends ServiceImpl<StoreZoneDAO, StoreZoneDO> implements StoreZoneRepository {

    @Override
    public Page<StoreZoneRes> storeZonePage(StoreZoneQuery req) {
        // 创建分页对象
        Page<StoreZoneDO> queryPage = new Page<>(req.getPageNo(), req.getPageSize());

        LambdaQueryWrapper<StoreZoneDO> wrapper = new LambdaQueryWrapper<StoreZoneDO>()
                .eq(req.getState() != null, StoreZoneDO::getState, req.getState())
                .orderByDesc(StoreZoneDO::getId);

        // 多字段模糊查询
        if (StrUtil.isNotBlank(req.getSearchContent())) {
            wrapper.and(w -> w
                    .eq(StoreZoneDO::getZoneCode, req.getSearchContent())
                    .or()
                    .like(StoreZoneDO::getZoneName, req.getSearchContent())
            );
        }

        // 执行分页查询
        Page<StoreZoneDO> page = this.page(queryPage, wrapper);

        // 转换为响应对象列表
        List<StoreZoneRes> responseList = page.getRecords().stream()
                .map(x -> BeanUtil.toBean(x, StoreZoneRes.class)).collect(Collectors.toList());

        // 构建新的 Page 对象
        Page<StoreZoneRes> resultPage = new Page<>();
        resultPage.setRecords(responseList);
        resultPage.setTotal(page.getTotal());
        resultPage.setSize(page.getSize());
        resultPage.setCurrent(page.getCurrent());
        resultPage.setPages(page.getPages());

        return resultPage;
    }

    @Override
    public void create(StoreZone storeZone) {
        this.save(TransferUtils.transfer(storeZone, StoreZoneDO::new));
    }

    @Override
    public void update(StoreZone storeZone) {
        this.updateById(TransferUtils.transfer(storeZone, StoreZoneDO::new));
    }

    /**
     * 新增商品个数
     */
    @Override
    public void increaseGoodsNum(String storeZoneCode, Integer increaseNum) {
        boolean updated = this.lambdaUpdate()
                .eq(StoreZoneDO::getZoneCode, storeZoneCode)
                .setSql("goods_num = goods_num + " + increaseNum)
                .update();
        if (!updated) {
            log.warn("门店专区商品数量更新失败，storeZoneCode: {}, increaseNum: {}", storeZoneCode, increaseNum);
        }
    }

    /**
     * 新增订单个数
     */
    @Override
    public void increaseOrderNum(String storeZoneCode, Integer increaseNum) {
        boolean updated = this.lambdaUpdate()
                .eq(StoreZoneDO::getZoneCode, storeZoneCode)
                .setSql("order_num = order_num + " + increaseNum)
                .update();
        if (!updated) {
            log.warn("门店专区订单数量更新失败，storeZoneCode: {}, increaseNum: {}", storeZoneCode, increaseNum);
        }
    }
}