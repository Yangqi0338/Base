package com.newzkl.platform.base.biz.finance.domain.earnings.service;

import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountContributeRepository;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.ConsumeEarningDataRepository;
import com.newzkl.platform.base.biz.finance.model.assembler.EarningRecordAssembler;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author niu
 * @description: 数据服务
 * @date 2023/12/20 17:17
 */
public class EarningStrategySupport {

    protected static Map<EarningsEnum.ConsumeType, ConsumeEarnings> consumeTypeMap = new ConcurrentHashMap<>();
    @Autowired(required = false)
    private List<ConsumeEarnings> consumeEarnings;
    @Resource
    protected ConsumeEarningDataRepository consumeEarningDataRepository;
    @Resource
    protected EarningRecordAssembler recordAssembler;

    @PostConstruct
    public void init() {
        if (consumeEarnings != null) {
            consumeEarnings.forEach(x -> {
                consumeTypeMap.put(x.consumeType(), x);
            });
        }
    }


}
