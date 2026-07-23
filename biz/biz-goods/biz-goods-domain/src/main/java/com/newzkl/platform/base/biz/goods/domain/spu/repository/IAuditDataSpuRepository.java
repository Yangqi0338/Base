package com.newzkl.platform.base.biz.goods.domain.spu.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.entity.audit.AuditDataSpu;
import com.newzkl.platform.base.biz.goods.model.goods.query.audit.AuditDataSpuQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataSpuVO;

import java.util.List;

/**
* 商品上传审核数据
* @author fang
*/
public interface IAuditDataSpuRepository {
    /**
    * 商品上传审核数据VO转实体
    * @param auditDataSpuVO
    * @return
    */
    AuditDataSpu voToAuditDataSpu(AuditDataSpuVO auditDataSpuVO);
    /**
     * 商品上传审核数据保存
     * @param auditDataSpu
     * @return
     */
    Long auditDataSpuSave(AuditDataSpu auditDataSpu);
    /**
     * 商品上传审核数据删除
     * @param idList
     */
    void auditDataSpuDelete(List<Long> idList);
    /**
     * 商品上传审核数据修改
     * @param auditDataSpu
     * @param auditDataSpuQuery
     */
    void auditDataSpuUpdateByQuery(AuditDataSpu auditDataSpu, AuditDataSpuQuery auditDataSpuQuery);
    /**
     * 商品上传审核数据实体
     * @param id
     * @return
     */
    AuditDataSpu auditDataSpu(Long id);
    /**
     * 商品上传审核数据值对象
     * @param id
     * @return
     */
    AuditDataSpuVO auditDataSpuVO(Long id);
    /**
     * 商品上传审核数据实体列表
     * @param auditDataSpuQuery
     * @return
     */
    List<AuditDataSpu> auditDataSpuList(AuditDataSpuQuery auditDataSpuQuery);
    /**
     * 商品上传审核数据值对象列表
     * @param auditDataSpuQuery
     * @return
     */
    List<AuditDataSpuVO> auditDataSpuVOList(AuditDataSpuQuery auditDataSpuQuery);
    /**
     * 商品上传审核数据分页
     * @param auditDataSpuQuery
     * @return
     */
    Page<AuditDataSpuVO> auditDataSpuPageVOList(AuditDataSpuQuery auditDataSpuQuery);
}
