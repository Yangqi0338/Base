ALTER TABLE
  `account_withdraw_record` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `amount` bigint NULL COMMENT '金额',
  MODIFY COLUMN `goods_points` int NULL COMMENT '提货积分',
  MODIFY COLUMN `config` json NULL COMMENT '配置',
  MODIFY COLUMN `finish_time` varchar(255) NULL COMMENT '提现时间',
  MODIFY COLUMN `tripartite_trade_no` varchar(255) NULL COMMENT '三方交易单号',
  MODIFY COLUMN `state` int NULL COMMENT '状态[0待审核,1通过,2拒绝,3退汇]',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))';
