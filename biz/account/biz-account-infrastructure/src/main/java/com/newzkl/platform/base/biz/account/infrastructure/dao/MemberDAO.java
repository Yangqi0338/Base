package com.newzkl.platform.base.biz.account.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.account.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.biz.account.infrastructure.entity.MemberDO;
import com.newzkl.platform.base.biz.account.model.req.MemberQuery;
import com.newzkl.platform.base.biz.account.model.vo.OrderMemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * c端客户
 *
 * @author fang
 */
@Mapper
public interface MemberDAO extends BaseMapper<MemberDO> {

    Long queryByWxId(@Param("wxId") String wxId);

    OrderMemberVO orderMemberVO(@Param("memberId") Long memberId);


    List<MemberDO> selectMemberByAccountId(@Param("accountId") Long accountId);

    MemberDO validByAccountId(@Param("accountId") Long accountId);

    default BaseLambdaQueryWrapper<MemberDO> getLw(MemberQuery query) {
        return new BaseLambdaQueryWrapper<>();
    }
}