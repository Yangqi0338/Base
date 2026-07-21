package com.newzkl.platform.base.biz.account.domain.repository;

import com.newzkl.platform.base.common.ddd.model.EditColumnDTO;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import com.newzkl.platform.base.biz.account.model.vo.tencent.ImCreateUserAccountEntity;

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

    void memberEdit(List<EditColumnDTO> columnList, Long id);

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

    void sendTencentCreateUserMsg(ImCreateUserAccountEntity entity);
}
