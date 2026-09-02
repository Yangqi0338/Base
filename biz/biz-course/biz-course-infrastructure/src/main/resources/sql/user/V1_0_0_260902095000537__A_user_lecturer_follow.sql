ALTER TABLE
  `user_lecturer_follow` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `user_id` bigint NULL COMMENT 'userId',
  MODIFY COLUMN `lecturer_id` bigint NULL COMMENT 'lecturerId',
  MODIFY COLUMN `follow_time` datetime NULL COMMENT 'followTime',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'UserLecturerFollowDO表';
