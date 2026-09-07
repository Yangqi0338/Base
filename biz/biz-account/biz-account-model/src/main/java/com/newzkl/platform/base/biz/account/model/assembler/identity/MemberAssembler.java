package com.newzkl.platform.base.biz.account.model.assembler.identity;


import com.newzkl.platform.base.biz.account.model.dto.MemberDTO;
import com.newzkl.platform.base.biz.account.model.res.MemberOutRes;
import com.newzkl.platform.base.biz.account.model.res.MemberRes;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import org.mapstruct.Mapper;

/**
 * c端客户
 *
 * @author fang
 */
@Mapper(componentModel = "spring")
public interface MemberAssembler {

    MemberOutRes vo2OutRes(MemberVO identityVO);

    /**
     * 会员视图转纯净 DTO
     *
     * @param memberVO 会员视图
     * @return 只含 member 自有列的纯净 DTO
     * @ext 主数据 member (无副数据)
     */
    MemberDTO vo2DTO(MemberVO memberVO);

    /**
     * 会员视图转聚合出参
     *
     * <p>只搬主数据 member 侧列, 副数据 account 由 domain 显式填充。
     * {@code nickname} / {@code head} 虽落在 {@link MemberVO} 上但真值在 account 侧,
     * 由 domain 覆盖填充</p>
     *
     * @param memberVO 会员视图
     * @return 会员聚合出参 (副数据尚未填充)
     * @ext 主数据 member, 副数据 account
     */
    MemberRes vo2Res(MemberVO memberVO);
}
