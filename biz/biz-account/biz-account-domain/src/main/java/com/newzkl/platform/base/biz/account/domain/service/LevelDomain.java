package com.newzkl.platform.base.biz.account.domain.service;

import com.newzkl.platform.base.biz.account.domain.adapt.api.PackUpCheckCommand;
import com.newzkl.platform.base.biz.account.model.level.req.LevelQuery;
import com.newzkl.platform.base.biz.account.model.level.req.LevelReq;
import com.newzkl.platform.base.biz.account.model.level.res.LevelRes;
import com.newzkl.platform.base.common.ddd.facade.PackGoodsInfo;

import java.util.List;

/**
 * 等级领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.level.service.ILevelDomain}。
 * 旧接口的 {@code executeLevel} (等级升级计算引擎) 依赖大量未迁的条件值对象与团队/业绩统计入参,
 * 其调用方在分润/升级链, 本切片未迁移, 登记为遗留。</p>
 *
 * @author KC
 */
public interface LevelDomain {

    /**
     * 等级保存
     *
     * <p>等级值恒为 1; 升级条件与直推礼包配置为必填, 校验不过抛业务异常;
     * 入会礼包经出站端口写入后回填礼包商品 ID。</p>
     *
     * @param req 等级入参
     * @return 主键 ID
     */
    Long save(LevelReq req);

    /**
     * 等级更新
     *
     * @param req 等级入参 (id 必填)
     * @return 主键 ID
     */
    Long update(LevelReq req);

    /**
     * 等级分页列表
     *
     * @param query 查询条件
     * @return 等级出参列表, 无数据返回空集合
     */
    List<LevelRes> pageList(LevelQuery query);

    /**
     * 等级列表 (不分页)
     *
     * @param query 查询条件
     * @return 等级出参列表, 无数据返回空集合
     */
    List<LevelRes> list(LevelQuery query);

    /**
     * 按查询条件取单个等级
     *
     * @param query 查询条件
     * @return 等级出参, 无则 null
     */
    LevelRes findByQuery(LevelQuery query);

    /**
     * 查询指定角色的一级入会礼包
     *
     * @param roleId 角色 ID
     * @return 礼包商品信息, 端口未接线时返回 null
     */
    PackGoodsInfo levelPack(Integer roleId);

	Integer packUpCheck(PackUpCheckCommand checkCommand);
}
