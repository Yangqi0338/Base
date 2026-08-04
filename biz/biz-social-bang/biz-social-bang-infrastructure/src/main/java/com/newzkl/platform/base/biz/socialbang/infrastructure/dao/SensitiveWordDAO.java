package com.newzkl.platform.base.biz.socialbang.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.socialbang.infrastructure.entity.SensitiveWordDO;
import com.newzkl.platform.base.biz.socialbang.model.im.query.SensitiveWordQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 敏感词 DAO
 *
 * @author KC
 */
@Mapper
@Repository
public interface SensitiveWordDAO extends BaseMapper<SensitiveWordDO> {

    /**
     * 组装查询条件
     *
     * <p>迁移: 替代源 {@code SensitiveWordDAO.xml#pageQuery} 手写 SQL。
     * 逻辑删除条件由 {@code @TableLogic} 自动追加, 不再手写 {@code is_deleted = 0}。
     * 源 SQL 排序为 {@code ORDER BY ${query.sortField} ${query.sortType}} 直拼字段名(存在注入面),
     * 迁移改走本仓统一排序机制, 由调用方 {@code orderBy(query)} 追加。</p>
     *
     * @param query 查询条件
     * @return 条件包装器
     */
    default BaseLambdaQueryWrapper<SensitiveWordDO> getLw(SensitiveWordQuery query) {
        BaseLambdaQueryWrapper<SensitiveWordDO> lw = new BaseLambdaQueryWrapper<>();
        lw.notEmptyLike(SensitiveWordDO::getSensitiveWord, query.getSensitiveWord())
                .notEmptyEq(SensitiveWordDO::getSourceType, query.getSourceType())
                .doBetween(SensitiveWordDO::getAddTime, query.getStartTime(), query.getEndTime());
        return lw;
    }
}
