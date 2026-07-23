package com.newzkl.platform.base.biz.goods.domain.virtualSpu.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.dto.virtualSpu.CdkDTO;
import com.newzkl.platform.base.biz.goods.model.goods.query.virtualSpu.CdkQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.virtualSpu.CdkVO;

import java.util.List;
import java.util.Set;

/**
* 兑换码
* @author fang
*/
public interface ICdkRepository {

    void cdkSaveBatch(List<CdkDTO> cdkDTOList);

    int cdkEdit(CdkDTO cdkDTO);

    CdkDTO cdk(Long cdkId);

    Set<String> existValue(Integer systemType, Set<String> valueList);

    int cdkEditForToCdk(CdkDTO cdkDTO, List<Long> cdkIdList);

    Page<CdkVO> cdkPage(CdkQuery cdkQuery);
}
