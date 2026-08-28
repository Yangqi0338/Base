ALTER TABLE
  `account` MODIFY COLUMN `client` varchar(8) NOT NULL COMMENT '所属端[admin平台端,user用户端,partner服务商端,channel渠道商端,supplier供应商端]',
  MODIFY COLUMN `state` int NULL COMMENT '状态[-1已注销,0已封禁,1正常]',
  MODIFY COLUMN `tripartite_account_permission` int NULL COMMENT '三方账户权限[1是,0否]';
