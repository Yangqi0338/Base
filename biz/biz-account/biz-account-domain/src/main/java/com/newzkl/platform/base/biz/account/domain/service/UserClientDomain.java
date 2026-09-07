package com.newzkl.platform.base.biz.account.domain.service;


import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.biz.account.model.dto.MemberDTO;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.MemberRes;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * c端客户
 *
 * @author fang
 */
public interface UserClientDomain {

    Long memberSave(MemberReq memberCommand);

    /**
     * 会员修改
     *
     * <p>{@code nickname} / {@code head} 落 account 表(member 表无该两列), 命中时先落账号侧
     * 再写 member 行, 同事务保证两表一致。入参三拆分常住地在端内拼成 {@code residence} 单列</p>
     *
     * @param id            会员账号ID (与 member 主键同值)
     * @param memberCommand 会员修改入参
     * @return 影响行数
     * @ext 主数据 member, 副数据 account(单副, 副数据不再向下关联)
     */
    int memberEdit(Long id, MemberReq memberCommand);

    int memberDelete(List<Long> memberIdList);

    void memberEdit(List<EditColumnVO> editColumnList, Long id);

    /**
     * 会员视图
     *
     * <p>历史签名, 返回内部 {@link MemberVO}(含 facade 转换与统计字段)。
     * 对外出参走 {@link #memberBase} / {@link #memberDetail}</p>
     *
     * @param memberId 会员账号ID
     * @return 会员视图, 无则 null
     * @ext 主数据 member (无副数据)
     */
    MemberVO member(Long memberId);

    /**
     * 会员纯净详情
     *
     * <p>只查 member 一张表, 相比 {@link #memberDetail} 少一次 account 查询。
     * 适用于取生日/常住地/微信绑定等只需自有列的场景</p>
     *
     * @param memberId 会员账号ID
     * @return 会员纯净视图
     * @ext 主数据 member (无副数据)
     */
    MemberDTO memberBase(Long memberId);

    /**
     * 会员聚合详情
     *
     * <p>主数据 member 由 assembler 搬列, 副数据 account 逐字段显式填充</p>
     *
     * @param memberId 会员账号ID
     * @return 会员聚合视图
     * @ext 主数据 member, 副数据 account(单副, 副数据不再向下关联)。与
     *      {@code AccountController.identityDetail} 的「主 account / 副身份」方向相反, 两者不可互相替代
     */
    MemberRes memberDetail(Long memberId);

    /**
     * 注销账号
     *
     * <p>仅将 account 状态置为注销, 身份表 (member/channel/...) 数据保留。</p>
     *
     * @param accountId 账号 ID
     * @param command   注销命令
     */
    void cancelAccount(Long accountId, CancelMemberReq command);

    /**
     * 回收已注销超过24h宽限期的用户账号(物理删除, 释放 username 唯一索引)
     *
     * @return 删除行数
     */
    int recycleCanceledMember();
}
