package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.brand.repository.IndustryRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.IndustryDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.IndustryDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.IndustryPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.IndustryReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.IndustryVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 行业仓储实现
 *
 * <p>对齐旧 {@code scm-goods} 的 {@code IndustryDAO.xml} where 片段: {@code id} 等值,
 * {@code idList} in, {@code name} 模糊, {@code categoryId} 对 {@code category_id_list}
 * 逗号串做模糊匹配, {@code notIndustryId} 不等, {@code notIndustryIdList} not in。
 * 旧 xml 未随本仓迁移, 分页改由 MyBatis-Plus wrapper 表达, 不调用未绑定的
 * {@link IndustryDAO#queryPage} (无 mapper xml, 调用即 Invalid bound statement)。</p>
 *
 * <p>gap 方法:</p>
 * <ul>
 *   <li>{@link IndustryRepositoryImpl#buildPageQuery} — 旧实现依赖运营商域 {@code operatorFacade
 *   .operatorTypeThirdIdGroupCountQuery} 取已绑定行业 ID 再反填 notIndustryIdList,
 *   本模块无账号/运营商域出口, 跨域能力缺失</li>
 * </ul>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class IndustryRepositoryImpl implements IndustryRepository {

    private final IndustryDAO industryDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void industrySave(IndustryReq req) {
        IndustryDO industryDO = TransferUtils.transfer(req, IndustryDO::new);
        industryDAO.insert(industryDO);
        // 回填自增/雪花主键, 领域层 industrySave 依赖 req.getId() 作返回值
        req.setId(industryDO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void industryDelete(List<Long> idList) {
        industryDAO.deleteByIds(idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void industryEdit(IndustryReq req) {
        industryDAO.updateById(TransferUtils.transfer(req, IndustryDO::new));
    }

    @Override
    public IndustryVO industry(Long id) {
        return TransferUtils.transfer(industryDAO.selectById(id), IndustryVO::new);
    }

    @Override
    public List<String> categoryIdListById(List<Long> industryIdList) {
        if (CollUtil.isEmpty(industryIdList)) {
            // 无入参时不能落到无条件全表查询
            return new ArrayList<>();
        }
        List<IndustryDO> industryList = industryDAO.selectList(new BaseLambdaQueryWrapper<IndustryDO>()
                .notEmptyIn(IndustryDO::getId, industryIdList)
                .select(IndustryDO::getCategoryIdList));
        return industryList.stream()
                .map(IndustryDO::getCategoryIdList)
                .collect(Collectors.toList());
    }

    @Override
    public Page<IndustryVO> queryPage(IndustryPageQuery industryQuery) {
        Page<IndustryDO> page = industryDAO.selectPage(RepositorySupport.page(industryQuery),
                new BaseLambdaQueryWrapper<IndustryDO>()
                        .notNullEq(IndustryDO::getId, industryQuery.getId())
                        .notEmptyIn(IndustryDO::getId, industryQuery.getIdList())
                        .notEmptyLike(IndustryDO::getName, industryQuery.getName())
                        .notEmptyLike(IndustryDO::getCategoryIdList, industryQuery.getCategoryId())
                        .notNullNe(IndustryDO::getId, industryQuery.getNotIndustryId())
                        // 不走 notEmptyNotIn: 该 helper 单元素时退化为 eq, 语义与 not in 相反
                        .notIn(CollUtil.isNotEmpty(industryQuery.getNotIndustryIdList()),
                                IndustryDO::getId, industryQuery.getNotIndustryIdList())
                        .orderByDesc(IndustryDO::getId));
        return TransferUtils.transferPage(page, IndustryVO::new);
    }

    @Override
    public void buildPageQuery(IndustryPageQuery industryQuery) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 运营商域 operatorTypeThirdIdGroupCountQuery 跨域查询缺失, 无法反填 notIndustryIdList");
    }
}
