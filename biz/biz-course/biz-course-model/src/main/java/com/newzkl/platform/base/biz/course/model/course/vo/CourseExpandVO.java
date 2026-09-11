package com.newzkl.platform.base.biz.course.model.course.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName
public class CourseExpandVO implements Serializable {

    /**
     * 课程名称
     */
    private String categoryName;

    /**
     * 讲师名称
     */
    private String lecturerName;

}
