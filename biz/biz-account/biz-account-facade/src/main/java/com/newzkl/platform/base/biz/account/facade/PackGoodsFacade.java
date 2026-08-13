package com.newzkl.platform.base.biz.account.facade;

import com.newzkl.platform.base.biz.account.facade.model.PackGoodsFacadeDTO;
import com.newzkl.platform.base.biz.account.facade.model.PackGoodsSaveDTO;

/**
 * 入会礼包商品对外契约 (inbound provider)
 *
 * <p>入会礼包按业务概念归属 biz-user (与 Level/ShipAddress 同域, slug 10 概念订正)。
 * 消费方 = biz-account 的 {@code PackGoodsApi} (出站端口), 由入口 starter 侧远程
 * consumer 接线覆盖其默认兜底。签名对齐 biz-account {@code PackGoodsApi.save/findByQuery}。</p>
 *
 * <p>本接口只使用自带 facade model 作出入参, 物理上不引用 biz-user-model
 * (biz-user-facade 的 pom 未声明该依赖)。</p>
 *
 * @author KC
 */
public interface PackGoodsFacade {

    /**
     * 保存入会礼包商品
     *
     * @param dto 礼包写入契约
     * @return 礼包商品 ID
     */
    Long save(PackGoodsSaveDTO dto);

    /**
     * 按 (类型, 等级) 查询入会礼包商品
     *
     * @param type  类型 (角色 ID)
     * @param level 礼包等级
     * @return 礼包商品信息, 无则 null
     */
    PackGoodsFacadeDTO findByTypeAndLevel(Integer type, Integer level);
}
