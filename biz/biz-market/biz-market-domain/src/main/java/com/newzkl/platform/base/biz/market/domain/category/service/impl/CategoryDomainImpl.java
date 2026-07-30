package com.newzkl.platform.base.biz.market.domain.category.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.market.domain.adapt.api.GoodsCategoryApi;
import com.newzkl.platform.base.biz.market.domain.adapt.api.PlatformCategoryInfo;
import com.newzkl.platform.base.biz.market.domain.category.repository.MerchantCategoryRepository;
import com.newzkl.platform.base.biz.market.domain.category.service.CategoryDomain;
import com.newzkl.platform.base.biz.market.model.biz.req.CategoryEditReq;
import com.newzkl.platform.base.biz.market.model.biz.req.CategoryReq;
import com.newzkl.platform.base.biz.market.model.biz.req.CategorySyncReq;
import com.newzkl.platform.base.biz.market.model.biz.req.query.CategoryQuery;
import com.newzkl.platform.base.biz.market.model.biz.vo.CategoryVO;
import com.newzkl.platform.base.biz.market.model.enums.MarketErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code CategoryDomain} 实现
 *
 * <p>迁移自 {@code com.zkl.scm.market.domain.category.service.impl.CategoryDomainImpl}。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class CategoryDomainImpl implements CategoryDomain {

    /**
     * 分类来源类型: 平台同步
     */
    private static final int TYPE_PLATFORM = 0;

    /**
     * 分类来源类型: 自营
     */
    private static final int TYPE_SELF = 1;

    /**
     * 顶层分类 pid
     */
    private static final long ROOT_PID = 0L;

    /**
     * 平台根分类ID前缀长度
     *
     * <p>迁移保留旧行为: 旧实现对入参 id 取前两位作为一级分类 id, 再按
     * {@code LEFT(id, 2)} 匹配整棵子树。</p>
     */
    private static final int ROOT_ID_PREFIX_LEN = 2;

    private final MerchantCategoryRepository merchantCategoryRepository;
    private final GoodsCategoryApi goodsCategoryApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long categorySave(CategoryReq req) {
        Long id = SnowflakeIdAble.getSnowflakeId();
        req.setId(id);
        req.setAccountId(SecurityUtils.getAccountId());
        if (req.getPid() == null) {
            req.setPid(ROOT_PID);
        }
        if (req.getType() == null) {
            req.setType(TYPE_SELF);
        }
        merchantCategoryRepository.categorySave(req);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void categoryEdit(CategoryEditReq req) {
        if (req.getId() == null || req.getCategory() == null) {
            throw new PlatformException(MarketErrorCode.PARAM_ERROR);
        }
        CategoryReq category = req.getCategory();
        category.setId(req.getId());
        merchantCategoryRepository.categoryEdit(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void categoryDelete(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return;
        }
        merchantCategoryRepository.categoryDelete(idList);
    }

    @Override
    public List<CategoryVO> categoryTree(CategoryQuery query) {
        return listToTree(merchantCategoryRepository.categoryList(query));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncCategory(CategorySyncReq req) {
        Long rootId = resolveRootId(req);
        Long accountId = SecurityUtils.getAccountId();
        // 幂等: 该账号已同步过此平台源分类则直接返回
        if (merchantCategoryRepository.existsBySource(rootId, accountId)) {
            return;
        }
        List<PlatformCategoryInfo> platformList = goodsCategoryApi.platformCategoryTree(rootId);
        if (CollUtil.isEmpty(platformList)) {
            return;
        }
        // 第一趟: 为每个平台节点分配新雪花 id, 建立 平台id -> 新id 映射
        Map<Long, Long> remap = new HashMap<>(platformList.size());
        for (PlatformCategoryInfo platform : platformList) {
            if (platform.getId() == null) {
                continue;
            }
            remap.put(platform.getId(), SnowflakeIdAble.getSnowflakeId());
        }
        // 第二趟: 按映射改写 pid, 平台源 id 落 sourceId
        List<CategoryReq> toSave = new ArrayList<>(platformList.size());
        for (PlatformCategoryInfo platform : platformList) {
            Long newId = remap.get(platform.getId());
            if (newId == null) {
                continue;
            }
            CategoryReq item = new CategoryReq();
            item.setId(newId);
            item.setPid(resolvePid(platform.getPid(), remap));
            item.setSourceId(platform.getId());
            item.setAccountId(accountId);
            item.setType(TYPE_PLATFORM);
            item.setName(platform.getName());
            item.setDesc(platform.getDesc());
            item.setImg(platform.getImg());
            toSave.add(item);
        }
        merchantCategoryRepository.batchSave(toSave);
    }

    /**
     * 解析平台根分类ID
     *
     * <p>迁移保留旧行为: 入参不足两位视为参数错误, 否则取前两位。</p>
     *
     * @param req 同步请求
     * @return 平台一级分类ID
     */
    private Long resolveRootId(CategorySyncReq req) {
        if (req == null || req.getId() == null) {
            throw new PlatformException(MarketErrorCode.PARAM_ERROR);
        }
        String idString = String.valueOf(req.getId());
        if (idString.length() < ROOT_ID_PREFIX_LEN) {
            throw new PlatformException(MarketErrorCode.PARAM_ERROR);
        }
        return Long.parseLong(idString.substring(0, ROOT_ID_PREFIX_LEN));
    }

    /**
     * 平台 pid 映射为本表 pid
     *
     * @param platformPid 平台父分类ID
     * @param remap       平台id -> 新id 映射
     * @return 本表父分类ID; 顶层或映射缺失时为 0
     */
    private Long resolvePid(Long platformPid, Map<Long, Long> remap) {
        if (platformPid == null || platformPid == ROOT_PID) {
            return ROOT_PID;
        }
        Long mapped = remap.get(platformPid);
        // 平台父节点不在本次同步范围内 (子树被截断) 时提升为顶层
        return mapped == null ? ROOT_PID : mapped;
    }

    /**
     * 扁平分类列表组装为树
     *
     * <p>迁移变更: 旧实现用 {@code BizUtil.listToTree} (要求 VO 实现 PlatformTreeNode 且
     * pid 为 null 时拆箱 NPE); 新实现内联按 {@code id}/{@code pid} 组装, VO 保持纯 POJO。</p>
     *
     * @param list 扁平分类列表
     * @return 树形分类列表, 恒非 null
     */
    private List<CategoryVO> listToTree(List<CategoryVO> list) {
        List<CategoryVO> roots = new ArrayList<>();
        if (CollUtil.isEmpty(list)) {
            return roots;
        }
        Map<Long, CategoryVO> idIndex = new LinkedHashMap<>(list.size());
        for (CategoryVO item : list) {
            item.setChildren(new ArrayList<>());
            if (item.getId() != null) {
                idIndex.put(item.getId(), item);
            }
        }
        for (CategoryVO item : list) {
            CategoryVO parent = item.getPid() == null ? null : idIndex.get(item.getPid());
            if (parent == null || parent == item) {
                roots.add(item);
            } else {
                parent.getChildren().add(item);
            }
        }
        return roots;
    }
}
