ALTER TABLE
  `role` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `client` varchar(11) NULL COMMENT 'client[admin平台端,user用户端,partner服务商端,channel渠道商端,supplier供应商端,mmt_channel脉脉通渠道商端]',
  MODIFY COLUMN `code` varchar(255) NULL COMMENT 'code',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT 'name',
  MODIFY COLUMN `description` varchar(255) NULL COMMENT 'description',
  MODIFY COLUMN `sort` int NULL COMMENT 'sort',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'RoleDO表';
