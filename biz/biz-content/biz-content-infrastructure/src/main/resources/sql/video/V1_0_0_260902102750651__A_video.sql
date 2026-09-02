ALTER TABLE
  `video` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '视频名称',
  MODIFY COLUMN `category_id` bigint NULL COMMENT '分类ID',
  MODIFY COLUMN `is_visible` int NULL COMMENT '是否显示(0-不显示, 1-显示)[1是,0否]',
  MODIFY COLUMN `video_url` varchar(255) NULL COMMENT '视频地址',
  MODIFY COLUMN `video_cover_url` varchar(255) NULL COMMENT '视频封面地址',
  MODIFY COLUMN `issuer_id` bigint NULL COMMENT '发布人id',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '视频数据实体';
