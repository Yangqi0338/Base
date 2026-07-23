package com.newzkl.platform.base.biz.account.domain.repository;

import com.newzkl.platform.base.common.ddd.model.EditColumnDTO;
import com.newzkl.platform.base.biz.account.model.res.SelectorOutRes;
import com.newzkl.platform.base.biz.account.model.vo.SelectorVO;
// TODO[cross-domain relation]: import relation.vo.TeamUserCountRes;

import java.util.List;

/**
 * 甄选师
 *
 * @author fang
 */
public interface SelectorRepository {

    List<SelectorOutRes> selectorListVO(List<Long> list);

    Long selectorSave(SelectorVO selector);

    int selectorEdit(SelectorVO selector);

    int selectorDelete(List<Long> selectorIdList);

    void selectorEdit(List<EditColumnDTO> columnList, Long id);

    SelectorVO selector(Long selectorId);

    // TODO[cross-domain relation]: List<TeamUserCountRes> countLevelNumber(Long id);


//    Integer executeSelectorLevel(ConditionCommand conditionCommand);
}
