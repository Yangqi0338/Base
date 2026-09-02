ALTER TABLE
  `deliver` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `spu_order_id` bigint NULL COMMENT 'spuOrderId',
  MODIFY COLUMN `deliver_username` varchar(255) NULL COMMENT 'deliverUsername',
  MODIFY COLUMN `express_company_name` varchar(255) NULL COMMENT 'expressCompanyName',
  MODIFY COLUMN `express_no` varchar(255) NULL COMMENT 'expressNo',
  MODIFY COLUMN `express_mobile` varchar(255) NULL COMMENT 'expressMobile',
  MODIFY COLUMN `item` json NULL COMMENT 'item',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'DeliverDO表';
