package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.SeatPackageApi;
import com.newzkl.platform.base.biz.finance.model.support.SeatPackageApiVO;
import com.newzkl.platform.base.biz.goods.facade.SeatPackageFacade;
import com.newzkl.platform.base.biz.goods.facade.model.SeatPackageFacadeVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * {@code SeatPackageApi} 的跨域实现
 *
 * <p>经 {@code SeatPackageFacade} 调 biz-goods 的席位套餐查询能力 (席位套餐属虚拟商品)。对等旧
 * {@code @DubboReference IStoreFacade#seatPackageVO}: Base 当前为单体, facade 实现
 * ({@code SeatPackageFacadeProvider}) 与本类同上下文, 直接按接口注入即可; 将来拆服务时改为
 * 远程 consumer, 本类与领域层零改动</p>
 *
 * @author KC
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SeatPackageApiImpl implements SeatPackageApi {

    private final SeatPackageFacade seatPackageFacade;

    /**
     * 按 id 查席位套餐
     *
     * @param seatPackageId 席位套餐 id
     * @return 席位套餐投影; 不存在时返回 null
     */
    @Override
    public SeatPackageApiVO seatPackageVO(Long seatPackageId) {
        SeatPackageFacadeVO facadeVO = seatPackageFacade.seatPackageVO(seatPackageId);
        if (facadeVO == null) {
            return null;
        }
        return TransferUtils.transfer(facadeVO, SeatPackageApiVO::new);
    }
}
