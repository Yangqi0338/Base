package com.newzkl.platform.base.biz.account.domain.repository;

import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.model.res.DealerOutRes;
import com.newzkl.platform.base.biz.account.model.vo.DealerVO;

import java.util.List;

/**
 * 市场交易师
 *
 * @author fang
 */
public interface DealerRepository {

    Long dealerSave(DealerVO dealer);

    int dealerEdit(DealerVO dealer);

    int dealerDelete(List<Long> dealerIdList);

    void dealerEdit(List<EditColumnVO> columnList, Long id);

    DealerVO dealer(Long dealerId);

    List<DealerOutRes> dealerRpcVO(List<Long> dealerIdList);


    List<DealerOutRes> dealerRpcVO();


}
