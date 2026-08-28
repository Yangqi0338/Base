CREATE TABLE `account_login_log` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `account_id` bigint NULL COMMENT '账号ID(查询)',
  `login_time` datetime NULL COMMENT '登录时间',
  `login_type` int NULL COMMENT '登录方式',
  `login_ip` varchar(255) NULL COMMENT '登录IP',
  PRIMARY KEY (`id`)
) COMMENT = '登录记录';
