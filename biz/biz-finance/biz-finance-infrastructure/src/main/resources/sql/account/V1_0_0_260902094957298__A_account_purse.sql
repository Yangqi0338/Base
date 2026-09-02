ALTER TABLE
  `account_purse` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `account_id` bigint NULL COMMENT 'accountId',
  MODIFY COLUMN `account_name` varchar(255) NULL COMMENT 'accountName',
  MODIFY COLUMN `purse_type` int NULL COMMENT 'purseType[0总账户,11商品分润账户,13商品货款结余账户,2采购金账户,3保证金账户,4营销账户,5商品位]',
  MODIFY COLUMN `account_type` int NULL COMMENT 'accountType[0会员,1供应商,2渠道商]',
  MODIFY COLUMN `amount` bigint NULL COMMENT 'amount',
  MODIFY COLUMN `total_amount` int NULL COMMENT 'totalAmount',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'AccountPurseDO表';
