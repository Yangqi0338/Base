package com.newzkl.platform.base.biz.account.domain.service;


import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.biz.account.model.req.*;
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

    int memberEdit(Long id, MemberReq memberCommand);

    int memberDelete(List<Long> memberIdList);

    void memberEdit(List<EditColumnVO> editColumnList, Long id);

    MemberVO member(Long memberId);

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
     * 修改会员信息
     *
     * @param accountId
     * @param command
     */
    void updateMemberInfo(Long accountId, UpdateMemberInfoCommand command);

    /**
     * 平台侧添加会员
     *
     * @param req
     */
    IdentityRegisterRes adminCreateMember(AdminRegisterIdentityReq req);

    /**
     * 批量导入会员（Excel）
     *
     * @param file 上传的Excel文件
     * @return 导入结果
     */
    EasyExcelErrorVO adminImportAccount(MultipartFile file);

    /**
     * 回收已注销超过24h宽限期的用户账号(物理删除, 释放 username 唯一索引)
     *
     * @return 删除行数
     */
    int recycleCanceledMember();
}
