ALTER TABLE
  `account_login_log` MODIFY COLUMN `login_type` int NULL COMMENT '登录方式[0密码,1验证码,2自动刷新,3切换身份]';
