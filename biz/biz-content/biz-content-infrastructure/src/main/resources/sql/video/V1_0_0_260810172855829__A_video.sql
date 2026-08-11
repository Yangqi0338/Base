ALTER TABLE
  `video` DROP COLUMN `creator_name`,
  DROP COLUMN `shares_num`,
  DROP COLUMN `issuer`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '视频名称',
  MODIFY COLUMN `category_id` bigint NULL COMMENT '分类ID',
  MODIFY COLUMN `is_visible` int NULL COMMENT '是否显示(0-不显示, 1-显示)' AFTER `category_id`,
  MODIFY COLUMN `video_url` varchar(255) NULL COMMENT '视频地址' AFTER `is_visible`,
  MODIFY COLUMN `video_cover_url` varchar(255) NULL COMMENT '视频封面地址' AFTER `video_url`,
  MODIFY COLUMN `issuer_id` bigint NULL COMMENT '发布人id' AFTER `video_cover_url`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `issuer_id`,
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '视频数据实体';
