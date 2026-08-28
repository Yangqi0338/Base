CREATE TABLE `course_chapter` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `course_id` bigint NULL COMMENT '所属课程ID(关联课程表主键)',
  `title` varchar(255) NULL COMMENT '章节标题',
  `chapter_num` int NULL COMMENT '章节数(从1开始, 同一课程下不重复)',
  `virtual_study_count` int NULL COMMENT '虚拟学习人数',
  `is_free` int NULL COMMENT '是否免费',
  `self_media_url` varchar(255) NULL COMMENT '自媒体上传视频URL',
  `external_media_url` varchar(255) NULL COMMENT '外部自媒体链接',
  `publish_time` datetime NULL COMMENT '发布时间',
  `duration_centisecond` int NULL COMMENT '章节视频时长(单位百分秒)',
  `duration_desc` varchar(255) NULL COMMENT '时长描述(冗余字段用于前端展示)',
  `is_enabled` int NULL COMMENT '是否启用',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '课程章节数据对象

<p>{@code courseId} 指向 {@code course.id}(源字段注释写"课程编号", 实际类型与用法均为课程主键)。
时长列单位为百分秒, 入参单位为秒, 换算在仓储实现完成。</p>';
