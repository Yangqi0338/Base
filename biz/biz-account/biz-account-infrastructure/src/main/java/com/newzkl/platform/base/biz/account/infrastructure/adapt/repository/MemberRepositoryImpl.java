package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;

import com.newzkl.platform.base.common.core.rocketmq.utils.MQUtil;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.common.core.rocketmq.MQ;
import com.newzkl.platform.base.biz.account.domain.repository.MemberRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.MemberDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.MemberDO;
import com.newzkl.platform.base.biz.account.model.req.MemberQuery;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import com.newzkl.platform.base.biz.account.model.vo.tencent.ImCreateUserAccountObj;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * c端客户
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberDAO memberDAO;

    @Override
    public Long memberSave(MemberVO member) {
        MemberDO memberDO = TransferUtils.transfer(member, MemberDO::new);
        memberDAO.insert(memberDO);
        return memberDO.getId();
    }

    @Override
    public int memberEdit(MemberVO member) {
        MemberDO memberDO = TransferUtils.transfer(member, MemberDO::new);
        return memberDAO.updateById(memberDO);
    }

    @Override
    public int memberDelete(List<Long> memberIdList) {
        return memberDAO.deleteByIds(memberIdList);
    }

    @Override
    public void memberEdit(List<EditColumnVO> columnList, Long id) {
        MemberQuery query = new MemberQuery();
        query.setId(id);
//        memberDAO.columnByQuery(columnList, query);
    }

    @Override
    public MemberVO member(Long memberId) {
        MemberDO memberDO = memberDAO.selectById(memberId);
        return TransferUtils.transfer(memberDO, MemberVO::new);
    }

    @Override
    public List<MemberVO> selectMemberByAccountId(Long accountId) {

        List<MemberDO> memberVOS =
                Optional.ofNullable(memberDAO.selectMemberByAccountId(accountId)).orElse(Collections.emptyList());

        return TransferUtils.transfers(memberVOS, MemberVO.class);
    }

    @Override
    public List<MemberVO> selectMemberByAccountIdList(List<Long> accountIdList) {

        List<MemberDO> memberVOS =
                Optional.ofNullable(memberDAO.selectByIds(accountIdList)).orElse(Collections.emptyList());

        return TransferUtils.transfers(memberVOS, MemberVO.class);
    }

    @Override
    public List<MemberVO> queryMember(String nickname) {
        MemberQuery query = new MemberQuery();
        query.setNickname(nickname);
        List<MemberDO> memberVOS = memberDAO.selectList(memberDAO.getLw(query));
        return TransferUtils.transfers(memberVOS, MemberVO.class);
    }

    @Override
    public void sendTencentCreateUserMsg(ImCreateUserAccountObj entity) {
        MQUtil.send(MQ.Tag.IM_CREAT_USER_ACCOUNT_EVENT, entity);
    }

}
