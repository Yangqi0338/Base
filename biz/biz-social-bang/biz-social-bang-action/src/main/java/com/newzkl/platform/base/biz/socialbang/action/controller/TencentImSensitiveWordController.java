package com.newzkl.platform.base.biz.socialbang.action.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.socialbang.domain.service.SensitiveWordDomain;
import com.newzkl.platform.base.biz.socialbang.model.im.query.SensitiveWordQuery;
import com.newzkl.platform.base.biz.socialbang.model.im.req.SensitiveWordRequest;
import com.newzkl.platform.base.biz.socialbang.model.im.vo.SensitiveWord;
import com.newzkl.platform.base.biz.socialbang.model.im.vo.SensitiveWordImportVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 腾讯IM-敏感词管理
 *
 * <p>迁移自 {@code com.zkl.scm.im.api.controller.TencentImSensitiveWordController}。
 * 类级路径含尾斜杠 {@code /im/sensitiveWord/}、方法级路径不带前导斜杠、
 * {@code import} 用 {@code PUT} 等旧写法逐字保留, 不做 REST 规整。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/im/sensitiveWord/")
@RequiredArgsConstructor
@Slf4j
public class TencentImSensitiveWordController {

    private final SensitiveWordDomain sensitiveWordDomain;

    /**
     * 添加敏感词
     *
     * @param sensitiveWord 敏感词实体信息
     * @return 保存后的敏感词实体
     */
    @PostMapping("add")
    public PlatformResult<SensitiveWord> addSensitiveWord(@RequestBody SensitiveWordRequest sensitiveWord) {
        log.info("开始添加敏感词：{}", sensitiveWord.getSensitiveWord());
        SensitiveWord savedWord = sensitiveWordDomain.addSensitiveWord(sensitiveWord);
        log.info("添加敏感词成功，敏感词：{}，ID：{}", savedWord.getSensitiveWord(), savedWord.getId());
        return PlatformResult.success(savedWord);
    }

    /**
     * 编辑敏感词
     *
     * @param sensitiveWord 敏感词实体(含主键ID)
     * @return 是否编辑成功
     */
    @PostMapping("edit")
    public PlatformResult<Boolean> editSensitiveWord(@RequestBody SensitiveWordRequest sensitiveWord) {
        log.info("开始编辑敏感词，ID：{}，新内容：{}", sensitiveWord.getId(), sensitiveWord.getSensitiveWord());
        boolean result = sensitiveWordDomain.editSensitiveWord(sensitiveWord);
        log.info("编辑敏感词{}，ID：{}", result ? "成功" : "失败", sensitiveWord.getId());
        return PlatformResult.success(result);
    }

    /**
     * 根据id删除敏感词
     *
     * @param id 敏感词主键
     * @return 是否删除成功
     */
    @PostMapping("delete/{id}")
    public PlatformResult<Boolean> deleteSensitiveWord(@PathVariable Long id) {
        log.info("开始删除敏感词：{}", id);
        boolean result = sensitiveWordDomain.deleteSensitiveWord(id);
        log.info("删除敏感词{}，内容：{}", result ? "成功" : "失败", id);
        return PlatformResult.success(result);
    }

    /**
     * 分页查询敏感词列表
     *
     * @param queryDTO 分页查询条件(含页码、页大小、筛选条件等)
     * @return 分页结果
     */
    @PostMapping("pageList")
    public PlatformResult<IPage<SensitiveWord>> pageSensitiveWord(@RequestBody SensitiveWordQuery queryDTO) {
        log.info("分页查询敏感词列表，查询条件：{}", queryDTO);
        IPage<SensitiveWord> pageResult = sensitiveWordDomain.pageSensitiveWord(queryDTO);
        log.info("分页查询敏感词完成，总条数：{}，总页数：{}", pageResult.getTotal(), pageResult.getPages());
        return PlatformResult.success(pageResult);
    }

    /**
     * 根据id查详情
     *
     * @param id 敏感词主键
     * @return 敏感词详情
     */
    @GetMapping("detail/{id}")
    public PlatformResult<SensitiveWord> getByWord(@PathVariable Long id) {
        log.info("查询敏感词详情：{}", id);
        SensitiveWord sensitiveWordInfo = sensitiveWordDomain.getByWord(id);
        return PlatformResult.success(sensitiveWordInfo);
    }

    /**
     * 批量导入敏感词(Excel)
     *
     * <p>沿用旧接口风格: PUT 请求 + 表单上传文件。
     * 迁移偏离: 源用 easypoi {@code ExcelImportUtil}, Base 统一 EasyExcel;
     * 表头 1 行、取第 0 个 sheet 与源 {@code ImportParams} 一致,
     * 源 {@code needVerify} 的必填校验由领域层逐行空值判断承担(文案不变)。</p>
     *
     * @param file 上传的Excel文件
     * @return 导入结果
     */
    @PutMapping("import")
    public PlatformResult<Object> importSensitiveWord(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return PlatformResult.fail("上传文件不能为空！");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
            return PlatformResult.fail("请上传Excel文件（.xlsx/.xls格式）！");
        }

        List<SensitiveWordImportVO> excelDataList;
        try (InputStream inputStream = file.getInputStream()) {
            excelDataList = EasyExcel.read(inputStream)
                    .head(SensitiveWordImportVO.class)
                    .sheet(0)
                    .headRowNumber(1)
                    .doReadSync();
        } catch (IOException | RuntimeException e) {
            log.error("敏感词导入-Excel解析失败", e);
            return PlatformResult.fail("文件解析异常：" + e.getMessage());
        }

        if (excelDataList == null || excelDataList.isEmpty()) {
            return PlatformResult.fail("Excel中无有效数据！");
        }

        List<String> words = excelDataList.stream().map(SensitiveWordImportVO::getSensitiveWord).toList();
        return PlatformResult.success(sensitiveWordDomain.importSensitiveWord(words, excelDataList.size()));
    }
}
