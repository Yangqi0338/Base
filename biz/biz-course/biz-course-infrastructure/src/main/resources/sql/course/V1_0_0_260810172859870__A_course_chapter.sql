ALTER TABLE
  `course_chapter` DROP COLUMN `is_deleted`,
  DROP COLUMN `create_by`,
  DROP COLUMN `update_by`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `course_id` bigint NULL COMMENT '所属课程ID(关联课程表主键)',
  MODIFY COLUMN `title` varchar(255) NULL COMMENT '章节标题',
  MODIFY COLUMN `chapter_num` int NULL COMMENT '章节数(从1开始, 同一课程下不重复)',
  MODIFY COLUMN `virtual_study_count` int NULL COMMENT '虚拟学习人数',
  MODIFY COLUMN `is_free` int NULL COMMENT '是否免费',
  MODIFY COLUMN `self_media_url` varchar(255) NULL COMMENT '自媒体上传视频URL',
  MODIFY COLUMN `external_media_url` varchar(255) NULL COMMENT '外部自媒体链接',
  MODIFY COLUMN `publish_time` datetime NULL COMMENT '发布时间',
  MODIFY COLUMN `duration_centisecond` int NULL COMMENT '章节视频时长(单位百分秒)',
  MODIFY COLUMN `duration_desc` varchar(255) NULL COMMENT '时长描述(冗余字段用于前端展示)',
  MODIFY COLUMN `is_enabled` int NULL COMMENT '是否启用',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `is_enabled`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '课程章节数据对象

<p>{@code courseId} 指向 {@code course.id}(源字段注释写"课程编号", 实际类型与用法均为课程主键)。
时长列单位为百分秒, 入参单位为秒, 换算在仓储实现完成。</p>';
