ALTER TABLE
  `brand` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `account_id` bigint NULL COMMENT 'accountId',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT 'name',
  MODIFY COLUMN `logo` varchar(255) NULL COMMENT 'logo',
  MODIFY COLUMN `state` varchar(8) NULL COMMENT 'state[PENDING待审核,APPROVED已通过,REJECTED已拒绝]',
  MODIFY COLUMN `category_id_list` varchar(255) NULL COMMENT 'categoryIdList',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'BrandDO表';
