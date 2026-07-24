package com.newzkl.platform.base.common.ddd.model.query;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.temporal.Temporal;
import java.util.List;

/**
 * @author god
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizPageQuery extends PageQuery implements Serializable {
    /**
     * id列表
     */
    private List<Long> idList;
    /**
     * 账号id
     */
    private List<Long> accountIdList;
    /**
     * 创建时间
     */
    private String[] createTime;
    /**
     * 返回类型
     */
    @JsonIgnore
    private Class<?> returnType;

    public Long getAccountId() {
        return CollUtil.getFirst(accountIdList);
    }

    public void setAccountId(Long accountId) {
        this.accountIdList = doWrapperList(accountIdList, accountId);
    }

    public Long getId() {
        return CollUtil.getFirst(idList);
    }

    public void setId(Long id) {
        this.idList = doWrapperList(idList, id);
    }

    /**
     * 根据时间设置范围(天维度)
     */
    public void setCreateDate(Object startTime) {
        if (ObjectUtil.isEmpty(startTime)) {
            return;
        }
        DateTime dateTime;
        if (startTime instanceof String) {
            dateTime = DateUtil.parse((String) startTime);
        } else if (startTime instanceof Temporal) {
            dateTime = DateUtil.date((Temporal) startTime);
        } else {
            return;
        }
        this.createTime = ArrayUtil.append(this.createTime, formatDateTime(dateTime),
                formatDateTime(DateUtil.offsetDay(dateTime, 1))
        );
    }

    public String getCreateStartTime() {
        return ArrayUtil.get(createTime, 0);
    }

    public void setCreateStartTime(Object startTime) {
        if (ObjectUtil.isEmpty(startTime)) {
            return;
        }
        DateTime dateTime;
        if (startTime instanceof String) {
            dateTime = DateUtil.parse((String) startTime);
        } else if (startTime instanceof Temporal) {
            dateTime = DateUtil.date((Temporal) startTime);
        } else {
            return;
        }
        this.createTime = ArrayUtil.setOrAppend(this.createTime, 0, formatDateTime(dateTime));
    }

    public String formatDateTime(DateTime time) {
        return DateUtil.formatDateTime(time);
    }

    public String getCreateEndTime() {
        return ArrayUtil.get(createTime, 1);
    }

    public void setCreateEndTime(Object endTime) {
        if (ObjectUtil.isEmpty(endTime)) {
            return;
        }
        if (ArrayUtil.isEmpty(createTime)) {
            // 如果没有前置创建时间,设置一个空值
            setCreateStartTime("");
        }
        DateTime dateTime;
        if (endTime instanceof String) {
            dateTime = DateUtil.parse((String) endTime);
        } else if (endTime instanceof Temporal) {
            dateTime = DateUtil.date((Temporal) endTime);
        } else {
            return;
        }
        this.createTime = ArrayUtil.append(this.createTime, formatDateTime(dateTime));
    }
}
