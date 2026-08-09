package com.newzkl.platform.base.biz.store.domain.store.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.biz.store.model.store.entity.SeatPackage;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackageCreateReq;
import com.newzkl.platform.base.biz.store.model.store.query.SeatPackageQuery;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackageUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.SeatPackageRes;
import com.newzkl.platform.base.biz.store.domain.store.repository.SeatPackageRepository;
import com.newzkl.platform.base.biz.store.domain.store.service.SeatPackageDomain;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.springframework.stereotype.Service;


/**
 * 席位套餐
 */
@Service
@lombok.RequiredArgsConstructor
public class SeatPackageDomainImpl implements SeatPackageDomain {

    private final SeatPackageRepository seatPackageRepository;

    @Override
    public void create(SeatPackageCreateReq req) {
        SeatPackage seatPackage = TransferUtils.transfer(req, SeatPackage::new);
        seatPackage.setCreateId(SecurityUtils.getAccountId());
        seatPackage.setCreateName(SecurityUtils.getNickName());
        seatPackage.setSeatPackageCode(BusinessCodeUtil.generate(BusinessType.SEAT_PACKAGE));
        seatPackageRepository.create(seatPackage);
    }

    @Override
    public void update(SeatPackageUpdateReq req) {
        SeatPackage seatPackage = TransferUtils.transfer(req, SeatPackage::new);
        seatPackageRepository.update(seatPackage);
    }

    @Override
    public Page<SeatPackageRes> seatPackagePage(SeatPackageQuery req) {
        return seatPackageRepository.seatPackagePage(req);
    }

    @Override
    public SeatPackageRes detail(Long id) {
        return seatPackageRepository.detail(id);
    }

    /**
     * 新增席位个数
     */
    @Override
    public void increaseSeatNum(String seatPackageCode, Integer increaseNum) {
        seatPackageRepository.increaseSeatNum(seatPackageCode, increaseNum);
    }
}
