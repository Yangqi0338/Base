package com.newzkl.platform.base.biz.socialbang.domain.adapt.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.socialbang.model.im.query.SensitiveWordQuery;
import com.newzkl.platform.base.biz.socialbang.model.im.vo.SensitiveWord;

import java.util.Optional;

/**
 * 敏感词仓储接口
 *
 * <p>迁移自 {@code com.zkl.scm.im.structure.tencent.repository.SensitiveWordRepository}。
 * 源接口继承 mybatis-plus {@code IService}, 把 getById/removeById 等框架方法一并暴露给上层;
 * 本仓 domain 不得依赖 infra 框架接口, 故按实际被调用到的能力显式声明。</p>
 *
 * @author KC
 */
public interface SensitiveWordRepository {

    /**
     * 保存/更新敏感词, 返回填充后的实体
     *
     * @param sensitiveWord 敏感词实体
     * @return 保存后的实体(含自增ID)
     */
    SensitiveWord saveSensitiveWord(SensitiveWord sensitiveWord);

    /**
     * 按主键查询敏感词
     *
     * @param id 主键ID
     * @return 敏感词实体, 不存在返回 null
     */
    SensitiveWord getById(Long id);

    /**
     * 按敏感词内容查询(查重)
     *
     * @param sensitiveWord 敏感词内容
     * @return 敏感词实体, 不存在返回 {@code Optional.empty()}
     */
    Optional<SensitiveWord> findByWord(String sensitiveWord);

    /**
     * 分页查询敏感词列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<SensitiveWord> pageQuery(SensitiveWordQuery query);

    /**
     * 按主键逻辑删除敏感词
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean removeById(Long id);

    /**
     * 按主键更新敏感词
     *
     * @param sensitiveWord 敏感词实体(含主键ID)
     * @return 是否更新成功
     */
    boolean updateSensitiveWord(SensitiveWord sensitiveWord);
}
