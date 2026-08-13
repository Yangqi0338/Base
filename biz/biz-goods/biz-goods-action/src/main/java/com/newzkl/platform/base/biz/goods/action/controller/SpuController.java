package com.newzkl.platform.base.biz.goods.action.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.action.cmd.CommonCmd;
import com.newzkl.platform.base.biz.goods.action.cmd.SpuCmd;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuService;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SkuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.req.spu.OutSpuEditCommand;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.GoldVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.MarketSimpleSpuVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.facade.SelectorSpuVO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SkuQuery;
import com.newzkl.platform.base.common.ddd.facade.SpuQuery;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品-商品库控制器
 *
 * @author KC
 */
@RestController
@RequestMapping("/goods/spu")
@RequiredArgsConstructor
@Slf4j
public class SpuController {

    private final SpuDomain spuDomain;
    private final SpuService spuService;

    /**
     * 外部商品价格修改
     *
     * @param outSpuEditCommand 外部商品修改命令
     * @return 空结果
     */
    @PostMapping("spuSalePriceEdit")
    public PlatformResult<Long> outSpuEdit(@Validated @RequestBody OutSpuEditCommand outSpuEditCommand) {
        spuDomain.outSpuEdit(outSpuEditCommand);
        return PlatformResult.success();
    }

    /**
     * 创建商品
     *
     * <p>供应商必填供货价, 渠道商必填销售价; 其余角色不支持。</p>
     *
     * @param spuDTO 商品请求
     * @return 商品主键
     */
    @PostMapping("spuCreate")
    public PlatformResult<Long> spuCreate(@Validated @RequestBody SpuDTO spuDTO) {
        if (spuDTO.getId() != null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        Long roleId = SecurityUtils.getRoleId();
        if (RoleEnum.CompanyRole.SUPPLIER.getCode().equals(roleId)) {
            for (SkuDTO skuDTO : spuDTO.getSkuList()) {
                if (skuDTO.getSupplyPrice() == null || skuDTO.getSupplyPrice().isNull()) {
                    ThrowsException.exception(BaseErrorCode.PARAM, "缺少供货价");
                }
            }
            spuDTO.setChannelType(SpuEnum.ChannelType.SELECTION);
        } else if (RoleEnum.CompanyRole.CHANNEL.getCode().equals(roleId)) {
            for (SkuDTO skuDTO : spuDTO.getSkuList()) {
                if (skuDTO.getSalePrice() == null || skuDTO.getSalePrice().isNull()) {
                    ThrowsException.exception(BaseErrorCode.PARAM, "缺少销售价");
                }
            }
            spuDTO.setChannelType(SpuEnum.ChannelType.CUSTOM);
        } else {
            ThrowsException.exception(BaseErrorCode.NOT_SERVICE);
        }
        spuDTO.setRole(RoleEnum.CompanyRole.getByCode(roleId));
        spuDTO.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(spuDomain.spuCreate(spuDTO));
    }

    /**
     * 商品上下架
     *
     * @param upCommand 上下架命令
     * @return 空结果
     */
    @PostMapping("spuUp")
    public PlatformResult<Long> spuUp(@Validated @RequestBody SpuCmd.UpCommand upCommand) {
        spuDomain.spuUp(upCommand.getEnable(), upCommand.getSpuIdList());
        return PlatformResult.success();
    }

    /**
     * 修改商品
     *
     * <p>供应商不允许改动商品状态。</p>
     *
     * @param spuDTO 商品请求
     * @return 空结果
     */
    @PostMapping("spuUpdate")
    public PlatformResult<Void> spuUpdate(@RequestBody SpuDTO spuDTO) {
        if (spuDTO.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        Long roleId = SecurityUtils.getRoleId();
        if (RoleEnum.CompanyRole.SUPPLIER.getCode().equals(roleId)) {
            spuDTO.setState(null);
        } else if (!RoleEnum.CompanyRole.CHANNEL.getCode().equals(roleId)) {
            ThrowsException.exception(BaseErrorCode.NOT_SERVICE);
        }
        spuDomain.spuPreUpdate(spuDTO);
        return PlatformResult.success();
    }

    /**
     * 删除商品
     *
     * <p>仅供应商可删除, 平台不支持。</p>
     *
     * @param idList 主键列表命令
     * @return 空结果
     */
    @PostMapping("spuDelete")
    public PlatformResult<Void> spuDelete(@RequestBody CommonCmd.IdList idList) {
        Long roleId = SecurityUtils.getRoleId();
        if (RoleEnum.CompanyRole.SUPPLIER.getCode().equals(roleId)) {
            spuDomain.spuDelete(idList.getIdList());
        } else if (RoleEnum.CompanyRole.PLATFORM.getCode().equals(roleId)) {
            ThrowsException.exception(BaseErrorCode.NOT_SERVICE);
        }
        return PlatformResult.success();
    }

    /**
     * 商品详情
     *
     * <p>非供应商角色返回脱敏后的数据。</p>
     *
     * @param id            商品主键
     * @param needExtraInfo 是否需要额外信息
     * @return 商品详情
     */
    @GetMapping("spu")
    public PlatformResult<SpuVO> spu(@RequestParam("id") Long id,
                                    @RequestParam(value = "needExtraInfo", required = false, defaultValue = "false") Boolean needExtraInfo) {
        SpuQuery spuQuery = new SpuQuery();
        spuQuery.setId(id);
        SpuVO spu = spuDomain.voByQuery(spuQuery);
        if (!RoleEnum.CompanyRole.SUPPLIER.getCode().equals(SecurityUtils.getRoleId()) && spu != null) {
            spu.doDesensitized();
        }
        return PlatformResult.success(spu);
    }

    /**
     * 商品分页
     *
     * <p>供应商与渠道商仅可见自身商品。</p>
     *
     * @param spuQuery 商品查询条件
     * @return 商品分页
     */
    @PostMapping("spuPage")
    public PlatformResult<Page<SpuVO>> spuPageVOList(@RequestBody SpuQuery spuQuery) {
        Long roleId = SecurityUtils.getRoleId();
        if (RoleEnum.CompanyRole.SUPPLIER.getCode().equals(roleId)
                || RoleEnum.CompanyRole.CHANNEL.getCode().equals(roleId)) {
            spuQuery.setAccountId(SecurityUtils.getAccountId());
        }
        spuQuery.setRole(RoleEnum.CompanyRole.getByCode(roleId));
        return PlatformResult.success(spuDomain.querySpuPage(spuQuery));
    }

    /**
     * SKU 列表
     *
     * @param skuQuery SKU 查询条件
     * @return SKU 列表
     */
    @PostMapping("skuList")
    public PlatformResult<List<SkuVO>> skuPageVOList(@RequestBody SkuQuery skuQuery) {
        return PlatformResult.success(spuDomain.skuVOList(skuQuery));
    }

    /**
     * 甄选师查看供应商商品分页
     *
     * @param spuQuery 商品查询条件
     * @return 甄选师视角商品分页
     */
    @PostMapping("selectorSpuPageVO")
    public PlatformResult<Page<SelectorSpuVO>> selectorSpuPageVO(@RequestBody SpuQuery spuQuery) {
        if (spuQuery.getAccountId() == null) {
            return PlatformResult.fail();
        }
        if (SpuEnum.State.STORE == spuQuery.getState() && AuditEnum.State.CUSTOM == spuQuery.getAuditState()) {
            spuQuery.setAuditStateList(Arrays.asList(AuditEnum.State.CUSTOM, AuditEnum.State.STOP));
        }
        Page<SpuVO> spuPage = spuDomain.querySpuPage(spuQuery);
        Page<SelectorSpuVO> resultPage = new Page<>();
        resultPage.setCurrent(spuPage.getCurrent());
        resultPage.setSize(spuPage.getSize());
        resultPage.setTotal(spuPage.getTotal());
        resultPage.setPages(spuPage.getPages());
        resultPage.setRecords(spuPage.getRecords().stream()
                .map(it -> BeanUtil.copyProperties(it, SelectorSpuVO.class))
                .collect(Collectors.toList()));
        return PlatformResult.success(resultPage);
    }

    /**
     * 获取黄金实时价格
     *
     * @return 黄金实时价格
     */
    @GetMapping("/getGoldRealTimePrice")
    public PlatformResult<GoldVO> getGoldRealTimePrice() {
        GoldVO goldVO = new GoldVO();
        goldVO.setRealTimePrice(RedisUtil.get(RedisEnum.Key.GOLD_REAL_TIME_PRICE.getCode()));
        goldVO.setUpdateTime(RedisUtil.get(RedisEnum.Key.GOLD_UPDATE_TIME.getCode()));
        return PlatformResult.success(goldVO);
    }

    /**
     * 货盘选品
     *
     * <p>仅平台角色可用, 落库为外部供应链商品草稿。</p>
     *
     * @param spuVO 商品视图对象
     * @return 商品主键
     */
    @PostMapping("/palletSelectGoods")
    public PlatformResult<Long> palletSelectGoods(@RequestBody SpuVO spuVO) {
        if (!RoleEnum.CompanyRole.PLATFORM.getCode().equals(SecurityUtils.getRoleId())) {
            ThrowsException.exception(BaseErrorCode.NOT_SERVICE);
        }
        spuVO.setChannelType(SpuEnum.ChannelType.OUT);
        return PlatformResult.success(spuService.palletSelectGoods(spuVO));
    }

    /**
     * 选品市场查询全量商品
     *
     * <p>固定只查在售商品。</p>
     *
     * @param spuQuery 商品查询条件
     * @return 商品分页
     */
    @PostMapping("marketQueryAllSpu")
    public PlatformResult<Page<SpuVO>> marketQueryAllSpu(@RequestBody SpuQuery spuQuery) {
        spuQuery.setState(SpuEnum.State.SALE);
        return PlatformResult.success(marketSpuPage(spuQuery));
    }

    /**
     * 选品市场查询全量商品-简化
     *
     * <p>固定只查在售商品, 出参仅保留编码/名称/图片与铺货标识。</p>
     *
     * @param spuQuery 商品查询条件
     * @return 简化商品分页
     */
    @PostMapping("marketSearchAllSpu")
    public PlatformResult<Page<MarketSimpleSpuVO>> marketSearchAllSpu(@RequestBody SpuQuery spuQuery) {
        spuQuery.setState(SpuEnum.State.SALE);
        Page<SpuVO> spuPage = marketSpuPage(spuQuery);
        Page<MarketSimpleSpuVO> resultPage = new Page<>();
        resultPage.setCurrent(spuPage.getCurrent());
        resultPage.setSize(spuPage.getSize());
        resultPage.setTotal(spuPage.getTotal());
        resultPage.setPages(spuPage.getPages());
        resultPage.setRecords(spuPage.getRecords().stream()
                .map(it -> BeanUtil.copyProperties(it, MarketSimpleSpuVO.class))
                .collect(Collectors.toList()));
        return PlatformResult.success(resultPage);
    }

    /**
     * 供应商商品统计
     *
     * <p>仅供应商角色可用, 强制按自身账号过滤。</p>
     *
     * @param spuQuery 商品查询条件
     * @return 带统计字段的商品分页
     */
    @PostMapping("spuCount")
    public PlatformResult<Page<SpuVO>> spuCount(@RequestBody SpuQuery spuQuery) {
        Long roleId = SecurityUtils.getRoleId();
        if (RoleEnum.CompanyRole.SUPPLIER.getCode().equals(roleId)) {
            spuQuery.setAccountId(SecurityUtils.getAccountId());
        } else {
            ThrowsException.exception(BaseErrorCode.NOT_SERVICE);
        }
        return PlatformResult.success(spuDomain.querySpuPage(spuQuery));
    }

    /**
     * 选品市场商品分页查询
     *
     * <p>源实现会用 market 域铺货关系回填 {@code choose} 标识, Base 侧无对等端口,
     * 该字段保持默认 false。</p>
     *
     * @param spuQuery 商品查询条件
     * @return 商品分页
     */
    private Page<SpuVO> marketSpuPage(SpuQuery spuQuery) {
        log.warn("TODO[capability-gap]: 选品市场商品未回填已铺货标识 choose, 缺 market 域铺货商品查询端口");
        return spuDomain.querySpuPage(spuQuery);
    }

    /**
     * 查询供应商下所有商品 (供应商审核通过)
     *
     * <p>迁移自 new-scm {@code SpuController.querySupplierSpuPage}: 仅运营商角色可调, 否则抛
     * {@code NOT_SERVICE}; 编排走 {@code SpuService#operatorSpuPage} (跨域取运营商可见供应商后分页)。</p>
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}, 本仓按 {@code rules/Architecture.md}
     * 改为 MyBatis-Plus {@code Page} ({@code records}/{@code total}/{@code current}/{@code size})。</p>
     *
     * @param spuQuery 商品查询 (type 为必填运营类型)
     * @return 商品分页
     */
    // TODO[service-gap]: 以下源端点未迁, 依赖 Base 侧尚不存在的能力, 待补齐后按原契约恢复:
    // 1. supplierSpuStatistics (GET supplierSpuStatistics) → SpuService#supplierSpuStatistics 当前抛
    //    UnsupportedOperationException, 缺供应商主体查询端口
    // 2. spuPageVOListInMarket (POST spuPageVOListInMarket) → 依赖 biz-market 的 IMarketFacade 跨域调用, 无对等 port
    // 3. spuUpload (POST spuUpload)            → 依赖 SpuImportService/QiConfigProperties Excel 导入链路, 未迁

    // 已迁移端点:
    // - spuSubmit (POST /goods/spu/spuSubmit)   → 已迁至 plugin-audit SpuController, 由 SpuSubmitService 编排审批流
    // - palletSpuPage (POST /goods/spu/palletSpuPage) → 已迁至 plugin-hdh PalletController (会订货 batchGetProducts)
    // - palletSpu (POST /goods/spu/palletSpu)   → 已迁至 plugin-hdh PalletController (会订货 getProductById)
}
