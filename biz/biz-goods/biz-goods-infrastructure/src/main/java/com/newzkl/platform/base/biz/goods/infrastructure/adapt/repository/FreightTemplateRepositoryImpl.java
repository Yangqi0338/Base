package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.freight.repository.FreightTemplateRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.FreightTemplateDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.FreightTemplateDO;
import com.newzkl.platform.base.biz.goods.model.goods.entity.freight.FreightTemplate;
import com.newzkl.platform.base.biz.goods.model.goods.query.freight.FreightTemplateQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.FreePostConditionVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.FreightTemplateVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.RegionVO;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.biz.goods.model.enums.RedisEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 运费模板
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class FreightTemplateRepositoryImpl implements FreightTemplateRepository {

    private final FreightTemplateDAO freightTemplateDAO;

    @Override
    public Long freightTemplateSave(FreightTemplate freightTemplate) {
        FreightTemplateDO freightTemplateDO = TransferUtils.transfer(freightTemplate, FreightTemplateDO::new);
        freightTemplateDAO.insert(freightTemplateDO);
        return freightTemplateDO.getId();
    }

    @Override
    public void freightTemplateDelete(List<Long> idList) {
        freightTemplateDAO.deleteByIds(idList);
        //刷新redis
        List<String> redisFreightTemplateList = idList.stream().map(x -> RedisEnum.Key.FREIGHT_TEMPLATE.getCode(x.toString())).collect(Collectors.toList());
        RedisUtil.batchDel(redisFreightTemplateList);
    }

    @Override
    public void freightTemplateEdit(FreightTemplate freightTemplate) {
        freightTemplateDAO.updateById(TransferUtils.transfer(freightTemplate, FreightTemplateDO::new));
        //刷新redis
        String redisFreightTemplate = RedisEnum.Key.FREIGHT_TEMPLATE.getCode(freightTemplate.getId().toString());
        RedisUtil.del(redisFreightTemplate);
    }

    @Override
    public FreightTemplate freightTemplate(Long id) {
        return TransferUtils.transfer(freightTemplateDAO.selectById(id), FreightTemplate::new, (c, v) -> {
            v.setFreePostCondition(JSONObject.parseObject(c.getFreePostCondition(), FreePostConditionVO.class));
            v.setRegionSpec(JSON.parseArray(c.getRegionSpec(), RegionVO.class));
        });
    }

    @Override
    public Page<FreightTemplateVO> freightTemplatePage(FreightTemplateQuery freightTemplateQuery) {
        return freightTemplateDAO.queryPage(RepositorySupport.page(freightTemplateQuery), freightTemplateQuery);
    }

}
