package com.newzkl.platform.base.biz.account.domain.repository;

import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;

import java.util.List;

/**
 * c端客户
 *
 * @author fang
 */
public interface MemberRepository {

    Long memberSave(MemberVO member);

    int memberEdit(MemberVO member);

    int memberDelete(List<Long> memberIdList);

    void memberEdit(List<EditColumnVO> columnList, Long id);

    MemberVO member(Long memberId);

    List<MemberVO> selectMemberByAccountId(Long accountId);

    List<MemberVO> selectMemberByAccountIdList(List<Long> accountIdList);

    /**
     * 模糊查询
     *
     * @param nickname
     * @return
     */
    List<MemberVO> queryMember(String nickname);

}
