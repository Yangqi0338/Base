package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.biz.goods.model.goods.entity.virtual.SeatPackage;
import com.newzkl.platform.base.biz.goods.model.goods.query.virtual.SeatPackageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.res.virtual.SeatPackageRes;
import com.newzkl.platform.base.biz.goods.domain.virtual.repository.SeatPackageRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.dao.SeatPackageDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.entity.SeatPackageDO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 席位套餐仓储实现
 */
@Slf4j
@Repository
public class SeatPackageRepositoryImpl extends ServiceImpl<SeatPackageDAO, SeatPackageDO> implements SeatPackageRepository {

    @Override
    public void create(SeatPackage seatPackage) {
        SeatPackageDO seatPackageDO = TransferUtils.transfer(seatPackage, SeatPackageDO::new);
        this.save(seatPackageDO);
    }

    @Override
    public void update(SeatPackage seatPackage) {
        SeatPackageDO seatPackageDO = TransferUtils.transfer(seatPackage, SeatPackageDO::new);
        this.updateById(seatPackageDO);
    }

    @Override
    public SeatPackageRes detail(Long id) {
        SeatPackageDO entity = this.getById(id);
        return TransferUtils.transfer(entity, SeatPackageRes::new);
    }

    @Override
    public Page<SeatPackageRes> seatPackagePage(SeatPackageQuery req) {
        // 创建分页对象
        Page<SeatPackageDO> queryPage = new Page<>(req.getPageNo(), req.getPageSize());

        LambdaQueryWrapper<SeatPackageDO> wrapper = new LambdaQueryWrapper<SeatPackageDO>()
                .eq(req.getState() != null, SeatPackageDO::getState, req.getState())
                .orderByAsc(AccountEnum.Identity.PLATFORM != req.getIdentity(), SeatPackageDO::getSeatNum);

        // 多字段模糊查询
        if (StrUtil.isNotBlank(req.getSearchContent())) {
            wrapper.and(w -> w
                    .eq(SeatPackageDO::getSeatPackageCode, req.getSearchContent())
                    .or()
                    .like(SeatPackageDO::getSeatPackageName, req.getSearchContent())
            );
        }

        // 执行分页查询
        Page<SeatPackageDO> page = this.page(queryPage, wrapper);

        // 转换为响应对象列表
        List<SeatPackageRes> responseList = page.getRecords().stream()
                .map(x -> TransferUtils.transfer(x, SeatPackageRes::new)).collect(Collectors.toList());

        // 构建新的 Page 对象
        Page<SeatPackageRes> resultPage = new Page<>();
        resultPage.setRecords(responseList);
        resultPage.setTotal(page.getTotal());
        resultPage.setSize(page.getSize());
        resultPage.setCurrent(page.getCurrent());
        resultPage.setPages(page.getPages());

        return resultPage;
    }

    @Override
    public List<SeatPackageRes> seatPackageList(SeatPackageQuery req) {
        LambdaQueryWrapper<SeatPackageDO> wrapper = new LambdaQueryWrapper<SeatPackageDO>()
                .eq(req.getState() != null, SeatPackageDO::getState, req.getState())
                .orderByDesc(SeatPackageDO::getId);

        // 多字段模糊查询
        if (StrUtil.isNotBlank(req.getSearchContent())) {
            wrapper.and(w -> w
                    .eq(SeatPackageDO::getSeatPackageCode, req.getSearchContent())
                    .or()
                    .like(SeatPackageDO::getSeatPackageName, req.getSearchContent())
            );
        }

        // 执行列表查询
        List<SeatPackageDO> list = this.list(wrapper);

        // 转换为响应对象列表
        return list.stream()
                .map(x -> TransferUtils.transfer(x, SeatPackageRes::new)).collect(Collectors.toList());
    }
}
