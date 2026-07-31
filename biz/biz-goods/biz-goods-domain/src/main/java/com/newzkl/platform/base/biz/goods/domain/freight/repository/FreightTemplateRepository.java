package com.newzkl.platform.base.biz.goods.domain.freight.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.entity.freight.FreightTemplate;
import com.newzkl.platform.base.biz.goods.model.goods.query.freight.FreightTemplateQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.FreightTemplateVO;

import java.util.List;

/**
* 运费模板
*
* <p>每方法单一持久化动作 (一次 IO); DB 写与 Redis 缓存清由 {@code FreightDomain} 编排</p>
*
* @author fang
*/
public interface FreightTemplateRepository {

    /**
     * 插入运费模板, 回填并返回主键
     *
     * @param freightTemplate 运费模板
     * @return 运费模板 ID
     */
    Long freightTemplateSave(FreightTemplate freightTemplate);

    /**
     * 按 ID 列表删除运费模板 (不含缓存清理)
     *
     * @param idList 运费模板 ID 列表
     */
    void freightTemplateDelete(List<Long> idList);

    /**
     * 按 ID 更新运费模板 (不含缓存清理)
     *
     * @param freightTemplate 运费模板 (id 必填)
     */
    void freightTemplateEdit(FreightTemplate freightTemplate);

    /**
     * 清理运费模板 Redis 缓存
     *
     * @param idList 运费模板 ID 列表
     */
    void evictFreightTemplateCache(List<Long> idList);

    /**
     * 按 ID 查运费模板
     *
     * @param id 运费模板 ID
     * @return 运费模板
     */
    FreightTemplate freightTemplate(Long id);

    /**
     * 分页查运费模板
     *
     * @param freightTemplateQuery 查询条件
     * @return 运费模板分页
     */
    Page<FreightTemplateVO> freightTemplatePage(FreightTemplateQuery freightTemplateQuery);

}
