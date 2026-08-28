ALTER TABLE
  `user_interaction` MODIFY COLUMN `target_type` varchar(16) NULL COMMENT '被操作对象类型[USER_VIDEO用户视频,PRODUCT商品,PRODUCT_VIDEO商品视频,INFLUENCER_VIDEO达人视频,ARTICLE文章]',
  MODIFY COLUMN `action_type` varchar(5) NULL COMMENT '操作类型[VIEW浏览,LIKE点赞,SHARE转发]';
