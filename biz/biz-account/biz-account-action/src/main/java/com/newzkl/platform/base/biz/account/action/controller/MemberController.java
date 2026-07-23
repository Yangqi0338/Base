package com.newzkl.platform.base.biz.account.action.controller;

import com.newzkl.platform.base.biz.account.action.cmd.EditColumnCmd;
import com.newzkl.platform.base.biz.account.domain.service.UserClientDomain;
import com.newzkl.platform.base.biz.account.model.req.AdminRegisterIdentityReq;
import com.newzkl.platform.base.biz.account.model.req.CancelMemberReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.MemberReq;
import com.newzkl.platform.base.biz.account.model.req.UpdateMemberInfoCommand;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 会员控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/user/member")
@RequiredArgsConstructor
public class MemberController {

    private final UserClientDomain userClientDomain;

    /**
     * 会员新增。
     *
     * @param memberReq 会员请求
     * @return 会员 ID
     */
    @PostMapping("memberSave")
    public ScmResult<Long> memberSave(@Validated @RequestBody MemberReq memberReq) {
        return ScmResult.success(userClientDomain.memberSave(memberReq));
    }

    /**
     * 会员修改。
     *
     * @param memberReq 会员请求
     * @return 修改数量
     */
    @PostMapping("memberEdit")
    public ScmResult<Integer> memberEdit(@Validated @RequestBody MemberReq memberReq) {
        return ScmResult.success(userClientDomain.memberEdit(memberReq.getId(), memberReq));
    }

    /**
     * 会员按列修改。
     *
     * @param cmd 列编辑命令
     * @return 成功结果
     */
    @PostMapping("memberEditColumn")
    public ScmResult<Void> memberEditColumn(@Validated @RequestBody EditColumnCmd cmd) {
        userClientDomain.memberEdit(cmd.getEditColumnList(), cmd.getId());
        return ScmResult.success();
    }

    /**
     * 会员删除。
     *
     * @param idListObj ID 列表
     * @return 删除数量
     */
    @PostMapping("memberDelete")
    public ScmResult<Integer> memberDelete(@Validated @RequestBody IdListCommand idListObj) {
        return ScmResult.success(userClientDomain.memberDelete(idListObj.getIdList()));
    }

    /**
     * 会员详情。
     *
     * @param memberId 会员 ID (为空时取当前账号)
     * @return 会员 VO
     */
    @PostMapping("member")
    public ScmResult<MemberVO> member(@RequestParam(value = "id", required = false) Long memberId) {
        if (memberId == null) {
            memberId = SecurityUtils.getAccountId();
        }
        return ScmResult.success(userClientDomain.member(memberId));
    }

    /**
     * 会员模糊查询。
     *
     * @param nickname 昵称
     * @return 会员列表
     */
    @PostMapping("queryMember")
    public ScmResult<List<MemberVO>> queryMember(@RequestParam("nickname") String nickname) {
        return ScmResult.success(userClientDomain.queryMember(nickname));
    }

    /**
     * 注销会员。
     *
     * @param command 注销会员请求
     * @return 成功结果
     */
    @PostMapping("cancelMember")
    public ScmResult<Void> cancelMember(@Validated @RequestBody CancelMemberReq command) {
        userClientDomain.cancelMember(SecurityUtils.getAccountId(), command);
        return ScmResult.success();
    }

    /**
     * 修改会员信息。
     *
     * @param command 会员信息修改命令
     * @return 成功结果
     */
    @PostMapping("updateMemberInfo")
    public ScmResult<Void> updateMemberInfo(@Validated @RequestBody UpdateMemberInfoCommand command) {
        userClientDomain.updateMemberInfo(SecurityUtils.getAccountId(), command);
        return ScmResult.success();
    }

    /**
     * 平台侧添加会员。
     *
     * @param req 平台注册身份请求
     * @return 身份注册结果
     */
    @PostMapping("adminCreateMember")
    public ScmResult<IdentityRegisterRes> adminCreateMember(@Validated @RequestBody AdminRegisterIdentityReq req) {
        return ScmResult.success(userClientDomain.adminCreateMember(req));
    }

    /**
     * 批量导入会员 (Excel)。
     *
     * @param file 上传的 Excel 文件
     * @return 导入结果
     */
    @PostMapping("adminImportAccount")
    public ScmResult<EasyExcelErrorVO> adminImportAccount(@RequestParam("file") MultipartFile file) {
        return ScmResult.success(userClientDomain.adminImportAccount(file));
    }
}
