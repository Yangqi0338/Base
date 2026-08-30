ALTER TABLE
  `account` DROP COLUMN `p_role_list`,
ADD
  COLUMN `main_account_id` bigint NULL COMMENT '顶层主账号id (主账号自身=0, 子账号=顶层主账号id)' AFTER `pid_list`,
ADD
  COLUMN `origin` int NULL COMMENT '账号来源[1自行注册,2主账号创建,3邀请]' AFTER `main_account_id`,
ADD
  INDEX `auto_idx_account_main_account_id`(`main_account_id`) COMMENT '顶层主账号id (主账号自身=0, 子账号=顶层主账号id)';
