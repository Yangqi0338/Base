package com.newzkl.platform.base.biz.account.model.assembler;

import com.newzkl.platform.base.biz.account.model.level.req.LevelReq;
import com.newzkl.platform.base.biz.account.model.level.res.LevelRes;
import com.newzkl.platform.base.biz.account.model.level.vo.LevelVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import org.mapstruct.Mapper;

/**
 * 等级转换器
 *
 * @author KC
 */
@Mapper(componentModel = "spring")
public interface LevelAssembler extends BaseAssembler<LevelReq, LevelVO> {

    /**
     * 领域视图转对外出参
     *
     * @param it 领域视图
     * @return 对外出参
     */
    LevelRes vo2Res(LevelVO it);
}
