package com.newzkl.platform.base.biz.finance.domain.adapt.repository;


import com.newzkl.platform.base.biz.finance.model.pay.req.PromiseFlowQuery;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PromiseFlowVO;
import com.newzkl.platform.base.common.ddd.model.EditColumnDTO;

import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2023/12/19 17:01
 */
public interface PromiseFlowRepository {


    PromiseFlowVO promiseFlowVO(Long promiseFlowId);

    List<PromiseFlowVO> promiseFlowPage(PromiseFlowQuery promiseFlowQuery);

    Long promiseFlowSave(PromiseFlowVO promiseFlow);

    int promiseFlowEdit(PromiseFlowVO promiseFlow);

    int promiseFlowDelete(List<Long> promiseFlowIdList);

    void promiseFlowEdit(List<EditColumnDTO> columnList, Long id);

    PromiseFlowVO promiseFlow(Long promiseFlowId);
}
