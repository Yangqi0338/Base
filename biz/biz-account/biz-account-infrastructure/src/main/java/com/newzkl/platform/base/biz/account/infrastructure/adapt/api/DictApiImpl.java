package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.account.domain.adapt.api.DictApi;
import com.newzkl.platform.base.biz.sys.facade.IDictFacade;
import com.newzkl.platform.base.common.ddd.facade.AmountRateDTO;
import com.newzkl.platform.base.common.ddd.model.enums.sys.DictEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeMap;

/**
 * {@code DictApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 字典能力尚未在中台落地, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖此默认实现。</p>
 *
 * @author KC
 */
@Component("accountDictApi")
public class DictApiImpl implements DictApi {

    @Autowired
    private IDictFacade dictFacade;

    @Override
    public TreeMap<Integer, Double> getChannelServiceFee() {
        String dictOpenStr = dictFacade.get(DictEnum.Key.CHANNEL_SERVICE_FEE.getCode());
        List<AmountRateDTO> list = JSONUtil.parseArray(dictOpenStr).toList(AmountRateDTO.class);
        return getTree(list);
    }

    private TreeMap<Integer, Double> getTree(List<AmountRateDTO> itemList) {
        TreeMap<Integer, Double> treeMap = new TreeMap<Integer, Double>();
        for (AmountRateDTO amountRateDTO : itemList) {
            treeMap.put(amountRateDTO.getAmount(), amountRateDTO.getRate());
        }
        return treeMap;
    }

}
