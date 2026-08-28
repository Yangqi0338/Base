CREATE TABLE `course_chapter_watch_record` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `user_id` bigint NULL COMMENT '用户ID',
  `course_id` bigint NULL COMMENT '课程ID(关联课程表主键)',
  `course_chapter_id` bigint NULL COMMENT '章节ID(关联课程章节表主键)',
  `course_num` varchar(255) NULL COMMENT '课程编码(冗余字段)',
  `chapter_num` int NULL COMMENT '章节数(冗余字段)',
  `is_watched` int NULL COMMENT '是否观看过',
  `watch_duration_centisecond` int NULL COMMENT '累计观看时长(单位百分秒, 预留字段)',
  `last_watch_position_centisecond` int NULL COMMENT '上次观看位置(单位百分秒, 断点续播预留字段)',
  `watch_time` datetime NULL COMMENT '首次观看时间(即完成时间)',
  `total_watch_times` int NULL COMMENT '累计观看次数',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '课程章节观看记录数据对象';
