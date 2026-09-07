package com.newzkl.platform.base.biz.goods.application.provider;

import com.newzkl.platform.base.biz.goods.domain.virtual.service.SeatPackageDomain;
import com.newzkl.platform.base.biz.goods.facade.SeatPackageFacade;
import com.newzkl.platform.base.biz.goods.facade.model.SeatPackageFacadeVO;
import com.newzkl.platform.base.biz.goods.model.goods.res.virtual.SeatPackageRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * {@code SeatPackageFacade} 的本域实现
 *
 * <p>对等旧 {@code com.zkl.scm.terminal.application.rpc.StoreFacadeImpl} 席位套餐查询部分 ——
 * 旧版是 Dubbo provider ({@code @DubboService}), Base 当前为单体 (全域同一 Spring 上下文),
 * 故以普通 {@code @Service} 暴露, 调用方直接注入接口。将来拆服务时只需在此类加 provider 注解,
 * 调用方零改动</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeatPackageFacadeProvider implements SeatPackageFacade {

    private final SeatPackageDomain seatPackageDomain;

    /**
     * 按 id 查席位套餐对外投影
     *
     * @param seatPackageId 席位套餐 id
     * @return 席位套餐投影; id 为空或套餐不存在时返回 null
     */
    @Override
    public SeatPackageFacadeVO seatPackageVO(Long seatPackageId) {
        if (seatPackageId == null) {
            return null;
        }
        SeatPackageRes detail = seatPackageDomain.detail(seatPackageId);
        if (detail == null) {
            return null;
        }
        // facade 契约 packagePrice 保持 Integer 分 (防腐, 跨域消费方未迁 Money); 内部 Money → getCent 降回分
        return TransferUtils.transfer(detail, SeatPackageFacadeVO::new,
                (c, v) -> v.setPackagePrice(c.getPackagePrice() == null ? null : (int) c.getPackagePrice().getCent()));
    }
}
