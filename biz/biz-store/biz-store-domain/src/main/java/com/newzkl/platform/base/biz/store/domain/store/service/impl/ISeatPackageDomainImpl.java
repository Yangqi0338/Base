package com.newzkl.platform.base.biz.store.domain.store.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkl.scm.auth.api.SecurityUtils;
import com.zkl.scm.module.enums.BusinessType;
import com.zkl.scm.module.util.BusinessCodeUtil;
import com.newzkl.platform.base.biz.store.model.store.entity.SeatPackage;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackageCreateReq;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackagePageReq;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackageUpdateReq;
import com.newzkl.platform.base.biz.store.model.store.res.SeatPackageResponse;
import com.newzkl.platform.base.biz.store.domain.store.repository.ISeatPackageRepository;
import com.newzkl.platform.base.biz.store.domain.store.service.ISeatPackageDomain;
import com.zkl.scm.web.utils.TransferUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 席位套餐
 */
@Service
public class ISeatPackageDomainImpl implements ISeatPackageDomain {

    @Resource
    private ISeatPackageRepository seatPackageRepository;

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
    public Page<SeatPackageResponse> seatPackagePage(SeatPackagePageReq req) {
        return seatPackageRepository.seatPackagePage(req);
    }

    /**
     * 新增席位个数
     */
    @Override
    public void increaseSeatNum(String seatPackageCode, Integer increaseNum) {
        seatPackageRepository.increaseSeatNum(seatPackageCode, increaseNum);
    }
}
