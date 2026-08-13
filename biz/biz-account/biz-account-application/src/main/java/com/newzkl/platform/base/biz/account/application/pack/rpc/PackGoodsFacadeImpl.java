package com.newzkl.platform.base.biz.account.application.pack.rpc;

import com.newzkl.platform.base.biz.account.domain.service.PackGoodsDomain;
import com.newzkl.platform.base.biz.account.facade.PackGoodsFacade;
import com.newzkl.platform.base.biz.account.facade.model.PackGoodsFacadeDTO;
import com.newzkl.platform.base.biz.account.facade.model.PackGoodsSaveDTO;
import com.newzkl.platform.base.biz.account.model.pack.req.PackGoodsCommand;
import com.newzkl.platform.base.biz.account.model.pack.res.PackGoodsRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

/**
 * 入会礼包商品对外契约实现 (inbound provider)
 *
 * <p>{@link PackGoodsFacade} 的 provider 侧实现, 落编排层。将 facade 自带 model
 * 与域内 model 互转 (facade 物理禁引 biz-user-model, 转换只发生在本 impl)。
 * 消费方 = biz-account {@code PackGoodsApi}。</p>
 *
 * @author KC
 */
@DubboService
@Component
@RequiredArgsConstructor
public class PackGoodsFacadeImpl implements PackGoodsFacade {

    private final PackGoodsDomain packGoodsDomain;

    @Override
    public Long save(PackGoodsSaveDTO dto) {
        PackGoodsCommand command = TransferUtils.transfer(dto, PackGoodsCommand::new);
        return packGoodsDomain.packGoodsSave(command);
    }

    @Override
    public PackGoodsFacadeDTO findByTypeAndLevel(Integer type, Integer level) {
        PackGoodsRes res = packGoodsDomain.findByTypeAndLevel(type, level);
        if (res == null) {
            return null;
        }
        return TransferUtils.transfer(res, PackGoodsFacadeDTO::new);
    }
}
