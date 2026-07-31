package com.newzkl.platform.base.biz.sys.infrastructure.gateway;


import com.newzkl.platform.base.biz.sys.domain.adapt.repository.DictRepository;
import com.newzkl.platform.base.biz.sys.facade.IDictFacade;
import com.newzkl.platform.base.biz.sys.infrastructure.dao.DictDAO;
import com.newzkl.platform.base.biz.sys.model.dict.res.DictRes;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author muc_fang
 * @Description: 字典
 * @date 2023/12/1411:56
 */
@Component
@DubboService
@RequiredArgsConstructor
public class DictFacadeImpl implements IDictFacade {

    private final DictRepository dictRepository;

    @Override
    public String get(Long id) {
        DictRes dictRes = dictRepository.dictVO(id);
        if(dictRes == null){
            return null;
        }
        return dictRes.getValue();
    }

    @Override
    public void set(Long id, String value) {
        DictRes dictRes = new DictRes();
        dictRes.setId(id);
        dictRes.setValue(value);
        dictRepository.dictSave(dictRes);
    }
}
