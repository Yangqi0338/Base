package com.newzkl.platform.base.biz.goods.application.goods.ext;

import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.application.spi.IdentityExtension;

/**
 * 商品详情查询扩展点。
 *
 * <p>按调用方身份分发: 不同身份 (平台/供应商/渠道商/运营商) 可看到不同数据 scope。平台/平台员工走 biz 默认实现,
 * 其余身份由对应 plugin 实现; 未装 plugin 的身份将被拒绝。</p>
 *
 * @author KC
 */
@IdentityExtension
public interface SpuQueryExt {

    /**
     * 查询商品详情。
     *
     * @param id            商品 ID
     * @param needExtraInfo 是否附加信息
     * @return 商品视图
     */
    SpuVO spu(Long id, Boolean needExtraInfo);
}
