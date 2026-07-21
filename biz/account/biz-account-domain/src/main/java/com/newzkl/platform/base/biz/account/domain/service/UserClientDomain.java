package com.newzkl.platform.base.biz.account.domain.service;


import com.newzkl.platform.base.common.ddd.model.EditColumnDTO;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelError;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import com.newzkl.platform.base.biz.account.model.vo.tencent.ImCreateUserAccountEntity;
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

    void memberEdit(List<EditColumnDTO> editColumnList, Long id);

    MemberVO member(Long memberId);

    void sendTencentCreateUserMsg(ImCreateUserAccountEntity entity);

    /**
     * 模糊查询
     *
     * @param nickname
     * @return
     */
    List<MemberVO> queryMember(String nickname);

    /**
     * 注销会员
     * @param accountId
     * @param command
     */
    void cancelMember(Long accountId, CancelMemberReq command);

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
    EasyExcelError adminImportAccount(MultipartFile file);
}
