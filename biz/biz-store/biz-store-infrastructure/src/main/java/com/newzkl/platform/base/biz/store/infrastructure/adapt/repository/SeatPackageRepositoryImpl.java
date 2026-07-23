package com.newzkl.platform.base.biz.store.infrastructure.adapt.repository;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.store.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.store.model.store.entity.SeatPackage;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackagePageReq;
import com.newzkl.platform.base.biz.store.model.store.res.SeatPackageResponse;
import com.newzkl.platform.base.biz.store.domain.store.repository.SeatPackageRepository;
import com.newzkl.platform.base.biz.store.infrastructure.dao.SeatPackageDAO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.SeatPackageDO;
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
    public SeatPackageResponse detail(Long id) {
        SeatPackageDO entity = this.getById(id);
        return TransferUtils.transfer(entity, SeatPackageResponse::new);
    }

    @Override
    public Page<SeatPackageResponse> seatPackagePage(SeatPackagePageReq req) {
        // 创建分页对象
        Page<SeatPackageDO> queryPage = new Page<>(req.getPageNo(), req.getPageSize());

        LambdaQueryWrapper<SeatPackageDO> wrapper = new LambdaQueryWrapper<SeatPackageDO>()
                .eq(req.getState() != null, SeatPackageDO::getState, req.getState())
                .orderByAsc(!RoleEnum.CompanyRole.PLATFORM.getCode().equals(req.getRoleId()), SeatPackageDO::getSeatNum);

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
        List<SeatPackageResponse> responseList = page.getRecords().stream()
                .map(x -> BeanUtil.toBean(x, SeatPackageResponse.class)).collect(Collectors.toList());

        // 构建新的 Page 对象
        Page<SeatPackageResponse> resultPage = new Page<>();
        resultPage.setRecords(responseList);
        resultPage.setTotal(page.getTotal());
        resultPage.setSize(page.getSize());
        resultPage.setCurrent(page.getCurrent());
        resultPage.setPages(page.getPages());

        return resultPage;
    }

    @Override
    public List<SeatPackageResponse> seatPackageList(SeatPackagePageReq req) {
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

        // 执行分页查询
        List<SeatPackageDO> list = this.list(wrapper);

        // 转换为响应对象列表
        return list.stream()
                .map(x -> BeanUtil.toBean(x, SeatPackageResponse.class)).collect(Collectors.toList());
    }


    /**
     * 新增席位个数
     */
    @Override
    public void increaseSeatNum(String seatPackageCode, Integer increaseNum) {
        boolean updated = this.lambdaUpdate()
                .eq(SeatPackageDO::getSeatPackageCode, seatPackageCode)
                .setSql("seat_num = seat_num + " + increaseNum)
                .update();

        if (!updated) {
            log.warn("席位套餐席位个数更新失败，storeZoneCode: {}, increaseNum: {}", seatPackageCode, increaseNum);
        }
    }
}