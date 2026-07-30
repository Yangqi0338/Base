package com.newzkl.platform.base.biz.sys.domain.service.impl;

import com.newzkl.platform.base.biz.sys.domain.adapt.repository.DictRepository;
import com.newzkl.platform.base.biz.sys.domain.service.DictDomain;
import com.newzkl.platform.base.biz.sys.model.dict.query.DictQuery;
import com.newzkl.platform.base.biz.sys.model.dict.req.DictReq;
import com.newzkl.platform.base.biz.sys.model.dict.res.DictRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典领域服务实现
 *
 * @author fang
 */
@Service
@RequiredArgsConstructor
public class DictDomainImpl implements DictDomain {

    private final DictRepository dictRepository;

    @Override
    public Long dictSave(DictReq req) {
        return dictRepository.dictSave(toVO(req));
    }

    @Override
    public DictRes dictVO(Long id) {
        return dictRepository.dictVO(id);
    }

    @Override
    public List<DictRes> dictList(DictQuery dictQuery) {
        return dictRepository.dictList(dictQuery);
    }

    @Override
    public String nextCode(Long id) {
        DictRes dictRes = dictRepository.dictVOLock(id);
        String value;
        if (dictRes == null || dictRes.getValue() == null) {
            value = "1";
        } else {
            value = Long.toString(Long.parseLong(dictRes.getValue()) + 1);
        }
        DictReq dict = new DictReq();
        dict.setId(id);
        dict.setValue(value);
        dictRepository.dictSave(toVO(dict));
        return value;
    }

    /**
     * 请求转视图对象
     *
     * @param req 字典请求
     * @return 字典视图对象
     */
    private DictRes toVO(DictReq req) {
        DictRes vo = new DictRes();
        vo.setId(req.getId());
        vo.setValue(req.getValue());
        vo.setDesc(req.getDesc());
        return vo;
    }
}
