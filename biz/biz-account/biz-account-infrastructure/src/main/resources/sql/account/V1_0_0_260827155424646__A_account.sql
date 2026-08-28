ALTER TABLE
  `account` MODIFY COLUMN `client` varchar(11) NOT NULL COMMENT '所属端[admin平台端,user用户端,partner服务商端,channel渠道商端,supplier供应商端,mmt_channel脉脉通渠道商端]';
