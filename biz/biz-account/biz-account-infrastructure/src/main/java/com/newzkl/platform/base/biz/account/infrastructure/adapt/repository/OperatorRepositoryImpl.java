package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.account.model.support.OperatorConfigVO;
import com.newzkl.platform.base.common.ddd.model.EditColumnDTO;
import com.newzkl.platform.base.biz.account.domain.repository.OperatorRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.OperatorDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.OperatorDO;
import com.newzkl.platform.base.biz.account.model.req.OperatorQuery;
import com.newzkl.platform.base.biz.account.model.res.OperatorDomainInfo;
import com.newzkl.platform.base.biz.account.model.vo.OperatorVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 市场运营商
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class OperatorRepositoryImpl implements OperatorRepository {

    private final OperatorDAO operatorDAO;

//    @DubboReference
//    private IDictFacade dictFacade;

    @Override
    public Long operatorSave(OperatorVO operator) {
        OperatorDO operatorDO = TransferUtils.transfer(operator, OperatorDO.class);
        operatorDAO.insert(operatorDO);
        return operatorDO.getId();
    }

    @Override
    public boolean operatorEdit(OperatorVO operator) {
        OperatorDO operatorDO = TransferUtils.transfer(operator, OperatorDO.class);
        return operatorDAO.updateById(operatorDO) > 0;
    }

    @Override
    public int operatorDelete(List<Long> operatorIdList) {
        OperatorQuery query = new OperatorQuery();
        query.setIdList(operatorIdList);
        return operatorDAO.deleteByIds(operatorIdList);
    }

    @Override
    public void operatorEdit(List<EditColumnDTO> columnList, Long id) {
        OperatorQuery query = new OperatorQuery();
        query.setId(id);
//        operatorDAO.columnByQuery(columnList, query);
    }

    @Override
    public OperatorVO operator(Long operatorId) {
        OperatorDO operatorDO = operatorDAO.selectById(operatorId);
        return TransferUtils.transfer(operatorDO, OperatorVO.class);
    }

    @Override
    public List<Map<String, String>> operatorCountByQuery(OperatorQuery operatorQuery) {
        return operatorDAO.groupCountByQuery(operatorQuery);
    }

    @Override
    public OperatorConfigVO getOperatorConfig() {
        return JSONUtil.toBean(
//                dictFacade.get(DictEnum.Key.OPERATOR_CONFIG.getCode()),
                "",
                OperatorConfigVO.class);
    }

    @Override
    public Long count(OperatorQuery operatorQuery) {
        return operatorDAO.selectCount(operatorDAO.getLw(operatorQuery));
    }

    @Override
    public OperatorDomainInfo getOperatorDomainInfo(Long operatorId) {
        OperatorQuery query = new OperatorQuery();
        query.setId(operatorId);
        OperatorDO operatorDO = operatorDAO.selectOne(operatorDAO.getLw(query).select(OperatorDomainInfo.class));
        return TransferUtils.transfer(operatorDO, OperatorDomainInfo::new);
    }

}
