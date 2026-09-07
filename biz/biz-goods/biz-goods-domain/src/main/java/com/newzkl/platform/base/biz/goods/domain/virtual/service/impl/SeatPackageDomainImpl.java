package com.newzkl.platform.base.biz.goods.domain.virtual.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.biz.goods.model.goods.entity.virtual.SeatPackage;
import com.newzkl.platform.base.biz.goods.model.goods.req.virtual.SeatPackageCreateReq;
import com.newzkl.platform.base.biz.goods.model.goods.query.virtual.SeatPackageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.virtual.SeatPackageUpdateReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.virtual.SeatPackageRes;
import com.newzkl.platform.base.biz.goods.domain.virtual.repository.SeatPackageRepository;
import com.newzkl.platform.base.biz.goods.domain.virtual.service.SeatPackageDomain;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 席位套餐
 */
@Service
@RequiredArgsConstructor
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
}
