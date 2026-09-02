ALTER TABLE
  `account_purse` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `account_name` varchar(255) NULL COMMENT '客户名称',
  MODIFY COLUMN `purse_type` int NULL COMMENT '账户类型[0总账户,11商品分润账户,13商品货款结余账户,2采购金账户,3保证金账户,4营销账户,5商品位]',
  MODIFY COLUMN `account_type` int NULL COMMENT '客户类型[0会员,1供应商,2渠道商]',
  MODIFY COLUMN `amount` bigint NULL COMMENT '账户余额',
  MODIFY COLUMN `total_amount` int NULL COMMENT '累计入账总额',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))';
