ALTER TABLE
  `user_interaction` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `user_id` bigint NULL COMMENT 'userId',
  MODIFY COLUMN `publisher_id` bigint NULL COMMENT 'publisherId',
  MODIFY COLUMN `store_id` bigint NULL COMMENT 'storeId',
  MODIFY COLUMN `target_type` varchar(16) NULL COMMENT 'targetType[USER_VIDEO用户视频,PRODUCT商品,PRODUCT_VIDEO商品视频,INFLUENCER_VIDEO达人视频,ARTICLE文章]',
  MODIFY COLUMN `target_id` bigint NULL COMMENT 'targetId',
  MODIFY COLUMN `action_type` varchar(5) NULL COMMENT 'actionType[VIEW浏览,LIKE点赞,SHARE转发]',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'UserInteractionDO表';
