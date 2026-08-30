ALTER TABLE
  `account_purse_roll_out` MODIFY COLUMN `account_type` int NULL COMMENT '客户类型[0会员,1供应商,2渠道商]',
  MODIFY COLUMN `purse_type` int NULL COMMENT '账户类型[0总账户,11商品分润账户,13商品货款结余账户,2采购金账户,3保证金账户,4营销账户,5商品位]';
