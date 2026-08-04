package com.newzkl.platform.base.biz.user.action.controller;

import com.newzkl.platform.base.biz.user.application.task.service.MemberTaskRecordService;
import com.newzkl.platform.base.biz.user.model.task.record.query.MemberTaskRecordQuery;
import com.newzkl.platform.base.biz.user.model.task.record.req.MemberTaskRecordAddReq;
import com.newzkl.platform.base.biz.user.model.task.record.res.MemberTaskRecordExportRes;
import com.newzkl.platform.base.biz.user.model.task.record.res.MemberTaskRecordRes;
import com.newzkl.platform.base.common.core.utils.common.EasyExcelUtil;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 营销中心-任务进度
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.interfaces.controller.MemberTaskRecordController}。
 * 类级路径与方法级路径逐字沿用旧契约</p>
 *
 * <p>迁移说明: 旧 page 回 {@code IPage<MemberTaskRecordVO>}, 本仓应用层已转 {@code List<MemberTaskRecordRes>} 直返;
 * 导出走平台 {@code EasyExcelUtil.export}; 旧 {@code /send} 为测试投递端点, 逐字保留</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/member-task-record")
@Validated
@RequiredArgsConstructor
public class MemberTaskRecordController {

    private final MemberTaskRecordService memberTaskRecordService;

    /**
     * 分页查询会员任务进度
     *
     * @param query 分页查询条件
     * @return 当前页记录列表
     */
    @PostMapping("/page")
    public PlatformResult<List<MemberTaskRecordRes>> pageQuery(@Valid @RequestBody MemberTaskRecordQuery query) {
        return PlatformResult.success(memberTaskRecordService.pageQuery(query));
    }

    /**
     * 导出全部任务进度记录
     *
     * @param query 查询条件, 为空则导出全部
     * @throws IOException 导出流异常
     */
    @PostMapping("/exportAll")
    public void exportAll(@RequestBody(required = false) MemberTaskRecordQuery query) throws IOException {
        if (query == null) {
            query = new MemberTaskRecordQuery();
        }
        List<MemberTaskRecordExportRes> exportList = memberTaskRecordService.exportAll(query);
        String name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-任务进度";
        EasyExcelUtil.export(exportList, name);
    }

    /**
     * 投递会员任务进度事件
     *
     * @param req 进度上报入参
     * @return 空结果
     */
    @PostMapping("/send")
    public PlatformResult<Void> send(@Valid @RequestBody MemberTaskRecordAddReq req) {
        memberTaskRecordService.sendMessage(req);
        return PlatformResult.success();
    }
}
