package com.newzkl.platform.base.biz.goods.application.goods.service.spu.impl;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuCategoryService;
import com.newzkl.platform.base.biz.goods.domain.brand.repository.IndustryRepository;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.SpuCategoryRepository;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.SpuRepository;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.PalletCategoryPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuCategoryQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.IndustryReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.IndustryVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.common.core.utils.biz.BizUtil;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品分类应用服务实现
 *
 * <p>行业绑定走 {@code IndustryRepository} 端口 (行业侧存分类 ID 逗号串),
 * 分类查询走 {@code SpuCategoryRepository} 端口, 本层只做编排与串/列表互转。</p>
 *
 * <p><b>缺口清单 (TODO[capability-gap])</b></p>
 * <ul>
 *   <li>{@code bindBrand} —— <b>抛异常</b>。Base 侧 {@code CategoryVO}/{@code CategoryReq}/
 *       {@code CategoryLayerDO} 均无 {@code brandIdList} 字段, 库表亦无该列,
 *       补齐需模型加字段 + DB 迁移, 超出本次范围</li>
 *   <li>{@code palletCategoryList} —— <b>抛异常</b>。源实现走会订货外部接口
 *       {@code HuiDingHuoApiUtils#getCategoryList}, Base 无该外部链路</li>
 *   <li>{@code categoryList} 的 SUPPLIER 角色分支 —— 源需 user 域
 *       {@code supplierFacade} 取供应商所属行业列表再按行业过滤分类, Base 无对等 port;
 *       当前退化为不按行业过滤 (返回全量分类), 主流程可跑</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpuCategoryServiceImpl implements SpuCategoryService {

    /**
     * 缺口说明统一前缀
     */
    private static final String GAP = "TODO[capability-gap]: ";

    private final IndustryRepository industryRepository;
    private final SpuCategoryRepository spuCategoryRepository;
    private final SpuRepository spuRepository;

    /**
     * 按分类分组统计 SPU 数并回填 {@code spuNum}
     *
     * <p>源实现在 {@code GoodsQueryServiceImpl#categoryPage}/{@code appCategoryPage} 里做同样的
     * 二次查询富化: 分类本表无商品数列, 需按分类 ID 批量统计后逐条塞回。统计不到的分类留 null,
     * 不补 0 —— 「查不到」与「确实 0 个」语义不同。</p>
     *
     * @param list 待回填的分类列表
     * @return 同一个 list 实例(已回填)
     */
    private List<SpuCategoryVO> fillSpuNum(List<SpuCategoryVO> list) {
        if (CollUtil.isEmpty(list)) {
            return list;
        }
        List<Long> idList = list.stream().map(SpuCategoryVO::getId).filter(Objects::nonNull).toList();
        Map<Long, Integer> numMap = spuRepository.countSpuByCategory(idList).stream()
                .filter(item -> item.getId() != null)
                .collect(Collectors.toMap(SpuCategoryVO::getId, SpuCategoryVO::getSpuNum, (a, b) -> a));
        list.forEach(item -> item.setSpuNum(numMap.get(item.getId())));
        return list;
    }

    /**
     * 分类绑定/解绑行业
     *
     * <p>行业侧以逗号串保存已绑定的分类 ID 集合, 绑定即追加, 解绑即剔除。</p>
     *
     * @param industryId 行业主键
     * @param categoryId 分类主键
     * @param bind       true 绑定, false 解绑
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindIndustry(Long industryId, Long categoryId, boolean bind) {
        IndustryVO industry = industryRepository.industry(industryId);
        String newIdList = bind
                ? BizUtil.addIdString(industry.getCategoryIdList(), categoryId)
                : BizUtil.cutIdString(industry.getCategoryIdList(), categoryId);
        IndustryReq industryReq = TransferUtils.transfer(industry, IndustryReq::new);
        industryReq.setCategoryIdList(newIdList);
        industryRepository.industryEdit(industryReq);
    }

    /**
     * 查询若干行业下已绑定的分类主键集合
     *
     * @param industryIdList 行业主键列表
     * @return 分类主键列表 (各行业逗号串展开后合并)
     */
    @Override
    public List<Long> idListByIndustryId(List<Long> industryIdList) {
        List<Long> result = new ArrayList<>();
        List<String> categoryIdList = industryRepository.categoryIdListById(industryIdList);
        if (categoryIdList != null) {
            categoryIdList.forEach(item -> result.addAll(CommonUtil.strToLongList(item)));
        }
        return result;
    }

    /**
     * 分类绑定/解绑品牌
     *
     * @param categoryId 分类主键
     * @param brandId    品牌主键
     * @param isBind     true 绑定, false 解绑
     * @throws UnsupportedOperationException 模型与库表均缺 brandIdList 字段
     */
    @Override
    public void bindBrand(Long categoryId, Long brandId, boolean isBind) {
        throw new UnsupportedOperationException(GAP
                + "分类绑定品牌需 CategoryVO/CategoryReq/CategoryLayerDO 新增 brandIdList 字段并做 DB 迁移, 超出本次范围");
    }

    /**
     * 分类列表
     *
     * <p>供应商角色下源实现会按供应商所属行业过滤, 该跨域能力缺失, 当前不做行业过滤。</p>
     *
     * @param categoryQuery 分类查询条件
     * @return 分类列表
     */
    @Override
    public List<SpuCategoryVO> categoryList(SpuCategoryQuery categoryQuery) {
        Long roleId = SecurityUtils.getRoleId();
        if (RoleEnum.CompanyRole.SUPPLIER.getCode().equals(roleId)) {
            log.warn(GAP + "供应商分类列表未按所属行业过滤, 缺 user 域 supplierFacade 取行业列表能力");
        }
        return fillSpuNum(spuCategoryRepository.categoryList(categoryQuery));
    }

    /**
     * app 市场分类列表
     *
     * <p>等价于平台归属 (accountId = 0) 的 {@link SpuCategoryServiceImpl#categoryList}。</p>
     *
     * @param categoryQuery 分类查询条件
     * @return 分类列表
     */
    @Override
    public List<SpuCategoryVO> appCategoryList(SpuCategoryQuery categoryQuery) {
        categoryQuery.setAccountId(0L);
        return fillSpuNum(spuCategoryRepository.categoryList(categoryQuery));
    }

    /**
     * 货盘分类列表
     *
     * @param categoryQuery 货盘分类查询条件
     * @return 分类列表
     * @throws UnsupportedOperationException 缺会订货外部链路
     */
    @Override
    public List<SpuCategoryVO> palletCategoryList(PalletCategoryPageQuery categoryQuery) {
        throw new UnsupportedOperationException(GAP
                + "货盘分类源走会订货外部接口 HuiDingHuoApiUtils#getCategoryList, Base 无该外部链路");
    }
}
