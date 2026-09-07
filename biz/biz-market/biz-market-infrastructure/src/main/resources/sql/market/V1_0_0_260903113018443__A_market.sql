ALTER TABLE
  `market` DROP COLUMN `market_level`,
  DROP COLUMN `client_id`,
  MODIFY COLUMN `sub_bind_num` int NULL COMMENT '渠道商绑定数',
  DROP INDEX `auto_idx_market_client_id`;
