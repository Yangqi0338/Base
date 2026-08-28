CREATE TABLE `article` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `title` varchar(255) NULL COMMENT '文章标题',
  `content` text NULL COMMENT '文章内容',
  `cover_image` varchar(255) NULL COMMENT '封面图URL',
  `poster_images` varchar(255) NULL COMMENT '海报轮播图URL列表(JSON数组, 源列 poster_images)',
  `category_id` bigint NULL COMMENT '分类ID',
  `is_visible` int NULL COMMENT '是否显示(0-不显示, 1-显示)',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '文章数据实体';
