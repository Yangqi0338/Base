package com.newzkl.platform.base.biz.goods.application.goods.ext.impl;

import com.newzkl.platform.base.biz.goods.application.goods.service.goods.GoodsQueryService;
import com.newzkl.platform.base.biz.goods.application.goods.ext.SpuQueryExt;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.application.spi.IdentityImpl;
import lombok.RequiredArgsConstructor;

/**
 * 商品详情查询默认实现
 *
 * <p>命中平台管理员 (PLATFORM) 与平台员工 (EMP) 身份; 条件非空 (非 catch-all), 故供应商/渠道商/运营商等
 * 在未装配对应 plugin 时无命中而被拒绝 (H3 per-point 无兜底)。CompanyRole 各 biz 副本已上收统一至 common/ddd-model。</p>
 *
 * @author KC
 */
@IdentityImpl({1L, 2L})
@RequiredArgsConstructor
public class DefaultSpuQueryExt implements SpuQueryExt {

    static {
        assert RoleEnum.CompanyRole.PLATFORM.getCode() == 1L
                && RoleEnum.CompanyRole.EMP.getCode() == 2L
                : "CompanyRole PLATFORM/EMP code 与注解条件不一致";
    }

    private final GoodsQueryService goodsQueryService;

    @Override
    public SpuVO spu(Long id, Boolean needExtraInfo) {
        return goodsQueryService.spuVO(id, needExtraInfo);
    }
}
