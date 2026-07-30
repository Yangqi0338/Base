package com.newzkl.platform.base.biz.socialbang.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.socialbang.model.im.query.SensitiveWordQuery;
import com.newzkl.platform.base.biz.socialbang.model.im.req.SensitiveWordRequest;
import com.newzkl.platform.base.biz.socialbang.model.im.vo.SensitiveWord;

import java.util.List;

/**
 * 敏感词领域服务
 *
 * <p>迁移自 {@code com.zkl.scm.im.service.sensitiveWord.service.SensitiveWordService}
 * 与 {@code com.zkl.scm.im.tencent.domain.SensitiveWordDomainService}。
 * 源侧 service(编排+事务) 与 domainService(纯校验) 两层, Base 事务默认落 domain service,
 * 且校验无跨业务域编排, 故合并为单一 domain。</p>
 *
 * @author KC
 */
public interface SensitiveWordDomain {

    /**
     * 添加敏感词
     *
     * @param request 敏感词请求
     * @return 保存后的敏感词实体
     */
    SensitiveWord addSensitiveWord(SensitiveWordRequest request);

    /**
     * 编辑敏感词
     *
     * @param request 敏感词请求(含主键ID)
     * @return 是否编辑成功
     */
    boolean editSensitiveWord(SensitiveWordRequest request);

    /**
     * 逻辑删除敏感词
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean deleteSensitiveWord(Long id);

    /**
     * 分页查询敏感词列表
     *
     * @param query 查询条件(分页+筛选)
     * @return 分页结果
     */
    IPage<SensitiveWord> pageSensitiveWord(SensitiveWordQuery query);

    /**
     * 按主键查询敏感词详情
     *
     * @param id 主键ID
     * @return 敏感词实体, 不存在返回 null
     */
    SensitiveWord getByWord(Long id);

    /**
     * 批量导入敏感词
     *
     * <p>入参为已解析完成的 Excel 行内容; 文件读取归 action 层, 领域层只做去重与逐条落库。</p>
     *
     * @param words       Excel 解析出的敏感词内容列表(可含空值与重复值)
     * @param totalRowNum Excel 数据总行数, 仅用于结果文案
     * @return 导入结果文案
     */
    String importSensitiveWord(List<String> words, int totalRowNum);
}
