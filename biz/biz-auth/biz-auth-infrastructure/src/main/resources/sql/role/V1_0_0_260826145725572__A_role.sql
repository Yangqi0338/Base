ALTER TABLE
  `role` MODIFY COLUMN `client` varchar(8) NULL COMMENT '所属端[admin平台端,user用户端,partner服务商端,channel渠道商端,supplier供应商端]';
