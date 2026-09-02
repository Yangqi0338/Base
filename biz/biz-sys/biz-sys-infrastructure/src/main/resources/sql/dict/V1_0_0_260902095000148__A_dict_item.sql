ALTER TABLE
  `dict_item` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `dict_id` bigint NULL COMMENT 'dictId',
  MODIFY COLUMN `item_key` varchar(255) NULL COMMENT 'itemKey',
  MODIFY COLUMN `item_value` varchar(255) NULL COMMENT 'itemValue',
  MODIFY COLUMN `sort` int NULL COMMENT 'sort',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'DictItemDO表';
