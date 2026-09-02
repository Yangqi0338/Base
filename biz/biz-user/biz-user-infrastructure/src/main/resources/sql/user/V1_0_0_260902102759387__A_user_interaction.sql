ALTER TABLE
  `user_interaction` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `user_id` bigint NULL COMMENT '操作人ID(关联用户表)',
  MODIFY COLUMN `publisher_id` bigint NULL COMMENT '被操作对象发布者ID(视频/商品的发布者，关联用户表)',
  MODIFY COLUMN `store_id` bigint NULL COMMENT '门店ID',
  MODIFY COLUMN `target_type` varchar(16) NULL COMMENT '被操作对象类型[USER_VIDEO用户视频,PRODUCT商品,PRODUCT_VIDEO商品视频,INFLUENCER_VIDEO达人视频,ARTICLE文章]',
  MODIFY COLUMN `target_id` bigint NULL COMMENT '被操作对象ID(视频ID或商品ID，与targetType对应)',
  MODIFY COLUMN `action_type` varchar(5) NULL COMMENT '操作类型[VIEW浏览,LIKE点赞,SHARE转发]',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '用户互动操作持久化对象（存储点赞、转发记录）';
