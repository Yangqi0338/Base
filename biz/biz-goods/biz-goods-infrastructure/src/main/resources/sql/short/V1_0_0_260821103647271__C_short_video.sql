CREATE TABLE `short_video` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `path` varchar(255) NULL COMMENT '视频路径',
  `cover_path` varchar(255) NULL COMMENT '封面路径',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '商品-短视频数据对象';
