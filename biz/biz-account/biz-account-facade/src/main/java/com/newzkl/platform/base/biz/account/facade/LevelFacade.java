package com.newzkl.platform.base.biz.account.facade;


import com.newzkl.platform.base.common.ddd.facade.ConditionCommand;
import com.newzkl.platform.base.common.ddd.facade.LevelRpcVO;
import com.newzkl.platform.base.common.ddd.facade.PermissionRpcVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;

/**
 * @author muc_fang
 * @Description: 字典
 * @date 2023/12/1411:51
 */
public interface LevelFacade {
    /**
     * 甄选师等级配置值对象
     *
     * @param level 甄选师等级
     * @return
     */
    LevelRpcVO selectorLevelVO(Integer level);

    /**
     * 等级计算 : null 表示不满足任何等级
     *
     * @param conditionCommand
     * @return
     */
    Integer executeLevel(ConditionCommand conditionCommand);

    /**
     * 权益配置值对象
     *
     * @param level 甄选师等级
     * @return
     */
    PermissionRpcVO levelPermissionVO(AccountEnum.Identity identity, Integer level);
}
