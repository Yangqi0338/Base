package com.newzkl.platform.base.biz.account.domain.repository;

import com.newzkl.platform.base.biz.account.model.support.OperatorConfigVO;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.model.req.OperatorQuery;
import com.newzkl.platform.base.biz.account.model.res.OperatorDomainInfo;
import com.newzkl.platform.base.biz.account.model.vo.OperatorVO;

import java.util.List;
import java.util.Map;

/**
 * 市场运营商
 *
 * @author fang
 */
public interface OperatorRepository {

    Long operatorSave(OperatorVO operator);

    boolean operatorEdit(OperatorVO operator);

    int operatorDelete(List<Long> operatorIdList);

    void operatorEdit(List<EditColumnVO> columnList, Long id);

    OperatorVO operator(Long operatorId);

    List<Map<String, String>> operatorCountByQuery(OperatorQuery query);

    OperatorConfigVO getOperatorConfig();

    Long count(OperatorQuery query);

    OperatorDomainInfo getOperatorDomainInfo(Long operatorId);
}
