package com.newzkl.platform.base.biz.account.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.service.AdminClientDomain;
import com.newzkl.platform.base.biz.account.model.req.EmpCreateReq;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import com.newzkl.platform.base.biz.account.model.res.EmpRes;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 员工控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/user/emp")
@RequiredArgsConstructor
public class EmpController {

    private final AdminClientDomain adminClientDomain;

    /**
     * 批量新增员工。
     *
     * @param empCreateReqList 员工创建请求列表
     * @return 成功结果
     */
    @PostMapping("batchEmpCreate")
    public ScmResult<Void> batchEmpCreate(@Validated @RequestBody List<EmpCreateReq> empCreateReqList) {
        adminClientDomain.batchEmpCreate(empCreateReqList, SecurityUtils.getAccountId());
        return ScmResult.success();
    }

    /**
     * 员工分页。
     *
     * @param empQuery 员工查询
     * @return 员工分页
     */
    @PostMapping("empPage")
    public ScmResult<Page<EmpRes>> empPage(@RequestBody EmpQuery empQuery) {
        return ScmResult.success(adminClientDomain.empPage(empQuery));
    }

    /**
     * Excel 批量新增员工。
     *
     * @param file 上传的 Excel 文件
     * @return 导入结果
     * @throws IOException 文件读取异常
     */
    @PostMapping("excelCreateEmp")
    public ScmResult<EasyExcelErrorVO> excelCreateEmp(@RequestParam("file") MultipartFile file) throws IOException {
        return ScmResult.success(adminClientDomain.excelCreateEmp(file.getInputStream(), SecurityUtils.getAccountId()));
    }
}
