ALTER TABLE
  `account_login_log` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `account_id` bigint NULL COMMENT 'accountId',
  MODIFY COLUMN `login_time` datetime NULL COMMENT 'loginTime',
  MODIFY COLUMN `login_type` int NULL COMMENT 'loginType[0密码,1验证码,2自动刷新,3切换身份]',
  MODIFY COLUMN `login_ip` varchar(255) NULL COMMENT 'loginIp',
  COMMENT = 'AccountLoginLogDO表';
