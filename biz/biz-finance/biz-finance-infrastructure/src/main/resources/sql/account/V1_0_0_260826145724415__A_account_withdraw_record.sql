ALTER TABLE
  `account_withdraw_record` MODIFY COLUMN `state` int NULL COMMENT '状态[0待审核,1通过,2拒绝,3退汇]';
