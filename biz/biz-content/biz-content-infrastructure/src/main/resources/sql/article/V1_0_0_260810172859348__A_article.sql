ALTER TABLE
  `article` DROP COLUMN `creator_name`,
  DROP COLUMN `issuer_id`,
  DROP COLUMN `issuer`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `title` varchar(255) NULL COMMENT '文章标题',
  MODIFY COLUMN `content` text NULL COMMENT '文章内容',
  MODIFY COLUMN `cover_image` varchar(255) NULL COMMENT '封面图URL',
  MODIFY COLUMN `poster_images` varchar(255) NULL COMMENT '海报轮播图URL列表(JSON数组, 源列 poster_images)',
  MODIFY COLUMN `category_id` bigint NULL COMMENT '分类ID',
  MODIFY COLUMN `is_visible` int NULL COMMENT '是否显示(0-不显示, 1-显示)' AFTER `category_id`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `is_visible`,
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '文章数据实体';
