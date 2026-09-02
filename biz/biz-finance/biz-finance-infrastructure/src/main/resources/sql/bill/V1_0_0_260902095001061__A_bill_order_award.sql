ALTER TABLE
  `bill_order_award` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id',
  MODIFY COLUMN `identity` int NULL COMMENT 'identity[1平台管理员,2平台员工,1000会员,1001供应商,1002渠道商,1003服务商,1004脉脉通渠道商]',
  MODIFY COLUMN `account_id` bigint NULL COMMENT 'accountId',
  MODIFY COLUMN `username` varchar(255) NULL COMMENT 'username',
  MODIFY COLUMN `purse_type` int NULL COMMENT 'purseType[0总账户,11商品分润账户,13商品货款结余账户,2采购金账户,3保证金账户,4营销账户,5商品位]',
  MODIFY COLUMN `account_type` int NULL COMMENT 'accountType[0会员,1供应商,2渠道商]',
  MODIFY COLUMN `amount` bigint NULL COMMENT 'amount',
  MODIFY COLUMN `executor` json NULL COMMENT 'executor',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT 'creatorId',
  MODIFY COLUMN `create_time` datetime NULL COMMENT 'createTime',
  MODIFY COLUMN `update_time` datetime NULL COMMENT 'updateTime',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT 'delFlag',
  COMMENT = 'BillOrderAwardDO表';
