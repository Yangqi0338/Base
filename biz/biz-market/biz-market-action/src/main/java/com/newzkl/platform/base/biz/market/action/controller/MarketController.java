package com.newzkl.platform.base.biz.market.action.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.market.service.MarketDomain;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketCategoryPageQuery;
import com.newzkl.platform.base.biz.market.model.dto.market.MarketDTO;
import com.newzkl.platform.base.common.ddd.model.enums.market.MarketEnum;
import com.newzkl.platform.base.biz.market.model.query.market.AppBindMarketGoodsPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.ChannelMarketPageQuery;
import com.newzkl.platform.base.biz.market.model.query.market.MarketPageQuery;
import com.newzkl.platform.base.biz.market.model.req.market.BindMarketListReq;
import com.newzkl.platform.base.biz.market.model.req.market.ClientBindMarketReq;
import com.newzkl.platform.base.biz.market.model.req.market.MarketReq;
import com.newzkl.platform.base.biz.market.model.req.market.MarketUserReq;
import com.newzkl.platform.base.biz.market.model.req.market.UpdateMarketDataReq;
import com.newzkl.platform.base.biz.market.model.vo.market.AppBindMarketGoodsVO;
import com.newzkl.platform.base.biz.market.model.vo.market.AppBindMarketVO;
import com.newzkl.platform.base.biz.market.model.vo.market.BindMarketVO;
import com.newzkl.platform.base.biz.market.model.vo.market.MarketCategoryVO;
import com.newzkl.platform.base.biz.market.model.vo.market.MarketGoodsCategoryVO;
import com.newzkl.platform.base.biz.market.model.vo.market.MarketUserVO;
import com.newzkl.platform.base.biz.market.model.vo.market.MarketVO;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Consumer;

/**
 * 市场服务控制器
 *
 * <p>迁移自 {@code com.zkl.scm.market.interfaces.market.MarketController},
 * 类级与方法级路径、HTTP verb 逐字沿用。</p>
 *
 * <p>未迁端点: {@code POST /queryMarketUnBindUser} 返回 user 域
 * {@code ChannelOutVO}, 须经 account 域 Dubbo Facade 查询未绑定渠道商,
 * Base 侧 market 域无对应出站端口, 补该端口须为 biz-market-infrastructure
 * 增加 {@code biz-account-facade} 依赖 (pom 改动), 故记阻塞不补。</p>
 *
 * <p>旧 {@code saveMarketCategory} / {@code querySelectorGoodsCategory} 两个端点不迁:
 * 二者在 new-scm 侧均无 {@code @Deprecated} 标记, 判死依据是无存活前端调用方
 * ({@code saveMarketCategory} 仅被 platform-admin 的 {@code marketCategoryList.vue} 引用,
 * 而该文件 import 的 {@code @/api/market} 模块不存在且未挂路由, 属遗留孤儿页)。
 * {@code MarketDomain} 侧对等能力 ({@code saveMarketCategory} /
 * {@code queryMarketGoodsCategory(null, userId)}) 已具备, 后续如确认仍需对外暴露可直接补。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/market")
@RequiredArgsConstructor
@Slf4j
public class MarketController {

    private final MarketDomain marketDomain;

    /**
     * APP渠道商获取绑定市场列表
     *
     * @param query 查询条件
     * @return 绑定市场列表
     */
    @GetMapping("/queryChannelBindMarket")
    public PlatformResult<Page<AppBindMarketVO>> queryChannelBindMarket(@ModelAttribute ChannelMarketPageQuery query) {
        if (query.getClientId() == null) {
            query.setClientId(SecurityUtils.getAccountId());
        }
        fillBindTypeByRole(query.getBindType(), query::setBindType);
        return PlatformResult.success(marketDomain.queryChannelBindMarket(query));
    }

    /**
     * APP渠道商获取绑定市场列表对应的商品
     *
     * @param query 查询条件
     * @return 绑定市场商品列表
     */
    @GetMapping("/queryChannelBindMarketGoods")
    public PlatformResult<Page<AppBindMarketGoodsVO>> queryChannelBindMarketGoods(@ModelAttribute AppBindMarketGoodsPageQuery query) {
        if (query.getUserId() == null) {
            query.setUserId(SecurityUtils.getAccountId());
        }
        fillBindTypeByRole(query.getBindType(), query::setBindType);
        return PlatformResult.success(marketDomain.queryChannelBindMarketGoods(query));
    }

    /**
     * 查询市场列表
     *
     * @param req 查询条件
     * @return 市场列表
     */
    @PostMapping("/queryMarketList")
    public PlatformResult<Page<MarketVO>> queryMarketList(@RequestBody MarketPageQuery req) {
        if (req.getClientId() == null) {
            req.setClientId(SecurityUtils.getAccountId());
        }
        return PlatformResult.success(marketDomain.queryMarketList(req));
    }

    /**
     * 查询市场列表 (二级市场, 按当前登录角色定位下级绑定关系)
     *
     * @param req 查询条件
     * @return 市场列表
     */
    @PostMapping("/queryMarketList/v2")
    public PlatformResult<Page<MarketVO>> queryMarketListV2(@RequestBody MarketPageQuery req) {
        req.setMarketLevel(2);
        req.setSubBindType(MarketEnum.User.accountTypeByRoleId(SecurityUtils.getRoleId()));
        if (req.getSubBindUser() == null) {
            req.setSubBindUser(SecurityUtils.getAccountId());
        }
        return PlatformResult.success(marketDomain.queryMarketList(req));
    }

    /**
     * 查询市场
     *
     * @param marketId 市场ID
     * @return 市场详情
     */
    @PostMapping("/queryMarket/{marketId}")
    public PlatformResult<MarketVO> queryMarket(@PathVariable Long marketId) {
        return PlatformResult.success(marketDomain.queryMarket(marketId));
    }

    /**
     * 保存一级市场
     *
     * @param req 市场请求
     * @return 操作结果
     */
    @PostMapping("/saveOneLevelMarket")
    public PlatformResult<Object> saveOneLevelMarket(@RequestBody MarketReq req) {
        MarketDTO marketDTO = buildMarketDTO(req);
        marketDTO.setMarketLevel(MarketEnum.Level.ONE.getLevel());
        marketDomain.saveMarket(marketDTO);
        return PlatformResult.success();
    }

    /**
     * 保存二级市场
     *
     * <p>行为差异: 旧 application 层保存后另调 user 域
     * {@code OperatorFacade#editTwoMarketCount} 累加运营商二级市场计数;
     * Base 侧 market 域无该出站端口, 计数未接线。</p>
     *
     * @param req 市场请求
     * @return 操作结果
     */
    @PostMapping("/saveTwoLevelMarket")
    public PlatformResult<Object> saveTwoLevelMarket(@RequestBody MarketReq req) {
        MarketDTO marketDTO = buildMarketDTO(req);
        marketDTO.setMarketLevel(MarketEnum.Level.TWO.getLevel());
        marketDTO.setCategoryId(req.getCategoryId());
        marketDomain.saveMarket(marketDTO);
        return PlatformResult.success();
    }

    /**
     * 运营商绑定一级市场
     *
     * <p>行为差异: 旧 application 层绑定后另调 user 域
     * {@code OperatorFacade#editOneMarketCount} 累加运营商一级市场计数;
     * Base 侧 market 域无该出站端口, 计数未接线。</p>
     *
     * @param req 绑定请求
     * @return 操作结果
     */
    @PostMapping("/operatorBindOneMarket")
    public PlatformResult<Object> operatorBindOneMarket(@RequestBody ClientBindMarketReq req) {
        req.setBindType(MarketEnum.User.OPERATOR.getType());
        marketDomain.bindMarket(req);
        marketDomain.alterMarketData(UpdateMarketDataReq.buildUpdateMarketDataReq(req.getMarketId(), MarketEnum.NumType.SUB_BIND_NUM, 1));
        return PlatformResult.success();
    }

    /**
     * 交易师绑定二级市场
     *
     * <p>行为差异: 旧 application 层绑定后另调 user 域
     * {@code DealerFacade#editTwoMarketCount} 累加交易师二级市场计数;
     * Base 侧 market 域无该出站端口, 计数未接线。</p>
     *
     * @param req 绑定请求
     * @return 操作结果
     */
    @PostMapping("/tradersBindTwoMarket")
    public PlatformResult<Object> tradersBindTwoMarket(@RequestBody ClientBindMarketReq req) {
        req.setBindType(MarketEnum.User.TRADERS.getType());
        marketDomain.bindMarket(req);
        return PlatformResult.success();
    }

    /**
     * 移动APP渠道商绑定交易市场
     *
     * @param req 绑定请求
     * @return 绑定ID
     */
    @PostMapping("/appChannelBindMarket")
    public PlatformResult<Object> appChannelBindMarket(@RequestBody ClientBindMarketReq req) {
        return PlatformResult.success(marketDomain.appChannelBindMarket(req));
    }

    /**
     * 交易师给渠道商绑定二级市场
     *
     * @param req 绑定请求
     * @return 绑定ID
     */
    @PostMapping("/channelBindTradersMarket")
    public PlatformResult<Object> channelBindTradersMarket(@RequestBody ClientBindMarketReq req) {
        return PlatformResult.success(marketDomain.channelBindTradersMarket(req));
    }

    /**
     * 解除绑定
     *
     * @param id 绑定ID
     * @return 操作结果
     */
    @PostMapping("/deBindMarket/{id}")
    public PlatformResult<Object> deBindMarket(@PathVariable Long id) {
        marketDomain.deBindMarket(id);
        return PlatformResult.success();
    }

    /**
     * 查询用户绑定市场列表
     *
     * @param req 查询条件
     * @return 绑定市场列表
     */
    @PostMapping("/queryBindMarket")
    public PlatformResult<List<BindMarketVO>> queryBindMarket(@RequestBody BindMarketListReq req) {
        if (req.getClientId() == null) {
            req.setClientId(SecurityUtils.getAccountId());
        }
        return PlatformResult.success(marketDomain.queryBindMarket(req));
    }

    /**
     * 查询市场绑定用户列表
     *
     * @param req 查询条件
     * @return 市场用户列表
     */
    @PostMapping("/queryMarketUser")
    public PlatformResult<List<MarketUserVO>> queryMarketUser(@RequestBody MarketUserReq req) {
        return PlatformResult.success(marketDomain.queryMarketUser(req));
    }

    /**
     * 查询市场分类列表
     *
     * @param req 查询条件
     * @return 市场分类列表
     */
    @PostMapping("/queryMarketCategoryList")
    public PlatformResult<Page<MarketCategoryVO>> queryMarketCategoryList(@RequestBody MarketCategoryPageQuery req) {
        return PlatformResult.success(marketDomain.queryMarketCategoryPage(req));
    }

    /**
     * 删除分类
     *
     * @param id 分类ID
     * @return 操作结果
     */
    @PostMapping("/deleteMarketCategory/{id}")
    public PlatformResult<Object> deleteMarketCategory(@PathVariable Long id) {
        marketDomain.deleteMarketCategory(id);
        return PlatformResult.success();
    }

    /**
     * 查询市场商品分类
     *
     * @param marketId 市场ID
     * @return 市场商品分类列表
     */
    @PostMapping("/queryMarketGoodsCategory/{marketId}")
    public PlatformResult<List<MarketGoodsCategoryVO>> queryMarketGoodsCategory(@PathVariable Long marketId) {
        return PlatformResult.success(marketDomain.queryMarketGoodsCategory(marketId, null));
    }

    /**
     * 未显式传入绑定类型时按当前登录角色补默认值
     *
     * <p>沿用旧 {@code queryChannelBindMarket} / {@code queryChannelBindMarketGoods} 判定:
     * 运营商端角色 (运营商 / 交易师 / 甄选师 / 运营商游客) 取 1, 渠道商取 3, 其余留空。
     * 旧实现用 {@code RoleEnum.CompanyRole.findClientRoleIdList(Client.OPERATOR).contains(roleId)},
     * Base 侧该辅助方法下沉到 account 域, 此处按等价语义改判角色所属端。</p>
     *
     * @param currentBindType 请求已带的绑定类型, 非空则不覆盖
     * @param bindTypeSetter  绑定类型写入器
     */
    private void fillBindTypeByRole(Integer currentBindType, Consumer<Integer> bindTypeSetter) {
        if (currentBindType != null) {
            return;
        }
        Long roleId = SecurityUtils.getRoleId();
        RoleEnum.CompanyRole role = RoleEnum.CompanyRole.getByCode(roleId);
        if (role != null && CommonEnum.Client.OPERATOR == role.getClient()) {
            bindTypeSetter.accept(MarketEnum.User.OPERATOR.getType());
            return;
        }
        if (ObjectUtil.equals(RoleEnum.CompanyRole.CHANNEL.getCode(), roleId)) {
            bindTypeSetter.accept(MarketEnum.User.CHANNEL.getType());
        }
    }

    /**
     * 由市场请求构建市场 DTO
     *
     * @param req 市场请求
     * @return 市场 DTO
     */
    private MarketDTO buildMarketDTO(MarketReq req) {
        MarketDTO market = new MarketDTO();
        market.setId(req.getId());
        market.setMarketName(req.getMarketName());
        market.setMarketDesc(req.getMarketDesc());
        market.setMarketLogo(req.getMarketLogo());
        market.setMarketType(req.getMarketType());
        return market;
    }
}
