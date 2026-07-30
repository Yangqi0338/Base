package com.newzkl.platform.base.biz.user.model.task.record.res;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 会员任务进度导出出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.memberTaskRecord.model.vo.MemberTaskRecordExportVO}。
 * 导出列与列序逐字沿用旧契约。</p>
 *
 * @author KC
 */
@Data
public class MemberTaskRecordExportRes {

    /**
     * 创建时间导出格式
     */
    private static final DateTimeFormatter CREATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称", index = 0)
    @ColumnWidth(15)
    private String memberNickname;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID", index = 1)
    @ColumnWidth(20)
    private String memberId;

    /**
     * 任务名称
     */
    @ExcelProperty(value = "任务名称", index = 2)
    @ColumnWidth(20)
    private String taskName;

    /**
     * 任务ID（对应任务编号）
     */
    @ExcelProperty(value = "任务ID", index = 3)
    @ColumnWidth(15)
    private String taskNum;

    /**
     * 任务类型描述
     */
    @ExcelProperty(value = "任务类型", index = 4)
    @ColumnWidth(15)
    private String taskTypeDesc;

    /**
     * 任务状态描述
     */
    @ExcelProperty(value = "任务状态", index = 5)
    @ColumnWidth(15)
    private String taskStatusDesc;

    /**
     * 完成条件
     */
    @ExcelProperty(value = "完成条件", index = 6)
    @ColumnWidth(25)
    private String completeCondition;

    /**
     * 任务进度
     */
    @ExcelProperty(value = "任务进度", index = 7)
    @ColumnWidth(20)
    private String taskProgress;

    /**
     * 红包奖励（元）
     */
    @ExcelProperty(value = "红包奖励", index = 8)
    @ColumnWidth(12)
    private BigDecimal redPacketRewardYuan;

    /**
     * 创建时间（格式化字符串）
     */
    @ExcelProperty(value = "创建时间", index = 9)
    @ColumnWidth(20)
    private String createTime;

    /**
     * 设置创建时间（LocalDateTime 格式化为字符串）
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime == null ? "" : createTime.format(CREATE_TIME_FORMATTER);
    }
}
