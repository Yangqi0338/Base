ALTER TABLE
  `permission_relation` MODIFY COLUMN `client` varchar(8) NULL COMMENT '所属端[admin平台端,user用户端,partner服务商端,channel渠道商端,supplier供应商端]',
  MODIFY COLUMN `type` varchar(18) NULL COMMENT '关系类型[ACCOUNT_ROLE账号-角色,ROLE_PERMISSION角色-权限,ACCOUNT_PERMISSION账号-权限]',
  MODIFY COLUMN `source` varchar(12) NULL COMMENT '关系来源[ROLE_DERIVED角色派生,DIRECT直接授权]';
