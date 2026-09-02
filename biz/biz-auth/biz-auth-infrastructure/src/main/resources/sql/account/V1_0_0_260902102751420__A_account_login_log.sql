ALTER TABLE
  `account_login_log` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '账号ID(查询)',
  MODIFY COLUMN `login_time` datetime NULL COMMENT '登录时间',
  MODIFY COLUMN `login_type` int NULL COMMENT '登录方式[0密码,1验证码,2自动刷新,3切换身份]',
  MODIFY COLUMN `login_ip` varchar(255) NULL COMMENT '登录IP',
  COMMENT = '登录记录';
