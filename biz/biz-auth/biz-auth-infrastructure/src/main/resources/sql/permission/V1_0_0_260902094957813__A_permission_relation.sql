ALTER TABLE
  `permission_relation` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `client` varchar(11) NULL COMMENT 'client[admin平台端,user用户端,partner服务商端,channel渠道商端,supplier供应商端,mmt_channel脉脉通渠道商端]',
  MODIFY COLUMN `type` varchar(18) NULL COMMENT 'type[ACCOUNT_ROLE账号-角色,ROLE_PERMISSION角色-权限,ACCOUNT_PERMISSION账号-权限]',
  MODIFY COLUMN `source` varchar(255) NULL COMMENT 'source',
  MODIFY COLUMN `target` varchar(255) NULL COMMENT 'target',
  MODIFY COLUMN `origin` varchar(12) NULL COMMENT 'origin[ROLE_DERIVED角色派生,DIRECT直接授权]',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'PermissionRelationDO表';
