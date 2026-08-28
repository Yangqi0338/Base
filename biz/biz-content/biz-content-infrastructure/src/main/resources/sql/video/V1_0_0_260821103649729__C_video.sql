CREATE TABLE `video` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `name` varchar(255) NULL COMMENT '视频名称',
  `category_id` bigint NULL COMMENT '分类ID',
  `is_visible` int NULL COMMENT '是否显示(0-不显示, 1-显示)',
  `video_url` varchar(255) NULL COMMENT '视频地址',
  `video_cover_url` varchar(255) NULL COMMENT '视频封面地址',
  `issuer_id` bigint NULL COMMENT '发布人id',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '视频数据实体';
