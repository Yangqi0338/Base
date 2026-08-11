ALTER TABLE
  `account_login_log` DROP COLUMN `create_time`,
  DROP COLUMN `update_time`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '账号ID(查询)',
  MODIFY COLUMN `login_time` datetime NULL COMMENT '登录时间',
  MODIFY COLUMN `login_ip` varchar(255) NULL COMMENT '登录IP';
