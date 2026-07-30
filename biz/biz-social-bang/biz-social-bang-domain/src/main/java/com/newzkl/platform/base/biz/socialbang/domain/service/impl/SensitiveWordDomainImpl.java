package com.newzkl.platform.base.biz.socialbang.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.socialbang.domain.adapt.repository.SensitiveWordRepository;
import com.newzkl.platform.base.biz.socialbang.domain.service.SensitiveWordDomain;
import com.newzkl.platform.base.biz.socialbang.model.im.query.SensitiveWordQuery;
import com.newzkl.platform.base.biz.socialbang.model.im.req.SensitiveWordRequest;
import com.newzkl.platform.base.biz.socialbang.model.im.vo.SensitiveWord;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * {@code SensitiveWordDomain} 实现
 *
 * <p>迁移自 {@code SensitiveWordServiceImpl} + {@code SensitiveWordDomainServiceImpl},
 * 校验规则(非空/255长度/来源枚举/addTime 默认值)与查重、编辑排除自身、逐条导入不中断等逻辑逐条保留。
 * 源 {@code ThrowsException.exception(BaseErrorCode.PARAM, msg)} 语义不变。</p>
 *
 * @author KC
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SensitiveWordDomainImpl implements SensitiveWordDomain {

    /**
     * 敏感词内容长度上限
     */
    private static final int WORD_MAX_LENGTH = 255;

    /**
     * 来源类型: 手动添加
     */
    private static final String SOURCE_MANUAL = "manual";

    /**
     * 来源类型: 批量导入
     */
    private static final String SOURCE_BATCH = "batch";

    private final SensitiveWordRepository sensitiveWordRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SensitiveWord addSensitiveWord(SensitiveWordRequest request) {
        log.info("开始添加敏感词：{}", request.getSensitiveWord());
        SensitiveWord word = new SensitiveWord();
        word.setSensitiveWord(request.getSensitiveWord());
        word.setSourceType(request.getSourceType());
        createValidate(word);

        Optional<SensitiveWord> existWord = sensitiveWordRepository.findByWord(request.getSensitiveWord());
        if (existWord.isPresent()) {
            ThrowsException.exception(BaseErrorCode.PARAM, "敏感词已存在，请勿重复添加");
        }

        SensitiveWord savedWord = sensitiveWordRepository.saveSensitiveWord(word);
        log.info("添加敏感词成功，敏感词：{}，ID：{}", savedWord.getSensitiveWord(), savedWord.getId());
        return savedWord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editSensitiveWord(SensitiveWordRequest request) {
        log.info("开始编辑敏感词，ID：{}，新内容：{}", request.getId(), request.getSensitiveWord());
        SensitiveWord word = new SensitiveWord();
        word.setSensitiveWord(request.getSensitiveWord());
        word.setId(request.getId());
        word.setSourceType(request.getSourceType());
        updateValidate(word);

        SensitiveWord existWord = sensitiveWordRepository.getById(request.getId());
        if (existWord == null) {
            ThrowsException.exception(BaseErrorCode.PARAM, "待编辑的敏感词不存在");
            return false;
        }

        if (!existWord.getSensitiveWord().equals(request.getSensitiveWord())) {
            Optional<SensitiveWord> duplicateWord = sensitiveWordRepository.findByWord(request.getSensitiveWord());
            if (duplicateWord.isPresent()) {
                ThrowsException.exception(BaseErrorCode.PARAM, "修改后的敏感词已存在，请勿重复");
            }
        }

        boolean success = sensitiveWordRepository.updateSensitiveWord(word);
        log.info("编辑敏感词{}，ID：{}", success ? "成功" : "失败", request.getId());
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSensitiveWord(Long id) {
        log.info("开始删除敏感词：{}", id);
        SensitiveWord word = sensitiveWordRepository.getById(id);
        if (Objects.isNull(word)) {
            ThrowsException.exception(BaseErrorCode.PARAM, "待删除的敏感词不存在");
            return false;
        }
        boolean success = sensitiveWordRepository.removeById(word.getId());
        log.info("删除敏感词{}，内容：{}", success ? "成功" : "失败", word.getSensitiveWord());
        return success;
    }

    @Override
    public IPage<SensitiveWord> pageSensitiveWord(SensitiveWordQuery query) {
        log.info("分页查询敏感词，条件：{}", query);
        IPage<SensitiveWord> pageResult = sensitiveWordRepository.pageQuery(query);
        log.info("分页查询敏感词完成，总条数：{}，总页数：{}", pageResult.getTotal(), pageResult.getPages());
        return pageResult;
    }

    @Override
    public SensitiveWord getByWord(Long id) {
        log.info("查询敏感词详情：{}", id);
        return sensitiveWordRepository.getById(id);
    }

    @Override
    public String importSensitiveWord(List<String> words, int totalRowNum) {
        Set<String> wordSet = new HashSet<>();
        List<String> errorMsgList = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        List<SensitiveWordRequest> requestList = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            // 行号(表头 1 行, 数据从第 2 行开始), 与源一致
            int rowNum = i + 2;
            String word = StrUtil.trimToNull(words.get(i));

            if (word == null) {
                errorMsgList.add(String.format("第%s行：敏感词为空，跳过导入", rowNum));
                failCount++;
                continue;
            }

            if (wordSet.contains(word)) {
                errorMsgList.add(String.format("第%s行：敏感词【%s】在Excel内重复，跳过导入", rowNum, word));
                failCount++;
                continue;
            }
            wordSet.add(word);

            SensitiveWordRequest request = new SensitiveWordRequest();
            request.setSensitiveWord(word);
            request.setSourceType(SOURCE_BATCH);
            requestList.add(request);
        }

        for (SensitiveWordRequest req : requestList) {
            String word = req.getSensitiveWord();
            try {
                addSensitiveWord(req);
                successCount++;
                log.info("敏感词【{}】导入成功", word);
            } catch (Exception e) {
                String errorMsg = String.format("敏感词【%s】导入失败：%s", word, e.getMessage());
                errorMsgList.add(errorMsg);
                failCount++;
                log.error(errorMsg, e);
            }
        }

        StringBuilder resultMsg = new StringBuilder();
        resultMsg.append("敏感词导入完成！")
                .append("Excel总数据量：").append(totalRowNum)
                .append("，有效数据量：").append(requestList.size())
                .append("，成功：").append(successCount)
                .append("，失败：").append(failCount);
        if (CollUtil.isNotEmpty(errorMsgList)) {
            resultMsg.append("；失败详情：").append(String.join(" | ", errorMsgList));
        }
        log.info(resultMsg.toString());
        return resultMsg.toString();
    }

    /**
     * 新增校验: 内容非空、长度上限、来源枚举、添加时间默认值
     *
     * @param sensitiveWord 敏感词实体
     */
    private void createValidate(SensitiveWord sensitiveWord) {
        if (StrUtil.isBlank(sensitiveWord.getSensitiveWord())) {
            ThrowsException.exception(BaseErrorCode.PARAM, "敏感词内容不能为空");
        }
        if (sensitiveWord.getSensitiveWord().length() > WORD_MAX_LENGTH) {
            ThrowsException.exception(BaseErrorCode.PARAM, "敏感词内容长度不能超过255个字符");
        }
        if (!SOURCE_MANUAL.equals(sensitiveWord.getSourceType()) && !SOURCE_BATCH.equals(sensitiveWord.getSourceType())) {
            ThrowsException.exception(BaseErrorCode.PARAM, "来源类型仅支持：manual（手动添加）、batch（批量导入）");
        }
        if (sensitiveWord.getAddTime() == null) {
            sensitiveWord.setAddTime(LocalDateTime.now());
        }
    }

    /**
     * 编辑校验: 主键非空 + 复用新增校验
     *
     * @param sensitiveWord 敏感词实体
     */
    private void updateValidate(SensitiveWord sensitiveWord) {
        if (sensitiveWord.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM, "编辑敏感词必须指定主键ID");
        }
        createValidate(sensitiveWord);
    }
}
