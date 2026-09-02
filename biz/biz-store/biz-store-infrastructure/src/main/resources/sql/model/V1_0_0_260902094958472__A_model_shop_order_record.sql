ALTER TABLE
  `model_shop_order_record` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `store_id` bigint NULL COMMENT 'storeId',
  MODIFY COLUMN `model_shop_id` bigint NULL COMMENT 'modelShopId',
  MODIFY COLUMN `type` varchar(255) NULL COMMENT 'type',
  MODIFY COLUMN `amount` bigint NULL COMMENT 'amount',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'ModelShopOrderRecordDO表';
