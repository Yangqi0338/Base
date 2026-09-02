ALTER TABLE
  `bill_order_award` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `identity` int NULL COMMENT '角色[1平台管理员,2平台员工,1000会员,1001供应商,1002渠道商,1003服务商,1004脉脉通渠道商]',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '账号id',
  MODIFY COLUMN `username` varchar(255) NULL COMMENT '用戶名即手机号',
  MODIFY COLUMN `purse_type` int NULL COMMENT '钱包类型[0总账户,11商品分润账户,13商品货款结余账户,2采购金账户,3保证金账户,4营销账户,5商品位]',
  MODIFY COLUMN `account_type` int NULL COMMENT '账户类型[0会员,1供应商,2渠道商]',
  MODIFY COLUMN `amount` bigint NULL COMMENT '金额',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '订单奖励账单 DO';
