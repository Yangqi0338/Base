ALTER TABLE
  `account_purse_roll_out` MODIFY COLUMN `audit_state` int NULL COMMENT '审核状态[0待用户提交,1待审核,2通过,3未通过,4终止]',
  MODIFY COLUMN `tripartite_trade_state` int NULL COMMENT '三方交易状态[0拒绝,1通过]';
