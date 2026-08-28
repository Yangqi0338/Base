ALTER TABLE
  `user_follow` DROP INDEX `auto_idx_idx_key`,
ADD
  UNIQUE INDEX `auto_idx_key`(`follower_id`, `following_id`, `del_flag`);
