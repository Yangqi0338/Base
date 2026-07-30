package com.newzkl.platform.base.biz.socialbang.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.socialbang.domain.adapt.repository.SensitiveWordRepository;
import com.newzkl.platform.base.biz.socialbang.infrastructure.dao.SensitiveWordDAO;
import com.newzkl.platform.base.biz.socialbang.infrastructure.entity.SensitiveWordDO;
import com.newzkl.platform.base.biz.socialbang.model.im.query.SensitiveWordQuery;
import com.newzkl.platform.base.biz.socialbang.model.im.vo.SensitiveWord;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@code SensitiveWordRepository} 实现
 *
 * <p>迁移自 {@code com.zkl.scm.im.structure.tencent.repository.SensitiveWordRepositoryImpl}。
 * 源继承 mybatis-plus {@code ServiceImpl} 并配 XML; Base 侧改为 DAO + {@code getLw} 条件组装,
 * DO↔领域实体转换由 {@code TransferUtils} 完成(禁 BeanUtils.copyProperties)。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class SensitiveWordRepositoryImpl extends RepositorySupport implements SensitiveWordRepository {

    private final SensitiveWordDAO sensitiveWordDAO;

    @Override
    public SensitiveWord saveSensitiveWord(SensitiveWord sensitiveWord) {
        SensitiveWordDO doObj = TransferUtils.transfer(sensitiveWord, SensitiveWordDO.class);
        if (doObj.getId() == null) {
            sensitiveWordDAO.insert(doObj);
        } else {
            sensitiveWordDAO.updateById(doObj);
        }
        return TransferUtils.transfer(doObj, SensitiveWord.class);
    }

    @Override
    public SensitiveWord getById(Long id) {
        return TransferUtils.transfer(sensitiveWordDAO.selectById(id), SensitiveWord.class);
    }

    @Override
    public Optional<SensitiveWord> findByWord(String sensitiveWord) {
        SensitiveWordDO doObj = sensitiveWordDAO.selectOne(buildOne(new BaseLambdaQueryWrapper<SensitiveWordDO>()
                .notEmptyEq(SensitiveWordDO::getSensitiveWord, sensitiveWord)));
        return Optional.ofNullable(TransferUtils.transfer(doObj, SensitiveWord.class));
    }

    @Override
    public IPage<SensitiveWord> pageQuery(SensitiveWordQuery query) {
        Page<SensitiveWordDO> doPage = sensitiveWordDAO.selectPage(RepositorySupport.page(query),
                sensitiveWordDAO.getLw(query).orderBy(query)
        );
        return TransferUtils.transferPage(doPage, SensitiveWord.class);
    }

    @Override
    public boolean removeById(Long id) {
        return sensitiveWordDAO.deleteById(id) > 0;
    }

    @Override
    public boolean updateSensitiveWord(SensitiveWord sensitiveWord) {
        SensitiveWordDO doObj = TransferUtils.transfer(sensitiveWord, SensitiveWordDO.class);
        return sensitiveWordDAO.updateById(doObj) > 0;
    }
}
