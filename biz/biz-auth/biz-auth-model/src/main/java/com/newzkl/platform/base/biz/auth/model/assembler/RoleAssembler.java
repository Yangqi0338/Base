package com.newzkl.platform.base.biz.auth.model.assembler;

import com.newzkl.platform.base.biz.auth.model.role.req.RoleReq;
import com.newzkl.platform.base.biz.auth.model.role.res.RoleRes;
import com.newzkl.platform.base.biz.auth.model.role.vo.RoleVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import org.mapstruct.Mapper;

/**
 * 角色转换器。
 *
 * @author KC
 */
@Mapper(componentModel = "spring")
public interface RoleAssembler extends BaseAssembler<RoleReq, RoleVO> {

    /**
     * 领域视图转对外出参。
     *
     * @param it 领域视图
     * @return 对外出参
     */
    RoleRes vo2Res(RoleVO it);
}
