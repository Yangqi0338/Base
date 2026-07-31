package com.newzkl.platform.base.biz.goods.domain.spu.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.entity.audit.AuditDataWorkTable;
import com.newzkl.platform.base.biz.goods.model.goods.query.audit.AuditDataWorkTableQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataWorkTableVO;

import java.util.List;

/**
* 工单审核数据
* @author fang
*/
public interface AuditDataWorkTableRepository {
    /**
    * 工单审核数据VO转实体
    * @param auditDataWorkTableVO
    * @return
    */
    AuditDataWorkTable voToAuditDataWorkTable(AuditDataWorkTableVO auditDataWorkTableVO);
    /**
     * 是否存在同商品同类型待审核工单
     * @param spuId 商品 ID
     * @param operateTarget 操作对象
     * @return 存在返回 true
     */
    boolean existsAuditing(Long spuId, Integer operateTarget);
    /**
     * 插入工单审核数据, 回填并返回主键
     * @param auditDataWorkTable 工单审核数据
     * @return 工单审核数据 ID
     */
    Long auditDataWorkTableInsert(AuditDataWorkTable auditDataWorkTable);
    /**
     * 按 ID 更新工单审核数据
     * @param auditDataWorkTable 工单审核数据 (id 必填)
     */
    void auditDataWorkTableEdit(AuditDataWorkTable auditDataWorkTable);
    /**
     * 工单审核数据删除
     * @param idList
     */
    void auditDataWorkTableDelete(List<Long> idList);
    /**
     * 工单审核数据修改
     * @param auditDataWorkTable
     * @param auditDataWorkTableQuery
     */
    void auditDataWorkTableUpdateByQuery(AuditDataWorkTable auditDataWorkTable, AuditDataWorkTableQuery auditDataWorkTableQuery);
    /**
     * 工单审核数据实体
     * @param id
     * @return
     */
    AuditDataWorkTable auditDataWorkTable(Long id);
    /**
     * 工单审核数据值对象
     * @param id
     * @return
     */
    AuditDataWorkTableVO auditDataWorkTableVO(Long id);
    /**
     * 工单审核数据实体列表
     * @param auditDataWorkTableQuery
     * @return
     */
    List<AuditDataWorkTable> auditDataWorkTableList(AuditDataWorkTableQuery auditDataWorkTableQuery);
    /**
     * 工单审核数据值对象列表
     * @param auditDataWorkTableQuery
     * @return
     */
    List<AuditDataWorkTableVO> auditDataWorkTableVOList(AuditDataWorkTableQuery auditDataWorkTableQuery);
    /**
     * 工单审核数据分页
     * @param auditDataWorkTableQuery
     * @return
     */
    Page<AuditDataWorkTableVO> auditDataWorkTablePageVOList(AuditDataWorkTableQuery auditDataWorkTableQuery);
}
