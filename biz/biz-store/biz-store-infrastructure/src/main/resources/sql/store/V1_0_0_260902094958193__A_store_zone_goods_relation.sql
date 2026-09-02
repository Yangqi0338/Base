ALTER TABLE
  `store_zone_goods_relation` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `zone_code` varchar(255) NULL COMMENT 'zoneCode',
  MODIFY COLUMN `goods_id` bigint NULL COMMENT 'goodsId',
  MODIFY COLUMN `store_id` bigint NULL COMMENT 'storeId',
  MODIFY COLUMN `store_name` varchar(255) NULL COMMENT 'storeName',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'StoreZoneGoodsRelationDO表';
