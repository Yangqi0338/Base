package com.newzkl.platform.base.biz.account.application.provider;

import com.newzkl.platform.base.biz.account.domain.service.LevelDomain;
import com.newzkl.platform.base.biz.account.facade.LevelFacade;
import com.newzkl.platform.base.biz.account.model.level.req.LevelQuery;
import com.newzkl.platform.base.biz.account.model.level.res.LevelRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.ConditionCommand;
import com.newzkl.platform.base.common.ddd.facade.LevelRpcVO;
import com.newzkl.platform.base.common.ddd.facade.PermissionRpcVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LevelFacadeProvider implements LevelFacade {

    @Autowired
    private LevelDomain levelDomain;

    private LevelRpcVO levelVO(AccountEnum.Identity identity, Integer level) {
        LevelQuery levelQuery = new LevelQuery();
        levelQuery.setType(identity);
        levelQuery.setValue(level);
        LevelRes levelVO = levelDomain.findByQuery(levelQuery);
        LevelRpcVO levelRpcVO = TransferUtils.transfer(levelVO, LevelRpcVO.class);
        levelRpcVO.getPermission().setIdentity(identity);
        return levelRpcVO;
    }

    @Override
    public LevelRpcVO selectorLevelVO(Integer level) {
        return null;
    }

    @Override
    public Integer executeLevel(ConditionCommand conditionCommand) {
        return 0;
    }

    @Override
    public PermissionRpcVO levelPermissionVO(AccountEnum.Identity identity, Integer level) {
        return levelVO(identity, level).getPermission();
    }
}
