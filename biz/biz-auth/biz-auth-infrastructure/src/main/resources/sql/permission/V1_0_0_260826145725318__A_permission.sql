ALTER TABLE
  `permission` MODIFY COLUMN `client` varchar(8) NULL COMMENT '所属端[admin平台端,user用户端,partner服务商端,channel渠道商端,supplier供应商端]',
  MODIFY COLUMN `type` varchar(4) NULL COMMENT '权限类型[MENU菜单,FUNC功能]';
