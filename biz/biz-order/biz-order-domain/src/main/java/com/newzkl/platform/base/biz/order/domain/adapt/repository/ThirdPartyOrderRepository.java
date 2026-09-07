package com.newzkl.platform.base.biz.order.domain.adapt.repository;




import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.biz.order.model.req.query.ThirdPartyOrderRecordQuery;

/**
 * 三方订单记录仓储
 *
 * @author fang
 */
public interface ThirdPartyOrderRepository {

    /**
     * 保存一条三方订单记录 无 id 走插入 有 id 走按 id 更新
     *
     * @param request 记录
     * @return 保存后的记录 插入时回填 id
     */
    ThirdPartyOrderRecordDTO saveRecord(ThirdPartyOrderRecordDTO request);

    /**
     * 分页查询三方订单记录
     *
     * @param query 查询条件 必须显式设置 pageNo/pageSize 否则恒返 0 行
     * @return 分页结果
     */
    Page<ThirdPartyOrderRecordDTO> selectPage(ThirdPartyOrderRecordQuery query);
}
